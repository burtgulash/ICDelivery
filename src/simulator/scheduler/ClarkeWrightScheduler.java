import graph.Graph;
import graph.ShortestPaths;
import graph.Dijkstra;
import graph.Path;

import java.util.List;
import java.util.LinkedList;

import static constant.Times.*;
import static constant.Costs.*;


/**
 * Handles incoming orders by Clarke-Wright algorithm.
 * Expected running time is quadratic in number of received orders.
 * 
 */
public class ClarkeWrightScheduler implements Scheduler {
    private final int HOME;

    private boolean done = false;
    private int releaseTime;

    private ShortestPaths costMinimizer;
    private Scheduler alternative;

    private int[] toSatisfy;
    private AbstractCustomer[] customers;



    /**
     * Create strategy object for given graph.
     *
     * @param graph simulation graph to do routing in
     */
    public ClarkeWrightScheduler(Graph graph) {
        HOME           = Simulator.HOME;
        toSatisfy      = new int[graph.vertices()];
        customers      = new AbstractCustomer[graph.vertices()];
        for (int i = 0; i < customers.length; i++) 
            customers[i] = new AbstractCustomer();

        costMinimizer  = new Dijkstra(graph, Simulator.HOME);
        alternative    = new GreedyScheduler(costMinimizer);


        int deadline   = deadLine();

        // skip whole ClarkeWright if there is no time for it
        if (deadline <= 0)
            done = true;

        releaseTime = deadline;
        Calendar.addEvent(new DeadlineEvent(deadline));
    }

    // deadline is time such that two longest fully loaded consecutive trips
    // can be delivered
    private int deadLine() {
        int deadline = DAY.time() * (Simulator.TERMINATION_TIME / DAY.time());
        deadline    += MIN_ACCEPT.time();
        if (deadline >= Simulator.TERMINATION_TIME)
            deadline -= DAY.time();

        // compute average trip time
        int avgPathLength = 0;
        for (int i = 0; i < toSatisfy.length; i++)
            avgPathLength += costMinimizer.shortestPath(HOME, i).pathLength();
        avgPathLength /= toSatisfy.length;

        int maxLoadTime = Truck.MAX_CAPACITY * LOAD.time();
        int estimatedTripTime = 
            avgPathLength * MINUTES_IN_HOUR.time() / Truck.SPEED + maxLoadTime;

        return deadline - estimatedTripTime;
    }

    @Override
    public void receiveOrder(Order received) {
        if (done)
            alternative.receiveOrder(received);
        else {
            int customer = received.sentBy().customerVertex();
            toSatisfy[customer] += received.amount() - received.processed();

            customers[customer].addOrder(received);
            // order implicitly accepted, only greedy scheduler can reject it
        }
    }


    /**
     * Core of Clarke-Wright algorithm. Computes all (n choose 2) savings
     * which are then sorted and used for routing.
     *
     */
    private Saving[] computeSavings() {
        int[] d_HOME         = new int[toSatisfy.length];
        List<Saving> savings = new LinkedList<Saving>();

        for (int i = 0; i < toSatisfy.length; i++)
            if (toSatisfy[i] > 0) {
                d_HOME[i]  = costMinimizer.shortestPath(HOME, i).pathLength();
                for (int j = 0; j < i; j++)
                    if (toSatisfy[j] > 0) {
                        int d_ij = 
                               costMinimizer.shortestPath(i, j).pathLength();
                        int saved = d_HOME[i] + d_HOME[j] - d_ij;

                        // only include those that save something, otherwise 
                        // use greedy scheduler instead
                        if (saved > 0) {
                            Saving s_ij = new Saving(saved, i, j);
                            savings.add(s_ij);
                        }
                    }
            }

        return (Saving[]) savings.toArray(new Saving[savings.size()]);
    }


    /**
     * Sorts (n choose 2) computed savings in descending order.
     *
     * @param savings list of savings to sort.
     */
    private void sort(Saving[] savings) {
        java.util.Arrays.sort(savings, new java.util.Comparator<Saving>() {
                    // reverse Comparator
                   public int compare(Saving s1, Saving s2) {
                       if (s1.savedCost == s2.savedCost)
                           return 0;
                       if (s1.savedCost < s2.savedCost)
                           return 1;
                       else
                           return -1;
                   }
               });
    }


    /**
     * Computes optimal load that can be put to first truck of the two.
     *
     * @param s compute the load according to this saving.
     */
    private int computeLoad(Saving s) {
        if (toSatisfy[s.fst] < toSatisfy[s.snd])
            return Math.min(Truck.MAX_CAPACITY / 2, toSatisfy[s.fst]);
        int secondLoad = Math.min(Truck.MAX_CAPACITY / 2, toSatisfy[s.snd]);
        return Math.min(Truck.MAX_CAPACITY - secondLoad, toSatisfy[s.fst]);
    }


    @Override
    /**
     * This method is called when deadline event is reached.
     * Releases all trucks that are currently held in queue.
     *
     */
    public void releaseAll() {
        // we've done our job, let greedy scheduler do the rest
        done = true;


        Saving[] savings = computeSavings();
        sort(savings);

        for (Saving s : savings) {
            // all following savings will be negative
            if (toSatisfy[s.fst] == 0 || toSatisfy[s.snd] == 0)
                continue;

            int cargoToFst = computeLoad(s);
            int cargoToSnd = 
                   Math.min(Truck.MAX_CAPACITY - cargoToFst, toSatisfy[s.snd]);
            assert(cargoToFst >= 0);
            assert(cargoToSnd >= 0);
            assert(cargoToSnd + cargoToFst <= Truck.MAX_CAPACITY);

            Path HomeToFst = costMinimizer.shortestPath(HOME, s.fst);
            Path FstToSnd = costMinimizer.shortestPath(s.fst, s.snd);


            int firstLoad = cargoToFst + cargoToSnd;
            int firstUnload = cargoToFst;
            DeliveryTrip toFst = 
                   new DeliveryTrip(releaseTime, HomeToFst, 
                                        firstLoad, firstUnload, firstLoad);
            int secondLoad = 0;
            int secondUnload = cargoToSnd;
            DeliveryTrip toSnd = 
                   new DeliveryTrip(toFst.endTime() + 1, FstToSnd, 
                                        secondLoad, secondUnload, cargoToSnd);

            // if this double-trip can't fit between MIN_TIME and MAX_TIME,
            // skip it
            if (!shiftTrips(toFst, toSnd))
                continue;

            Path back = costMinimizer.shortestPath(s.snd, HOME);

            // while both customers at the same time need to be satisfied, 
            // send trucks
            while (toSatisfy[s.fst] > 0 && toSatisfy[s.snd] > 0) {
                cargoToFst   = Math.min(cargoToFst, toSatisfy[s.fst]);
                cargoToSnd   = Math.min(cargoToSnd, toSatisfy[s.snd]);

                firstLoad    = cargoToFst + cargoToSnd;
                firstUnload  = cargoToFst;
                DeliveryTrip toFstReal = 
                   new DeliveryTrip(releaseTime, HomeToFst, 
                                        firstLoad, firstUnload, firstLoad);
                secondLoad   = 0;
                secondUnload = cargoToSnd;
                DeliveryTrip toSndReal = 
                   new DeliveryTrip(toFstReal.endTime() + 1, FstToSnd, 
                                        secondLoad, secondUnload, cargoToSnd);
                // if something messed up, every following double-trip in this
                // loop will be almost the same
                if (!shiftTrips(toFstReal, toSndReal))
                    break;

                // back to HOME trip
                ReturnTrip backTrip = 
                       new ReturnTrip(toSndReal.endTime() + 1, back);


                // create truck for this double-trip
                Truck truck = new Truck();

                Customer fstCustomer = CustomerList.get(s.fst);
                Customer sndCustomer = CustomerList.get(s.snd);

                Event assignEvent_1 = new CustomerAssignEvent(
                      releaseTime, cargoToFst, truck, fstCustomer);
                Event assignEvent_2 = new CustomerAssignEvent(
                      releaseTime, cargoToSnd, truck, sndCustomer);

                int totalLoad = cargoToFst + cargoToSnd;
                int loadCost = totalLoad * LOADING.cost();
                Event load = new TruckLoad(
                    toFstReal.startTime(), loadCost, totalLoad, truck);


                int unloadCost_1 = cargoToFst * UNLOADING.cost();
                int unloadCost_2 = cargoToSnd * UNLOADING.cost();

                Event unload_1 = new TruckUnload(
                     toFstReal.arrivalTime(), unloadCost_1, cargoToFst, truck);
                Event unload_2 = new TruckUnload(
                     toSndReal.arrivalTime(), unloadCost_2, cargoToSnd, truck);


                Calendar.addEvent(assignEvent_1);
                Calendar.addEvent(assignEvent_2);
                Calendar.addEvent(load);
                Calendar.addEvent(unload_1);
                Calendar.addEvent(unload_2);

                assert(!customers[s.fst].allSatisfied());
                assert(!customers[s.snd].allSatisfied());

                customers[s.fst].satisfy(toFstReal.endTime(),cargoToFst,truck);
                customers[s.snd].satisfy(toSndReal.endTime(),cargoToSnd,truck);

                // dispatcher
                toFstReal.sendTruck(truck);
                toSndReal.sendTruck(toSndReal.dispatchTime(), s.fst, truck);
                backTrip.sendTruck(s.snd, truck);

                toSatisfy[s.fst] -= cargoToFst;
                toSatisfy[s.snd] -= cargoToSnd;
            }
        }



        // handle every remaining unsatisfied order
        for (int i = 0; i < customers.length; i++)
            while (!customers[i].allSatisfied()) {
                // handle by greedy Scheduler, simple 
                alternative.receiveOrder(customers[i].getCurrentOrder());

                customers[i].satisfyCurrent();
            }
    }


    /**
     * Delay trip equivalent for two trips.
     *
     * @param fst first trip to delay
     * @param snd second trip to delay
     * @return true on successfuly shifted trips, false if this double-trip 
     *     cannot be planned.
     */
    private boolean shiftTrips(DeliveryTrip fst, DeliveryTrip snd) {
        int arrivalFst  = fst.arrivalTime() % DAY.time();

        // delay if too early
        if (arrivalFst < MIN_ACCEPT.time()) {
            int delayTime = MIN_ACCEPT.time() - arrivalFst; 
            fst.delay(delayTime);
            snd.delay(delayTime);
        }

        int completion  = snd.endTime()     % DAY.time();
        if (completion > MAX_ACCEPT.time() || 
            completion < MIN_ACCEPT.time())
        {
           int delayTime = 
              DAY.time() - fst.arrivalTime() % DAY.time() + MIN_ACCEPT.time();
            fst.delay(delayTime);
            snd.delay(delayTime);
        }

        // sequence of conditions rejecting this saving
        assert(fst.arrivalTime() % DAY.time() >= MIN_ACCEPT.time());
        // if second trip too long, reject immediately
        if (snd.endTime() - snd.startTime() > 
                                    MAX_ACCEPT.time() - MIN_ACCEPT.time())
            return false;
        // if after all effort second could have not been planned to arrive
        // within the interval, reject
        if (snd.endTime() % DAY.time() > MAX_ACCEPT.time())
            return false;
        // if too late, reject
        if (snd.endTime >= Simulator.TERMINATION_TIME)
            return false;

        // accept, possibly TODO more reject conditions
        return true;
    }


    /**
     * Data struct containing information about computed saving.
     */
    private class Saving {
        int savedCost, fst, snd;

        Saving(int saved, int f, int s) {
            savedCost = saved;
            fst = f;
            snd = s;
        }
    }


    /**
     * Data structure for customer, such that Customers in CustomerList
     * don't need to be written to.
     *
     */
    private class AbstractCustomer {
        private List<Order> orderHistory;
        private List<Integer> orderRemainTons;
        private int currentOrder = 0;

        AbstractCustomer() {
            orderHistory = new LinkedList<Order>();
            orderRemainTons = new LinkedList<Integer>();
        }

        void addOrder(Order o) {
            orderHistory.add(o);
            orderRemainTons.add(o.amount() - o.processed());
        }

        Order getCurrentOrder() {
            return orderHistory.get(currentOrder);
        }

        boolean allSatisfied() {
            assert(currentOrder <= orderHistory.size());
            return currentOrder == orderHistory.size();
        }

        void satisfyCurrent() {
            currentOrder++;
        }

        void satisfy(int satisfyTime, int amount, Truck truck) {
            while (amount > 0) {
                assert(currentOrder < orderHistory.size());

                int curRemains   = orderRemainTons.get(currentOrder);
                int curAmount    = Math.min(curRemains, amount);

                orderRemainTons.set(currentOrder, curRemains - curAmount);
                amount -= curAmount;

                // update stats outside
                Order order = orderHistory.get(currentOrder);
                truck.assignOrder(order);
                order.assignTruck(truck, amount); 
                order.process(curAmount);


                Event completion = new OrderSatisfyEvent(
                             satisfyTime + 1, curAmount, truck, order);
                Calendar.addEvent(completion);

                // move to next order if this done
                if (orderRemainTons.get(currentOrder) == 0)
                    currentOrder++;
            }
        }
    }
}

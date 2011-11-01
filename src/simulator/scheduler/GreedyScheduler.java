package simulator;

import stats.Order;
import stats.Truck;

import graph.Graph;
import graph.Path;
import graph.ShortestPaths;
import graph.FloydWarshall;
import graph.Dijkstra;

// import constants
import static simulator.Times.*;



/**
 * Greedy Scheduler
 *
 * Assigns one Truck to one Order and sends it as soon as possible.
 * Only minimizes distance costs.
 *
 */
public class GreedyScheduler implements Scheduler {
    private final int TERMINATION_TIME;
    private final int DEPOT;

    private ShortestPaths costMinimizer;

    /**
     * Constructor for Greedy Scheduler
     * @param graph city map of simulation
     * @param depot depot vertex in graph
     * @param terminationTime time of end, trucks won't be sent if they would 
                              arrive after this time
     */
    public GreedyScheduler (Graph graph) {
        DEPOT = Simulator.DEPOT;
        TERMINATION_TIME = Simulator.TERMINATION_TIME;
        costMinimizer = new Dijkstra(graph, DEPOT);
    }


    @Override
    /**
     * Receives order and immediately dispatches trucks
     * @param received Order to handle
     */
    public void receiveOrder(Order received) {
        assert(received.sentBy().customerVertex() != DEPOT);


        // create Trip that will handle the Order
        // +1 to prevent status events happening before receiving
        int receivedTime   = received.received() + 1;
        int customer       = received.sentBy().customerVertex();
        int amount         = received.amount();
        Path shortestPath  = costMinimizer.shortestPath(DEPOT, customer);

        // plan with MAX_CAPACITY, that will return latest time of completion
        // Fully loaded trucks then will be sent in parallel
        // but don't just reject the order if orderedAmount was low
        int planAmount = Math.min(Truck.MAX_CAPACITY, amount);
        Trip plan = new Trip(receivedTime, shortestPath, planAmount);
        delayIfNeeded(plan);


        // reject if completion after simulation time
        if (plan.endTime >= TERMINATION_TIME) {
            Event reject = new OrderRejectEvent(receivedTime, received);
            Calendar.addEvent(reject);
            return;
        }


        // success, assign Trucks and dispatch them
        dispatchTrucks(plan, received);
    }


    /**
     * Dispatch as many truck as needed to satisfy the order
     * @param success trip that guarantees that all the trucks will manage to 
     *                deliver in time
     * @param received original received order
     */
    private void dispatchTrucks(Trip success, Order received) {
        int customer = received.sentBy().customerVertex();

        // assign new truck while there are containers to be delivered
        int orderAmount = received.amount();
        while (orderAmount > 0) {
            Truck truck = new Truck();

            // assign as many containers as the Truck can take
            int assignedAmount = Math.min(Truck.MAX_CAPACITY, orderAmount);
            orderAmount -= assignedAmount;

            // Construct new trip for each truck and delay such that 
            // no waiting is needed
            Trip truckTrip = 
                  new Trip(success.startTime, success.path, assignedAmount);

            // Now we know travelCost for this truck, update in Truck 
            truck.setTravelCost(truckTrip.tripCost());

            delayIfNeeded(truckTrip);


            prepareTruck(truck, received, assignedAmount, 
                         truckTrip.startTime, truckTrip.endTime);

            // Dispatch the truck
            sendTruck(truck, truckTrip, assignedAmount);
            sendBack(truck, truckTrip, customer);
        }
    }



    /**
     * Prepares the truck for trip
     * @param truck truck to be prepared
     * @param order order assigned to the truck
     * @param loadAmount part of total orderedAmount to be loaded to truck
     * @param start start time of trip
     * @param end   end   time of trip
     */
    private void 
    prepareTruck(Truck truck, Order order, int amount, int start, int end) {
            // Greedy Scheduler will only assign one Order per Trucko
            truck.assignOrder(order);
            order.assignTruck(truck, amount); 


            int assignedTime = order.received() + 1;
            Event assign = 
                   new AssignEvent(assignedTime, amount, truck, order);

            Event load = 
                   new TruckLoad(start, amount, truck);

            Event completion = 
                   new OrderSatisfyEvent(end, order, amount);

            Calendar.addEvent(assign);
            Calendar.addEvent(load);
            Calendar.addEvent(completion);
    }



    /**
     * Send a truck loaded with given loadAmount
     * @param truck truck to be dispatched
     * @param trip  send the truck on this trip
     */
    private void sendTruck(Truck truck, Trip trip, int loadAmount) {


        Path p = trip.path;
        int fromTime = trip.dispatchTime;
        int toTime   = fromTime +  p.distanceToNext() * 
                                   MINUTES_IN_HOUR.time() / Truck.SPEED;
        int src      = DEPOT;
        int dst      = p.to();

        // Throw all Path events to Calendar
        while (p.rest() != null) {
            Event advanceByTown = new TruckSend(fromTime, truck, src, dst);
            Calendar.addEvent(advanceByTown);

            p = p.rest();
            src = dst;
            dst = p.to();
            fromTime = toTime;
            toTime = fromTime + p.distanceToNext() * 
                                MINUTES_IN_HOUR.time() / Truck.SPEED;
        }
        Event toLastTown = new TruckSend(fromTime, truck, src, dst);
        Calendar.addEvent(toLastTown);


        // add unload event        
        // +1 to prevent unload/arrived swap in log
        Event unload = new TruckUnload(trip.arrivalTime, loadAmount, truck);
        Calendar.addEvent(unload);
    }


    /**
     * Sends the truck back to DEPOT
     * @param truck truck to be sent back
     * @param trip  trip to be reversed
     * @param customer original trip destination vertex
     */
    private void sendBack(Truck truck, Trip trip, int customer) {
        // Reverse original path
        Path p = Path.reversed(DEPOT, trip.path);

        int fromTime = trip.endTime;
        int toTime   = fromTime + p.distanceToNext() * 
                                  MINUTES_IN_HOUR.time() / Truck.SPEED;
        int src      = customer;
        int dst      = p.to();

        // Throw all Path events to Calendar
        while (p.rest() != null) {
            Event advanceByTown = new TruckSend(fromTime, truck, src, dst);
            Calendar.addEvent(advanceByTown);

            p = p.rest();
            src = dst;
            dst = p.to();
            fromTime = toTime;
            toTime = fromTime + p.distanceToNext() * 
                                MINUTES_IN_HOUR.time() / Truck.SPEED;
        }
        Event toDepot = new TruckSend(fromTime, truck, src, dst);
        Calendar.addEvent(toDepot);

        assert(DEPOT == dst);

        // arrive at DEPOT
        Event returned = new TruckReturn(toTime, truck);
        Calendar.addEvent(returned);
    }

    
    /**
     * Delays the trip such that it arrives in acceptable time
     * @param trip trip to be delayed
     */
    private void delayIfNeeded(Trip trip) {
        int delayTime = 0;
        int arrivalTimeInDay     = trip.arrivalTime % DAY.time();
        int completionTimeInDay  = trip.endTime     % DAY.time();

        // delay if too early
        if (arrivalTimeInDay < MIN_ACCEPT.time())
            delayTime = MIN_ACCEPT.time() - trip.arrivalTime % DAY.time();

        // delay to next day if too late
        if (completionTimeInDay > MAX_ACCEPT.time())
            delayTime = DAY.time()
                        - trip.arrivalTime % DAY.time()
                        + MIN_ACCEPT.time();

        trip.delay(delayTime);
    }
}

import static constant.Times.*;

import graph.Graph;
import graph.Path;
import graph.ShortestPaths;
import graph.Dijkstra;


/**
 * Greedy Scheduler
 *
 * Assigns one Truck to one Order and sends it as soon as possible.
 * Only minimizes distance costs.
 *
 */
public class GreedyScheduler implements Scheduler {
    private final int TERMINATION_TIME;

    private ShortestPaths costMinimizer;

    /**
     * Constructor for Greedy Scheduler
     * @param graph city map of simulation
     * @param depot depot vertex in graph
     * @param terminationTime time of end, trucks won't be sent if they would 
                              arrive after this time
     * @param sp ShortestPaths implementation to be used by this scheduler
     */
    public 
    GreedyScheduler (ShortestPaths sp) {
        TERMINATION_TIME = Simulator.TERMINATION_TIME;
        costMinimizer = sp;
    }

    public
    GreedyScheduler(Graph graph) {
        this(new Dijkstra(graph, Simulator.HOME));
    }


    @Override
    public void
    releaseAll() {
        // done
    }

    @Override
    /**
     * Receives order and immediately dispatches trucks
     * @param received Order to handle
     */
    public void 
    receiveOrder(Order received) {
        assert(received.sentBy().customerVertex() != Simulator.HOME);


        // create Trip that will handle the Order
        // +1 to prevent status events happening before receiving
        int receivedTime   = received.received() + 1;
        int customer       = received.sentBy().customerVertex();
        int amount         = received.amount();
        Path shortestPath  = 
                     costMinimizer.shortestPath(Simulator.HOME, customer);

        // plan with MAX_CAPACITY, that will return latest time of completion
        // Fully loaded trucks then will be sent in parallel
        // but don't just reject the order if orderedAmount was low
        int planAmount = Math.min(Truck.MAX_CAPACITY, amount);
        DeliveryTrip plan = 
              new DeliveryTrip(receivedTime, shortestPath, planAmount);
        delayIfNeeded(plan);


        // reject if completion after simulation time
        if (plan.endTime() >= TERMINATION_TIME) {
            Event reject = new OrderRejectEvent(receivedTime, received);
            Calendar.addEvent(reject);
            received.reject();
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
    private void 
    dispatchTrucks(Trip success, Order received) {
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
            DeliveryTrip deliveryTrip = 
                  new DeliveryTrip(success.startTime(), 
                                   success.path(), assignedAmount);
            delayIfNeeded(deliveryTrip);

            ReturnTrip sendBack = 
                  new ReturnTrip(deliveryTrip.endTime() + 1, 
                               Path.reversed(Simulator.HOME, success.path()));


            prepareTruck(truck, deliveryTrip, received, assignedAmount);

            // Dispatch the truck
            deliveryTrip.sendTruck(truck);
            sendBack.sendTruck(customer, truck);
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
    prepareTruck(Truck truck, DeliveryTrip trip, Order order, int amount) {
            // Greedy Scheduler will only assign one Order per Trucko
            truck.assignOrder(order);
            order.assignTruck(truck, amount); 


            int assignedTime = order.received() + 1;
            Event assign = 
                   new AssignEvent(assignedTime, amount, truck, order);

            Event load = 
                   new TruckLoad(trip.startTime(), amount, truck);

            Event unload = 
                   new TruckUnload(trip.arrivalTime(), amount, truck);

            Event completion = 
                   new OrderSatisfyEvent(trip.endTime(), amount, truck, order);


            Calendar.addEvent(assign);
            Calendar.addEvent(load);
            Calendar.addEvent(unload);
            Calendar.addEvent(completion);
    }


    /**
     * Delays the trip such that it arrives in acceptable time
     * @param trip trip to be delayed
     */
    private void 
    delayIfNeeded(DeliveryTrip trip) {
        int delayTime = 0;
        int arrivalTimeInDay     = trip.arrivalTime() % DAY.time();
        int completionTimeInDay  = trip.endTime()     % DAY.time();
        // arrives next day in morning, then completionTime <= arrivalTime
        boolean arrivesNextDay   = completionTimeInDay <= arrivalTimeInDay;

        // delay if too early
        if (arrivalTimeInDay < MIN_ACCEPT.time())
            delayTime = MIN_ACCEPT.time() - trip.arrivalTime() % DAY.time();

        // delay to next day if too late
        else if (completionTimeInDay > MAX_ACCEPT.time() || arrivesNextDay)
           delayTime = 
              DAY.time() - trip.arrivalTime() % DAY.time() + MIN_ACCEPT.time();

        trip.delay(delayTime);
    }
}

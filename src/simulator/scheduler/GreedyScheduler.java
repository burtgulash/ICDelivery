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
     */
    public GreedyScheduler (Graph graph, int depot, int terminationTime) {
        DEPOT = depot;
        TERMINATION_TIME = terminationTime;
        costMinimizer = new Dijkstra(graph);
    }


    @Override
    /**
     * Receives order and immediately sends result to Dispatcher
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
     */
    private void dispatchTrucks(Trip success, Order received) {
        int customer = received.sentBy().customerVertex();

        // assign new truck while there are containers to be delivered
        int loadAmount = received.amount();
        while (loadAmount > 0) {
            Truck assigned = new Truck();
            // Greedy Scheduler will only assign one Order per Trucko
            assigned.assignOrder(received);

            // assign as many containers as the Truck can take
            int assignedAmount = Math.min(Truck.MAX_CAPACITY, loadAmount);
            loadAmount -= assignedAmount;
            received.assignTruck(assigned, assignedAmount); 

            // assign event
            int assignedTime = received.received() + 1;
            Event assign = new AssignEvent(assignedTime, assignedAmount, 
                                           assigned, received);
            // progress update event
            Calendar.addEvent(assign);
            Event completion = new OrderSatisfyEvent(success.endTime,
                                                     received, assignedAmount);
            Calendar.addEvent(completion);


            // Construct new trip for each truck and delay such that 
            // no waiting is needed
            Trip truckTrip = new Trip(success.startTime, 
                                      success.path, 
                                      assignedAmount);
            delayIfNeeded(truckTrip);

            // Now we know travelCost for this truck, update in Truck 
            assigned.setTravelCost(truckTrip.tripCost());

            // Dispatch the truck
            sendTruck(assigned, truckTrip, assignedAmount);
            sendBack(assigned, truckTrip, customer);
        }
    }

    /**
     * Create and send a truck loaded with given loadAmount
     */
    private void sendTruck(Truck truck, Trip trip, int loadAmount) {
        // add load event
        Event load = new TruckLoad(trip.startTime, loadAmount, truck);
        Calendar.addEvent(load);


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

        // add arrival event
        Event arrived = new TruckArrive(toTime, truck, dst);
        Calendar.addEvent(arrived);
                                                 

        // add unload event        
        // +1 to prevent unload/arrived swap in log
        Event unload = new TruckUnload(trip.arrivalTime, loadAmount, truck);
        Calendar.addEvent(unload);
    }


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
        Event arrived = new TruckArrive(toTime, truck, dst);
        Calendar.addEvent(arrived);
    }

    
    private void delayIfNeeded(Trip trip) {
        // if Trip is planned out of accepting interval, delay it to
        // nearest accepting interval [MIN_ACCEPT, MAX_ACCEPT]
        int delayTime = 0;
        int arrivalTimeInDay     = trip.arrivalTime % DAY.time();
        int completionTimeInDay  = trip.endTime     % DAY.time();

        if (arrivalTimeInDay < MIN_ACCEPT.time())
            delayTime = MIN_ACCEPT.time() - trip.arrivalTime % DAY.time();
        if (completionTimeInDay > MAX_ACCEPT.time())
            delayTime = DAY.time()
                        - trip.arrivalTime % DAY.time()
                        + MIN_ACCEPT.time();

        trip.delay(delayTime);
    }
}

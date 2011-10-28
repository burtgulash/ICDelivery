package simulator;

import stats.Order;
import stats.Truck;

import graph.Graph;
import graph.Path;
import graph.ShortestPaths;
import graph.FloydWarshall;

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

    private final int TRUCK_SPEED = 70; // in km/h

    private ShortestPaths costMinimizer;

    /**
     * Constructor for Greedy Scheduler
     */
    public GreedyScheduler (Graph graph, int depot, int terminationTime) {
        DEPOT = depot;
        TERMINATION_TIME = terminationTime;
        costMinimizer = new FloydWarshall(graph);
    }


    @Override
    /**
     * Receives order and immediately sends result to Dispatcher
     */
    public void receiveOrder(Order received) {
        assert(received.sentBy().customerVertex() != DEPOT);


        // create Trip that will handle the Order
        int receivedTime   = received.received();
        int customer       = received.sentBy().customerVertex();
        int amount  = received.amount();
        Path shortestPath  = costMinimizer.shortestPath(DEPOT, customer);

        Trip plan = new Trip(receivedTime, shortestPath, amount, TRUCK_SPEED);

    
        // if Trip is planned out of accepting interval, delay it to
        // nearest accepting interval [MIN_ACCEPT, MAX_ACCEPT]
        if (plan.arrivesBefore(MIN_ACCEPT.time()) || 
            plan.arrivesAfter(MAX_ACCEPT.time()))
        {
            int delayTime = DAY.time()
                            - plan.endTime % DAY.time()
                            + MIN_ACCEPT.time();
            plan.delay(delayTime);
        }

        // the Trip should be successfully planned now

        if (!plan.arrivesBefore(TERMINATION_TIME))
            ;// reject the Order


        // success, assign a Truck and dispatch
        dispatchTrucks(plan, received);
    }


    /**
     * Dispatch as many truck as needed to satisfy the order
     */
    private void dispatchTrucks(Trip success, Order received) {
        int customer = received.sentBy().customerVertex();

        // assign new Truck while there are containers to be delivered
        int loadAmount = received.toSatisfy();
        Truck assigned = new Truck(received);
        received.satisfy(loadAmount);

        sendTruck(assigned, success, loadAmount);
        sendBack(assigned, success, customer);
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
                                   TRUCK_SPEED / MINUTES_IN_HOUR.time();
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
                                TRUCK_SPEED / MINUTES_IN_HOUR.time();
        }

        // add arrival event
        // src and fromTime are now set as if the truck was leaving destination
        Event arrived = new TruckArrive(fromTime, truck, src);
        Calendar.addEvent(arrived);

        // add unload event        
        // +1 to prevent unload/arrived swap in log
        Event unload = new TruckUnload(fromTime + 1, loadAmount, truck);
        Calendar.addEvent(unload);
    }


    private void sendBack(Truck truck, Trip trip, int customer) {
        // Reverse original path
        Path p = Path.reversed(customer, trip.path);

        int fromTime = trip.endTime;
        int toTime   = fromTime + p.distanceToNext() * 
                                  TRUCK_SPEED / MINUTES_IN_HOUR.time();
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
                                TRUCK_SPEED / MINUTES_IN_HOUR.time();
        }

        assert(DEPOT == src);

        // arrive at DEPOT
        Event arrived = new TruckArrive(fromTime, truck, src);
        Calendar.addEvent(arrived);
    }
}

package simulator;

import stats.Order;
import stats.Truck;

import graph.Graph;
import graph.ShortestPaths;
import graph.FloydWarshall;
import graph.Path;


/**
 * Greedy Scheduler
 *
 * Assigns one Truck to one Order and sends it as soon as possible.
 * Only minimizes distance costs.
 *
 *
 * Only one instance should exist because computing shortestPaths is 
 * expensive.
 */
public class GreedyScheduler implements Scheduler {

    // constants begin
    private final int DEPOT;

    private final int ROUND_UP        = 1;
    private final int MINUTES_IN_HOUR = 60;

    private final int MIN_ACCEPT_TIME = 6  * MINUTES_IN_HOUR;  // 6 hodin rano
    private final int MAX_ACCEPT_TIME = 18 * MINUTES_IN_HOUR; // 18 hodin vecer
    private final int DAY             = 24 * MINUTES_IN_HOUR;

    private final int SPEED           = 70;   // km/h
    private final int LOADING_TIME    = 15;   // minuters
    private final int UNLOADING_TIME  = LOADING_TIME;

    private final int BASE_COST       = 5;    // kc/km
    private final int TRANSPORT_COST  = 1;    // per container
    private final int UNLOAD_COST     = 100;
    private final int WAITING_COST    = 150;
    // constants end


    private ShortestPaths costMinimizer;
    private Calendar cal;
    


    /**
     * Constructor for Greedy Scheduler
     */
    public GreedyScheduler (Graph graph, Calendar cal, int depot) {
        DEPOT = depot;
        this.cal = cal;
        costMinimizer = new FloydWarshall(graph);
    }


    @Override
    /**
     * Receives order and immediately sends result to Dispatcher
     */
    public void receiveOrder(Order received) {
        // find shortest Path to customer
        int customerVertex = received.customer.vertex;
        Path shortestPath  = costMinimizer.shortestPath(DEPOT, customerVertex);
        
        // create plan for received Order
        int receivedTime   = received.received();
        int amount         = received.amount();
        Trip currentPlan   = new Trip(receivedTime, shortestPath, amount);
        
        // delay if needed
        // arrival time in minutes at the day of arrival
        int endTime = currentPlan.arrivalTime % DAY;
        if (endTime < MIN_ACCEPT_TIME)
            currentPlan.delay(MIN_ACCEPT_TIME - endTime);
        else if(endTime > MAX_ACCEPT_TIME)
            currentPlan.delay(DAY + MIN_ACCEPT_TIME - endTime);


        // accept the Order
        dispatch(currentPlan, received);
    }

    /**
     * Takes care of inserting Routing events into calendar
     * and communicating with statistics package
     */
    private void dispatch(Trip successfullyPlanned, Order order) {

        // Initialize dispatch variables
        Trip t = successfullyPlanned;
        Truck truck = new Truck(order, t.path, order.amount());


        // Create loading event
        Event load  = new TruckLoad(t.startTime, t.orderedAmount, truck);

        // Create send event
        Event send  = new TruckSend(t.dispatchTime, t.path, truck);

        // Create return events
        int customerVertex = order.customer.vertex;
        Path shortestBack  = costMinimizer.shortestPath(customerVertex, DEPOT);
        Event goBack = new TruckSend(t.endTime, shortestBack, truck);


        // Send them to Calendar
        cal.addEvent(load);
        cal.addEvent(send);
        cal.addEvent(goBack);
        
        // BIG TODO update statistics

        // TODO update Truck List
        // TODO update Orders List
        // TODO ??? update Customers List ???
    }


    @Override
    /**
     * Greedy scheduler does not implement this method as it never
     * holds any Trucks, they are immediately dispatched.
     */
    public void forceDispatch() {
    }





    /**
     * Private class for internal purposes of Scheduler
     * 
     * Stores cost and time of the trip to be assigned to Truck
     */
    private class Trip {
        Path path;
        int orderedAmount;
        int totalCost;

        int endTime;
        int startTime;
        int dispatchTime;
        int arrivalTime;



        /**
         * Constructs temporary storage Trip given Path to customer
         */
        private Trip (int receivedTime, Path toDestination, int orderedAmount) {
            // dispatch as soon as possible

            path = toDestination;
            int tripLength = path.pathLength();

            startTime = receivedTime;
            dispatchTime = startTime + LOADING_TIME   * orderedAmount;
            arrivalTime = startTime  + tripLength     * MINUTES_IN_HOUR/SPEED
                                     + ROUND_UP;

            endTime = arrivalTime    + UNLOADING_TIME * orderedAmount;
            

            // unnecessary to compute
            totalCost   = BASE_COST      * tripLength    + 
                          TRANSPORT_COST * tripLength    * orderedAmount +
                          UNLOAD_COST    * orderedAmount + 
                          WAITING_COST   * 0;
        }


        /**
         * Delay the trip by delay time
         */
        private void delay(int delayTime) {
            startTime     += delayTime;
            dispatchTime  += delayTime;
            arrivalTime   += delayTime;
            endTime       += delayTime;
        }
    }
}

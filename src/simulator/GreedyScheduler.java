package truckDepot;

import stats.Order;
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

    private final int MIN_TIME		  = 360;  // 6 hodin rano
    private final int MAX_TIME 		  = 1080; // 18 hodin vecer
    private final int DAY			  = 1440;
    private final int SPEED           = 70;   // km/h
    private final int LOADING_TIME    = 15;   // minuters

    private final int BASE_COST       = 5;    // kc/km
    private final int TRANSPORT_COST  = 1;    // per container
    private final int UNLOAD_COST     = 100;
    private final int WAITING_COST    = 150;

	private final int ROUND_UP        = 1;
	private final int MINUTES_IN_HOUR = 60;
    // constants end

    private ShortestPaths costMinimizer;
    


    /**
     * Constructor for Greedy Scheduler
     */
    public GreedyScheduler (Graph graph, int depot) {
        DEPOT = depot;
        costMinimizer = new FloydWarshall(graph);
    }


    @Override
    /**
     * Receives order and immediately sends result to Dispatcher
     */
    public void receiveOrder(Order received) {
        // find shortest Path to customer
        int customerVertex  = received.customer.vertex;
        Path shortest = costMinimizer.shortestPath(DEPOT, customerVertex);
        
        // create plan for received Order
        int receivedTime = received.received();
        int amount       = received.amount();
        Trip currentPlan = new Trip(receivedTime, shortest, amount);
        
        // delay if needed
        int arrivalTime = currentPlan.arrivalTime()%DAY; // arrival time in minutes at the day of arrival
        if ((arrivalTime) < MIN_TIME) {
            currentPlan.delay(MIN_TIME-arrivalTime);
        }
        else if((arrivalTime) > MAX_TIME){
        	currentPlan.delay(DAY+MIN_TIME-arrivalTime);
        }
        
    }

    /**
     * Takes care of inserting Routing events into calendar
     * and communicating with statistics package
     */
    private void dispatch(Trip successfullyPlanned) {
        // Create Truck events

        // Send them to Calendar

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
        Path trip;
        int totalCost;
        int arrivalTime;
        int dispatchTime;

        /**
         * Constructs temporary storage Trip given Path to customer
         */
        private Trip (int receivedTime, Path toDestination, int orderedAmount) {
            // dispatch as soon as possible
            dispatchTime   = receivedTime;
            arrivalTime    = dispatchTime;

            this.trip = toDestination;
            int tripLength = trip.pathLength();


            // travel time + load + unload
            arrivalTime = tripLength * MINUTES_IN_HOUR/SPEED + ROUND_UP +
                          LOADING_TIME * orderedAmount + 
                          LOADING_TIME * orderedAmount; 

            totalCost   = BASE_COST * tripLength + 
                          TRANSPORT_COST * tripLength * orderedAmount +
                          UNLOAD_COST * orderedAmount + 
                          WAITING_COST * 0;
        }


        /**
         * Delay the trip by delay time
         */
        private void delay(int delay) {
            dispatchTime += delay;
            arrivalTime  += delay;
        }

        /**
         * Selector for arrival time
         */
        private int arrivalTime () {
            return arrivalTime;
        }
    }
}

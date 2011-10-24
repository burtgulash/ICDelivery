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

	private final int DEPOT;
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
		int customerVertex  = received.customer.vertex;
		Path shortest = costMinimizer.shortestPath(DEPOT, customerVertex);
		
		// TODO delay if needed
		// TODO send to Dispatcher
	}


	@Override
	/**
	 * Greedy scheduler does not implement this method as it never
	 * holds any Trucks, they are immediately dispatched.
	 */
	public void forceDispatch() {
	}
}

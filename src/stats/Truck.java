package stats;

import stats.Order;
import graph.Path;

// import time constants
import simulator.Times.*;


public class Truck {
	private Order assignedOrder;

	// remaining Path to destination for this truck
	private Path currentPath;
	private int cargo;

	public Truck(Order assigned, Path path, int cargo) {
		assignedOrder = assigned;
		currentPath = path;
		this.cargo = cargo;
	}

	public int timeToNextTown () {
		int length = currentPath.pathLength() - currentPath.rest().pathLength();

		// change to constants
		return length * 60 / 70 + 1;
	}

	/**
	 * Check if the truck has arrived to destination
	 */
	public boolean arrived() {
		return currentPath.rest() == null;
	}

	/**
	 * Send truck to next town
	 */
	public Path advance() {
		currentPath = currentPath.rest();
		return currentPath;
	}
}

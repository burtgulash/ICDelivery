package stats;

import stats.Order;
import graph.Path;

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
}

package stats;

import stats.Order;
import graph.Path;

public class Truck {
	Order assignedOrder;

	// remaining Path to destination for this truck
	Path currentPath;

	// number of containers in the Truck
	int cargo;
}

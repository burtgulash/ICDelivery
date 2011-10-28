package stats;

/**
 * Customer object is needed to keep track of statistics
 */
public class Customer {
	private final int vertex;
	// TODO attribute orderHIstory : LIST

	public Customer (int customerId) {
		// must match with graph vertex
		vertex = customerId;
	}

	public int customerId() {
		return vertex;
	}

	public int customerVertex() {
		return vertex;
	}
}

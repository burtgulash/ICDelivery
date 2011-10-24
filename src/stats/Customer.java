package stats;

/**
 * Customer object is needed to keep track of statistics
 */
public class Customer {
	public final int vertex;
	// TODO attribute orderHIstory : LIST

	public Customer (int customerId) {
		// must match with graph vertex
		vertex = customerId;
	}
}

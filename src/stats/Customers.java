package stats;


/**
 * Customers class to keep list of all Customers
 */
public class Customers {
	private Customer[] list;
	
	public Customers(int numCustomers) {
		list = new Customer[numCustomers];

		for (int i = 0; i < numCustomers; i++)
			list[i] = new Customer(i);
	}

	public Customer getCustomer(int customerId) {
		assert(0 <= customerId && customerId < list.length);
		return list[customerId];
	}
	
	public int size(){
		return list.length;
	}
}

package stats;


/**
 * Customers class to keep list of all Customers
 */
public static class CustomerList {
	private Customer[] list;

	
	/**
	 * static constructor
	 */ 
	public static init (int numCustomers) {
		list = new Customer[numCustomers];

		for (int i = 0; i < numCustomers; i++)
			list[i] = new Customer(i);
	}

	/**
	 * Finds customer by id/customerVertex
	 */
	public static Customer get(int customerId) {
		assert(0 <= customerId && customerId < list.length);
		assert(list[customerId] != null);
		return list[customerId];
	}
	
	/**
	 * Returns number of customers
	 * @return number of customers
	 */
	public static int numCustomers() {
		assert(list != null);
		return list.length;
	}
}

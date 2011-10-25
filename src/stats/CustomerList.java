package stats;


/**
 * Customers class to keep list of all Customers
 */
public class CustomerList {
	private static CustomerList onlyRef;

	// list of all customers
	private Customer[] list;

	/**
	 * Constructor for singleton class CustomerList
	 * @param numCustomers number of customers to be kept in list
	 */
	public static CustomerList getCustomerListObject(int numCustomers) {
		if (onlyRef == null)
			onlyRef = new CustomerList(numCustomers);
		return onlyRef;
	}
	
	// singleton constructor
	private CustomerList (int numCustomers) {
		list = new Customer[numCustomers];

		for (int i = 0; i < numCustomers; i++)
			list[i] = new Customer(i);
	}

	/**
	 * Finds customer by id/customerVertex
	 */
	public Customer getCustomer(int customerId) {
		assert(0 <= customerId && customerId < list.length);
		return list[customerId];
	}
	
	/**
	 * Returns number of customers
	 * @return number of customers
	 */
	public int size() {
		return list.length;
	}
}

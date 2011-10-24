package stats;


/**
 * Class Order
 *
 * Used by scheduler and statistics keeper
 */
public class Order {
	private final Customers customers;
	public final Customer customer;
	public int amount;
	public boolean accepted;
	public boolean served;
	public int servedBy;

	public Order(Customers customerList, int customerNum, int amount) {
		this.customers = customerList;
		this.amount = amount;
		customer = customers.getCustomer(customerNum);
	}
}

package stats;


/**
 * Class Order
 *
 * Used by scheduler and statistics keeper
 */
public class Order {
    private Trip assigned; // null if Order declined
    private final Customer customer; // creator of Order
    
    private int amount;   // num of containers
    private int servedBy; // id of truck
    
    private final int receivedTime;

    // for Order tracking purposes
    private static int orderCount = 0;
    private int orderId;


    /**
     * Constructs Order object 
     *
     * time of construction != receivedTime, receivedTime means when it 
     * first appears in time
     *
     * @param receivedTime time, when Simulator first sees the Order
     * @param customerNum  id of Customer that generated this Order
     * @param amount       number of containers to deliver
     */
    public Order(int receivedTime, int customerNum, int amount) {
        orderId = ++orderCount;

        this.receivedTime = receivedTime;
        this.amount = amount;
        customer = CustomerList.get(customerNum);
    }

    /**
     * Return time the Order was first seen
     */
    public int received() {
        return receivedTime;
    }

	/**
	 * Return customer that sent this Order
	 */
	public Customer sentBy() {
		return customer;
	}


    /**
     * Return ordered amount of containers
     */
    public int amount() {
        return amount;
    }
    
    /**
     * Returns id of this Order
     */
    public int getId(){
        return orderId;
    }
}

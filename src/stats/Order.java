package stats;


/**
 * Class Order
 *
 * Used by scheduler and statistics keeper
 */
public class Order {
    private final int receivedTime;
    private final CustomerList customers;
    public final Customer customer;
    private int amount;
    public boolean accepted;
    public boolean served;
    public int servedBy;

    public Order(CustomerList customerList, 
                 int customerNum,
                 int amount,
                 int receivedTime) {

        this.receivedTime = receivedTime;
        this.customers = customerList;
        this.amount = amount;
        customer = customers.getCustomer(customerNum);
    }

    /**
     * Return time the Order was first seen
     */
    public int received() {
        return receivedTime;
    }

    /**
      * Return ordered amount of containers
     */
    public int amount() {
        return amount;
    }
}

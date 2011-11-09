import java.util.LinkedList;
import java.util.List;

/**
 * Class Order
 *
 * Used by scheduler and statistics keeper
 */
public class Order {
    private List<Truck> trucks;      // trucks that serve this order
    private final Customer customer; // creator of Order

    private int orderedAmount;   // num of containers
	private int delivered;
    private int processed;

    private final int receivedTime;

    // for Order tracking purposes
    private static int orderCount = 0;
    private int orderId;

    // Every order is marked as accepted until we reject it
    private boolean accepted = true;


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

        this.receivedTime    = receivedTime;
        orderedAmount        = amount;
		delivered            = 0;
        processed            = 0;
        customer             = CustomerList.get(customerNum);
        trucks               = new LinkedList<Truck>();
    }


    /**
     * Assign truck to this order
     * @param truck Truck that got assigned to this order
     * @param assignedAmount containers, that will be delivered by the truck
     */
    public void assignTruck(Truck truck, int assignedAmount) {
        trucks.add(truck);
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
        return orderedAmount;
    }


    /**
     * Return amount of containers delivered at the end of Simulation
     */
    public int processed() {
        return processed;
    }


	public void process(int containers) {
		processed += containers;
        assert(processed <= orderedAmount);
	}

    public int remains() {
        return orderedAmount - processed;
    }

    /**
     * satisfies order by specified number of containers
     */
    public void satisfy(int containers) {
		delivered += containers;
        assert(delivered <= orderedAmount);
    }

    /**
     * Returns id of this Order
     */
    public int getId(){
        return orderId;
    }


    public void reject() {
        accepted = false;
    }

    public boolean accepted() {
        return accepted;
    }
}

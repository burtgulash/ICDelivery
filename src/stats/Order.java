import java.util.LinkedList;
import java.util.List;

/**
 * Order object abstraction.
 * 
 * Keeps track of trucks that serve this order, 
 * customer who sent this order, 
 * Amount of ordered tons, delivered tons, ...
 */
public class Order {
    private List<Truck> trucks;      // trucks that serve this order
    private final Customer customer; // creator of Order

    private int orderedAmount;   // num of containers
    private int delivered = 0;
    private int processed = 0;

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
     * first appears in simulation time
     *
     * @param receivedTime time, when Simulator first sees the Order
     * @param customerNum  id of Customer that generated this Order
     * @param amount       number of containers to deliver
     */
    public Order(int receivedTime, int customerNum, int amount) {
        orderId = orderCount++;

        this.receivedTime    = receivedTime;
        orderedAmount        = amount;
        customer             = CustomerList.get(customerNum);
        trucks               = new LinkedList<Truck>();
    }


    /**
     * Assign truck to this order.
     *
     * @param truck Truck that got assigned to this order
     * @param assignedAmount containers, that will be delivered by the truck
     */
    public void assignTruck(Truck truck, int assignedAmount) {
        trucks.add(truck);
    }


    /**
     * Get list of all trucks that serve this order.
     *
     * @return List of trucks serving this order.
     */
    public List<Truck> assignedTrucks() {
        return trucks;
    }


    /**
     * Return time the order was received.
      *
     * @return received time.
     */
    public int received() {
        return receivedTime;
    }


    /**
     * Return customer that sent this Order.
     *
     * @return customer responsible for this order.
     */
    public Customer sentBy() {
        return customer;
    }

	public static void destroyLast() {
		orderCount--;
	}

    /**
     * Return amount of ordered containers in this order.
     *
     * @return Amount of ordered containers.
     */
    public int amount() {
        return orderedAmount;
    }


    /**
     * Return amount of containers delivered by the end of simulation.
     * Amount of containers processed by schedulers and planned
     * to be successfully delivered by the end of simulation.
     *
     */
    public int processed() {
        return processed;
    }


    /**
     * Scheduler notifies this order that it will successfully deliver
     * specified amount of containers.
     *
     * @param containers Containers that will be delivered.
     */
    public void process(int containers) {
        processed += containers;
        assert(processed <= orderedAmount);
    }


    /**
     * Get number of containers yet to be assigned to a truck
     * by a scheduler.
     *
     * @return Amount that remains to be processed by a scheduler.
     */
    public int remains() {
        return orderedAmount - processed;
    }


    /**
     * Successfully deliver specified amount of containers.
     *
     * @param containers Deliver this many containers.
     */
    public void satisfy(int containers) {
        delivered += containers;
        assert(delivered <= orderedAmount);
    }


    /**
     * Get number of containers delivered so far.
     *
     * @return Number of delivered containers.
     */
    public int delivered() {
        return delivered;
    }


    /**
     * Returns id of this Order
     *
     * @return Order id.
     */
    public int getId(){
        return orderId;
    }


    /**
     * Mark this order as rejected.
     * Can only happen if it was received too late.
     */
    public void reject() {
        accepted = false;
    }


    /**
     * Get status of this order.
     *
     * @return true if it was accepted or false if rejected.
     */
    public boolean accepted() {
        return accepted;
    }
}

import java.util.List;
import java.util.LinkedList;



/**
 * Customer object
 *
 * Stores information about graph location and sent orders.
 */
public class Customer {
    private final int vertex;
    private List<Order> orderHistory;
    private int orderToSatisfy = 0;

    /**
     * Constructs Customer abstraction with given id or graph vertex.
     *
     * @param customerId graph vertex or id of this customer
     */
    public Customer (int customerId) {
        // must match with graph vertex
        vertex = customerId;
        orderHistory = new LinkedList<Order>();
    }


    /**
     * Get all orders that came from this customer.
     *
     * @return All orders sent by this customer.
     */
    public List<Order> sentOrders() {
        return orderHistory;
    }


    /**
     * Add generated order to order history.
     *
     * @param order new order to be inserted
     */
    public void addOrder(Order order) {
        orderHistory.add(order);
    }


    /**
     * Get number of total orders this customer sent.
     *
     * @return Number of orders by this customer.
     */
    public int totalOrders() {
        return orderHistory.size();
    }


    /**
     * Get number of accepted/satisfied orders for this customer.
     *
     * @return Number of accepted orders.
     */
    public int acceptedOrders() {
        int tot = 0;
        for (Order o : orderHistory)
            if(o.accepted())
                tot++;
        return tot;
    }


    /**
     * Get number of containers that actually were delivered.
     *
     * @return Number of delivered containers for this customer.
     */
    public int deliveredContainers() {
        int tot = 0;
        for (Order o : orderHistory)
            tot += o.delivered();
        return tot;
    }


    /**
     * Get number of all containers sent by this customer.
     *
     * @return Number of all containers by this customer.
     */
    public int totalContainers() {
        int tot = 0;
        for (Order o : orderHistory)
            tot += o.amount();
        return tot;
    }


    /**
     * Get id of this customer.
     *
     * @return Id of customer.
     */
    public int getId() {
        return vertex;
    }


    /**
     * Get position of this customer in graph.
     *
     * @return Vertex number of this customer.
     */
    public int customerVertex() {
        return vertex;
    }
}

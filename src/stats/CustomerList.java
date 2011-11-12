/**
 * Customers class to keep list of all Customers
 */
public class CustomerList {
    private static Customer[] list;

    private boolean initialized = false;


    /**
     * Static constructor, must be called before using this class.
     *
     * @param numCustomers creates list of this size
     */ 
    public static void init (int numCustomers) {
        list = new Customer[numCustomers];

        for (int i = 0; i < numCustomers; i++)
            list[i] = new Customer(i);
    }


    /**
     * Retrieves customer by id/customerVertex.
     *
     * @param id Id of customer to get.
     */
    public static Customer get(int id) {
        if (id < 0 || id >= size())
            return null;
        assert(list[id] != null);
        return list[id];
    }


    /**
     * Returns number of customers.
     *
     * @return Number of customers.
     */
    public static int size() {
        assert(list != null);
        return list.length;
    }


    /**
     * Get number of all accepted orders.
     *
     * @return Number of all accepted orders.
     */
    public static int acceptedOrders() {
        int tot = 0;
        for (Customer c : list)
            tot += c.acceptedOrders();
        return tot;
    }


    /**    
       * Get number of all orders that appeared in simulation.
     *
     * @return Number of all orders.
     */
    public static int totalOrders() {
        int tot = 0;
        for (Customer c : list)
            tot += c.totalOrders();
        return tot;
    }


    /**
     * Get number of all containers that were delivered.
     *
     * @return Number of all delivered containers.
     */
    public static int deliveredContainers() {
        int tot = 0;
        for (Customer c : list)
            tot += c.deliveredContainers();
        return tot;
    }


    /**
     * Get number of all containers that appeared in simulation.
     *
     * @return Number of all containers.
     */
    public static int totalContainers() {
        int tot = 0;
        for (Customer c : list)
            tot += c.totalContainers();
        return tot;
    }
}

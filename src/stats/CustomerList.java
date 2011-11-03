/**
 * Customers class to keep list of all Customers
 */
public class CustomerList {
    private static Customer[] list;


    /**
     * Static constructor
     *
     * @param numCustomers creates list of this size
     */ 
    public static void init (int numCustomers) {
        list = new Customer[numCustomers];

        for (int i = 0; i < numCustomers; i++)
            list[i] = new Customer(i);
    }

    /**
     * Finds customer by id/customerVertex
     */
    public static Customer get(int customerId) {
        assert(0 <= customerId && customerId < numCustomers());
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

    private static int acceptedOrders() {
        int tot = 0;
        for (Customer c : list)
            tot += c.acceptedOrders();
        return tot;
    }


    private static int totalOrders() {
        int tot = 0;
        for (Customer c : list)
            tot += c.totalOrders();
        return tot;
    }

    private static int totalContainers() {
        int tot = 0;
        for (Customer c : list)
            tot += c.totalContainers();
        return tot;
    }

    public static void summary() {
        System.out.printf("Orders accepted    : %d/%d%n", 
                                    acceptedOrders(), totalOrders());
        System.out.printf("Containers ordered : %d%n", totalContainers());
    }
}

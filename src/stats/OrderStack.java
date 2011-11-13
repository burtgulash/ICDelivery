import java.util.Map;
import java.util.TreeMap;


/**
 * Keeps track of all orders that were received.
 */
class OrderStack {
    private static Map<Integer, Order> allOrders;
    private static boolean initialized = false;


    // disable ordinary constructor
    private OrderStack() {/*,*/}


    /**
     * Initializes this static object. Must be called before
     * using this class.
     */
    private static void init() {
        initialized = true;
        allOrders = new TreeMap<Integer, Order>();
    }


    /**
     * Push received order to the stack.
     *
     * @param order received order.
     */
    static void add(Order order) {
        if (!initialized)
            init();

        allOrders.put(order.getId(), order);
    }


    /**
     * Get order by its id.
     *
     * @param orderId id of order to get
     * @return Order with specified id or null if it does not exist.
     */
    static Order get(int orderId) {
        if (!initialized)
            init();

        Order ret =  allOrders.get(orderId);
        assert ret != null;
        return ret;
    }


    /**
     * Get number of all orders.
     *
     * @return Number of all orders.
     */
    static int size() {
        if (allOrders == null)
            return 0;

        return allOrders.size();
    }
}

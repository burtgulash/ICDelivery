import java.util.Map;
import java.util.TreeMap;


public class OrderStack {
    private static Map<Integer, Order> allOrders;

    private OrderStack() {/*,*/}

    public static void init() {
        allOrders = new TreeMap<Integer, Order>();
    }


    public static void add(Order order) {
        allOrders.put(order.getId(), order);
    }

    public static Order get(int orderId) {
        Order ret =  allOrders.get(orderId);
        assert(ret != null);
        return ret;
    }

    public static int size() {
        return allOrders.size();
    }
}

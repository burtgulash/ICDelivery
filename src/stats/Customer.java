import java.util.List;
import java.util.LinkedList;


/**
 * Customer object is needed to keep track of statistics
 */
public class Customer {
    private final int vertex;
    private List<Order> orderHistory;
	private int orderToSatisfy = 0;

    public Customer (int customerId) {
        // must match with graph vertex
        vertex = customerId;
        orderHistory = new LinkedList<Order>();
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
    }

	public void satisfy(int containers) {
		while (containers > 0) {
			Order curOrder = orderHistory.get(orderToSatisfy);
			int curSatisfy = Math.min(curOrder.remains(), containers);
			curOrder.satisfy(curSatisfy);
			// if current order done:
			if (curOrder.remains() == 0)
				orderToSatisfy++;

			containers -= curSatisfy;
		}
	}

    public int totalOrders() {
        return orderHistory.size();
    }

    public int acceptedOrders() {
        int tot = 0;
        for (Order o : orderHistory)
            if(o.accepted())
                tot++;
        return tot;
    }

    public int deliveredContainers() {
        int tot = 0;
        for (Order o : orderHistory)
            if (o.accepted())
                tot += o.amount();
        return tot;
    }

    public int totalContainers() {
        int tot = 0;
        for (Order o : orderHistory)
            tot += o.amount();
        return tot;
    }

    public int customerId() {
        return vertex;
    }

    public int customerVertex() {
        return vertex;
    }
}

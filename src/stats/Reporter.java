import java.io.OutputStream;
import java.io.PrintWriter;


public class Reporter {
    public void printOrderReport(OutputStream out) {
        PrintWriter p = new PrintWriter(out);

        int numOrders = OrderStack.size();
        for (int i = 0; i < numOrders; i++) {
            Order order = OrderStack.get(i);
            assert order != null;

            // TODO
        }
    }


    public static String orderReport (int id) {
        Order order = OrderStack.get(id);
        StringBuilder s = new StringBuilder();

        if (order == null)
            s.append(String.format("Order %5d does not exist%n", id));
        else {
            s.append(String.format("Order %5d:%n", id));
            s.append(String.format("received at %s%n", 
                              TimeConverter.ascTime(order.received())));

            if (order.accepted()) {
                s.append(String.format("Accepted%n"));
                s.append(String.format("Delivered containers: %d%n", 
                                          order.delivered()));
                s.append(String.format("Served by:%n"));
                for (Truck t : order.assignedTrucks())
                    s.append(String.format("\tTruck %5d%n", t.getId()));
            } else 
                s.append(String.format("Rejected%n"));
        }

        return s.toString();
    }


    public static String customerReport(int id) {
        Customer customer = CustomerList.get(id);
        StringBuilder s = new StringBuilder();

        if (customer == null)
            s.append(String.format("Customer %5d does not exist%n", id));
        else if (id == Simulator.HOME)
            s.append(String.format("Town %5d is HOME vertex%n", id));

        else {
            s.append(String.format("Customer %5d:%n", id));
            s.append(String.format("Ordered containers  : %d%n", 
                                            customer.totalContainers()));
            s.append(String.format("Orders from this customer:%n"));
            for (Order o : customer.sentOrders()) {
                String status = o.accepted() ? "Accepted" : "Rejected";
                s.append(String.format(
                         "\tOrder %5d for %2d tons, received at %s: %s%n",
                           o.getId(), o.amount(), 
                           TimeConverter.ascTime(o.received()), status));
            }
        }

        return s.toString();
    }
}

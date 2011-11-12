import java.io.OutputStream;
import java.io.PrintWriter;

import java.text.DecimalFormat;


public class Reporter {
    private static final String NL = String.format("%n");
    private static DecimalFormat d = new DecimalFormat(",###");


    public static void printOrderReport(OutputStream out) {
        PrintWriter p = new PrintWriter(out);
        p.println(orderSummary());

        int numOrders = OrderStack.size();
        for (int id = 0; id < numOrders; id++) {
            p.println(orderReport(id));
            p.println();
        }
    }


    public static String orderSummary() {
        StringBuilder s       = new StringBuilder();

        String totalOrders    = d.format(CustomerList.totalOrders());
        String acceptedOrders = d.format(CustomerList.acceptedOrders());

        s.append(String.format("Total    orders: %s", totalOrders));
        s.append(NL);
        s.append(String.format("Accepted orders: %s", acceptedOrders));
        s.append(NL);

        return s.toString();
    }


    public static String truckSummary() {
        StringBuilder s = new StringBuilder();

        String totalCost = d.format(TruckStack.totalCost());
        s.append(String.format("TOTAL COST           : %s CZK", totalCost));
        s.append(NL);
        s.append(String.format("Trucks dispatched    : %4d", 
                                                          TruckStack.size()));
        s.append(NL);

        return s.toString();
    }


    public static String customerSummary() {
        StringBuilder s = new StringBuilder();

        s.append(String.format("Orders accepted      : %4d/%4d%n", 
                CustomerList.acceptedOrders(), CustomerList.totalOrders()));
        s.append(String.format("Containers delivered : %4d/%4d%n", 
                         CustomerList.deliveredContainers(), 
                                  CustomerList.totalContainers()));

        return s.toString();
    }


    public static void printCustomerReport(OutputStream out) {
        PrintWriter p = new PrintWriter(out);

        // TODO rename numCustomers to size
        int numCustomers = CustomerList.numCustomers();
        for (int id = 0; id < numCustomers; id++) {
            p.println(customerReport(id));
            p.println();
        }
    }


    public static void printTruckReport(OutputStream out) {
        PrintWriter p = new PrintWriter(out);

        int numTrucks = TruckStack.size();
        for (int id = 0; id < numTrucks; id++) {
            p.println(truckFullReport(id));
            p.println();
        }
    }


    public static String truckReport (int id) {
        Truck truck = TruckStack.get(id);
        StringBuilder s = new StringBuilder();

        if (truck == null)
            s.append(String.format("Truck %5d does not exist%n", id));
        else {
            s.append(String.format("Truck %5d:%n", id));
            s.append(String.format("is near town %d%n", truck.currentTown()));
            s.append(String.format("Carries %d containers%n", truck.loaded()));
            s.append("for orders:");
            s.append(NL);

            for (Order o : truck.assignedOrders())
                s.append(String.format(
                             "\tOrder %5d from customer %5d, received at %s%n", 
                                     o.getId(), o.sentBy().customerId(), 
                                     TimeConverter.ascTime(o.received())));
        }

        return s.toString();
    }


    public static String truckFullReport (int id) {
        Truck truck = TruckStack.get(id);
        StringBuilder s = new StringBuilder(truckReport(id));

        if (truck == null)
            return s.toString();

        s.append(NL);
        for (TruckEvent e : truck.actions) {
            s.append(TimeConverter.ascTime(e.time()));
            s.append(" | ");
            s.append(e.report());
            s.append(NL);
        }

        return s.toString();
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
                s.append("Accepted");
                s.append(NL);
                s.append(String.format("Delivered containers: %d%n", 
                                          order.delivered()));
                s.append(String.format("Served by:%n"));
                for (Truck t : order.assignedTrucks())
                    s.append(String.format("\tTruck %5d%n", t.getId()));
            } else  {
                s.append("Rejected");
                s.append(NL);
            }
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
            s.append("Orders from this customer:");
            s.append(NL);
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

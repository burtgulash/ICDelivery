import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.StringTokenizer;



class PauseEvent extends Event {
    private static BufferedReader keybr;

    PauseEvent(int time){
        super(time);
        if (keybr == null)
            keybr = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    protected int doWork() {
        printSummary();
        printHelp();

        while (true) {
            System.out.print(">>> ");
            String query = "";
            try {
                query = keybr.readLine();
            } catch (IOException ex) {
                System.err.println("Error reading query");
            }

            if (query == null || query.trim().equals(""))
                break;

            StringTokenizer tk = new StringTokenizer(query);
            if (!tk.hasMoreTokens()) {
                System.err.println("Missing option");
                continue;
            }

            String option = tk.nextToken();
            if (option.matches("(?:H|h)(?:elp)?")) {
                printHelp();
                continue;
            }


            if (!tk.hasMoreTokens()) {
                System.err.println("Missing value");
                continue;
            }


            int id = 0;
            try {
                id = Integer.parseInt(tk.nextToken());
            } catch (NumberFormatException ex) {
                System.err.println("ID must be number");
                continue;
            }

            if (option.matches("(?:T|t)(?:ruck)?")) {
                // TODO id must exist
                Truck truck = TruckStack.get(id);
                assert(truck != null);

                System.out.println();
                System.out.printf("Truck %5d:%n", id);
                System.out.printf("Is near town %d%n", truck.currentTown());
                System.out.printf("Carries %d containers%n", truck.loaded());
                System.out.println("for orders:");

                for (Order o : truck.assignedOrders())
                    System.out.printf("\tOrder %5d from customer %5d%n", 
                                      o.getId(), o.sentBy().customerId());
            }
            else if (option.matches("(?:O|o)(?:rder)?")) {
                // TODO id must exist
                Order order = OrderStack.get(id);
                assert(order != null);

                System.out.println();
                System.out.printf("Order %5d:%n", id);
                System.out.printf("received at %s%n", 
                                  Calendar.ascTime(order.received()));

                if (order.accepted()) {
                    System.out.println("Accepted");
                    System.out.printf("Delivered containers: %d%n", 
                                      order.delivered());
                    System.out.println("\tServed by:");

                    for (Truck t : order.assignedTrucks())
                        System.out.printf("Truck %5d%n", t.getId());
                } else 
                    System.out.println("Rejected");
            }
            else if (option.matches("(?:U|c)(?:ustomer)?")) {
                // TODO id must exist
                Customer customer = CustomerList.get(id);

                System.out.println();
                System.out.printf("Customer %5d:%n", id);
                System.out.printf("Ordered containers   : %d%n", 
                                            customer.totalContainers());
                System.out.printf("Delivered containers : %d%n", 
                                            customer.deliveredContainers());
                System.out.println("Orders from this customer:");
                for (Order o : customer.sentOrders()) {
                    String status = o.accepted() ? "Accepted" : "Rejected";
                    System.out.printf("\tOrder %5d for %2d tons: %s%n", 
                                   o.getId(), o.amount(), status);
                }

            }
        }

        return Simulator.CONTINUE;
    }

    @Override
    protected String log () {
        return "Simulation paused";
    }

    private void printSummary() {
        System.out.printf("%nPaused at %s%n", Calendar.ascTime(time()));
        TruckStack.summary();
        CustomerList.summary();
    }

    private void printHelp() {
        System.out.println();
        System.out.println("usage:");
        System.out.printf("\t[t]ruck    <%d-%d>%n", 1, TruckStack.size());
        System.out.printf("\t[o]rder    <%d-%d>%n", 1, OrderStack.size());
        System.out.printf("\t[c]ustomer <%d-%d>%n", 0,  
                                             CustomerList.numCustomers());
        System.out.println("\t[h]elp");
    }
}

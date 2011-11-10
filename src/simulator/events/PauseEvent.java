import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.StringTokenizer;



class PauseEvent extends Event {
    private static BufferedReader keybr;
    private static String HELP = String.format("%s%n%s%n",
"[T]ruck ID", 
"[O]rder ID");

    PauseEvent(int time){
        super(time);
        if (keybr == null)
            keybr = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    protected int doWork() {
        printSummary();

        while (true) {
            printHelp();
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
            if (!tk.hasMoreTokens()) {
                System.err.println("Missing value");
                continue;
            }


            if (option.matches("(?:T|t)(?:ruck)?")) {
                int id = 0;
                try {
                    id = Integer.parseInt(tk.nextToken());
                } catch (NumberFormatException ex) {
                    System.err.println("ID must be number");
                    continue;
                }
                // TODO id must exist
                Truck truck = TruckStack.get(id);

                System.out.printf("Truck %5d:%n", id);
                System.out.printf("Is near town %d%n", truck.currentTown());
                System.out.printf("Carries %d containers%n", truck.loaded());
                System.out.println("for orders:");

                for (Order o : truck.assignedOrders())
                    System.out.printf("\tOrder %5d from customer %5d%n", 
                                      o.getId(), o.sentBy().customerId());
            }
            else if (option.matches("(?:O|o)(?:rder)?")) {
                int id = 0;
                try {
                    id = Integer.parseInt(tk.nextToken());
                } catch (NumberFormatException ex) {
                    System.err.println("ID must be number");
                    continue;
                }

                Order order = OrderStack.get(id);

                System.out.printf("Order %5d:%n", id);
                System.out.printf("received at %s%n", 
                                  Calendar.ascTime(order.received()));

                if (order.accepted()) {
                    System.out.println("Accepted");
                    System.out.printf("Delivered containers: %d%n", 
                                      order.delivered());
                    System.out.println("Served by:");

                    for (Truck t : order.assignedTrucks())
                        System.out.printf("Truck %5d%n", t.getId());
                } else 
                    System.out.println("Rejected");
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
        System.out.println();
    }

    private void printHelp() {
        System.out.println("usage:");
        System.out.print(HELP);
    }
}

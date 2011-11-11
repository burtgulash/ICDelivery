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
            System.err.print(">>> ");
            String query = "";
            try {
                query = keybr.readLine();
            } catch (IOException ex) {
                System.err.println("Error reading query");
            }

            // continue simulation
            if (query == null || query.matches("(?:Q|q)(?:uit)?") || 
                query.trim().equals(""))
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

            // Options with value follow
            if (!tk.hasMoreTokens()) {
                System.err.println("Missing value");
                continue;
            }


            if (option.matches("(?:P|p)(?:ause)?")) {
                int time = TimeConverter.toMinutes(time(), tk.nextToken());
                if (time <= this.time()) {
                    System.err.println("Invalid time");
                } else {
                    Event nextPause = new PauseEvent(time);
                    Calendar.addEvent(nextPause);
                }
                continue;
            }
            else if (option.matches("(?:I|i)(?:nsert)?")) {
                int customer, amount, time;
                try {
                    customer  = Integer.parseInt(tk.nextToken());
                    amount    = Integer.parseInt(tk.nextToken());
                    time      = TimeConverter.toMinutes(time(), tk.nextToken());
                } catch (NumberFormatException nfe) {
                    System.err.println("Customer and amount must be integers");
                    continue;
                } catch (java.util.NoSuchElementException nse) {
                    System.err.println("Missing value");
                    continue;
                }
                if (customer < 0 || customer >= CustomerList.numCustomers()) {
                    System.err.printf("Customer %5d does not exist%n", 
                                                                     customer);
                    continue;
                }
                if (time <= 0) {
                    System.err.println(TimeConverter.TIME_HELP);
                    continue;
                } if (amount <= 0) {
                    System.err.println("Ordered amount must be positive");
                    continue;
                }

                if (customer == Simulator.HOME) {
                    System.err.printf("Town %5d is HOME vertex%n", customer);
                    continue;
                }

                Order o = new Order(time, customer, amount);        
                Event orderEvent = new OrderEvent(time, o);
                Calendar.addEvent(orderEvent);
                continue;
            }



            // ID options follow
            int id = 0;
            try {
                id = Integer.parseInt(tk.nextToken());
            } catch (NumberFormatException ex) {
                System.err.println("ID must be number");
                continue;
            }

            if (option.matches("(?:T|t)(?:ruck)?")) {
                Truck truck = TruckStack.get(id);
                if (truck == null) {
                    System.err.printf("Truck %5d does not exist%n", id);
                    continue;
                }

                System.err.println();
                System.err.printf("Truck %5d:%n", id);
                System.err.printf("Is near town %d%n", truck.currentTown());
                System.err.printf("Carries %d containers%n", truck.loaded());
                System.err.println("for orders:");

                for (Order o : truck.assignedOrders())
                    System.err.printf(
                             "\tOrder %5d from customer %5d, received at %s%n", 
                                     o.getId(), o.sentBy().customerId(), 
                                     TimeConverter.ascTime(o.received()));
            }
            else if (option.matches("(?:O|o)(?:rder)?")) {
                System.err.println();
                System.err.println(Reporter.orderReport(id));
            } else if (option.matches("(?:U|c)(?:ustomer)?")) {
                System.err.println();
                System.err.println(Reporter.customerReport(id));
            } else {
                System.err.println("Unknown option");
            }
        }

        return Simulator.CONTINUE;
    }

    @Override
    protected String log () {
        return "Simulation paused";
    }

    private void printSummary() {
        System.err.printf("%nPaused at %s%n", TimeConverter.ascTime(time()));
        TruckStack.summary();
        CustomerList.summary();
    }

    private void printHelp() {
        System.err.println();
        System.err.println("usage:");
        System.err.printf("\t[t]ruck          [%d-%d]%n", 1, TruckStack.size());
        System.err.printf("\t[o]rder          [%d-%d]%n", 1, OrderStack.size());
        System.err.printf("\t[p]ause          TIME%n");
        System.err.printf("\t[i]insert order  [%d-%d] AMOUNT TIME%n", 1, 
                                             CustomerList.numCustomers() - 1);
        System.err.printf("\t[c]ustomer       [%d-%d]%n", 1,  
                                             CustomerList.numCustomers() - 1);
        System.err.println("\t[h]elp");
    }
}

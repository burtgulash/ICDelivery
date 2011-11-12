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

            if (query == null || query.trim().equals(""))
                continue;

            query = query.toLowerCase();

            // continue simulation
            if (query.matches("q(?:uit)?|go?"))
                break;


            StringTokenizer tk = new StringTokenizer(query);
            if (!tk.hasMoreTokens()) {
                System.err.println("Missing option");
                continue;
            }

            String option = tk.nextToken();
            if (option.matches("h(?:elp)?")) {
                printHelp();
                continue;
            }

            // Options with value follow
            if (!tk.hasMoreTokens()) {
                System.err.println("Missing value");
                continue;
            }


            if (option.matches("p(?:ause)?")) {
                int time = TimeConverter.toMinutes(time(), tk.nextToken());
                if (time <= this.time()) {
                    System.err.println("Invalid time");
                } else {
                    Event nextPause = new PauseEvent(time);
                    Calendar.addEvent(nextPause);
                }
                continue;
            }
            else if (option.matches("i(?:nsert)?")) {
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

            if (option.matches("t(?:ruck)?")) {
                System.err.println();
                System.err.println(Reporter.truckReport(id));
            } else if (option.matches("o(?:rder)?")) {
                System.err.println();
                System.err.println(Reporter.orderReport(id));
            } else if (option.matches("c(?:ustomer)?")) {
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
        System.err.println(Reporter.truckSummary());
        System.err.println(Reporter.customerSummary());
    }

    private void printHelp() {
        System.err.println();
        System.err.println("usage:");
        System.err.printf("\t[t]ruck          [%d-%d]%n", 0, 
                                             TruckStack.size() - 1);
        System.err.printf("\t[o]rder          [%d-%d]%n", 0, 
                                             OrderStack.size() - 1);
        System.err.printf("\t[p]ause          TIME%n");
        System.err.printf("\t[i]insert order  [%d-%d] AMOUNT TIME%n", 0, 
                                             CustomerList.numCustomers() - 1);
        System.err.printf("\t[c]ustomer       [%d-%d]%n", 0,  
                                             CustomerList.numCustomers() - 1);
        System.err.printf("\t[g]o, [q]uit%n");
        System.err.println("\t[h]elp");
    }
}

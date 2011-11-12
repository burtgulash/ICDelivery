import static constant.Times.*;

import java.io.OutputStream;
import java.util.StringTokenizer;

import graph.Graph;



/**
 * Takes care of initialization of simulator components in correct order.
 */
public class Initializer {
    /**
     * Initializes simulation from given parameters
     */
    public static void initSimulation (Graph graph, int homeVertex, 
                          int simulationTime, String pause, int orderMean, 
                          int startOrders, String[] orders, int maxOrderAmount, 
                          boolean quiet, OutputStream logFile, 
                          OutputStream[] reportFiles, String strategy)
    {
        // Initialize components
        // Initialize Simulator first!
        Simulator.init(homeVertex, simulationTime);
        Calendar.init();

        Scheduler s;
        if (strategy.equals("greedy"))
            s = new GreedyScheduler(graph);
        else
            s = new ClarkeWrightScheduler(graph);

        Simulator.setScheduler(s);

        CustomerList.init(graph.vertices());
        TruckStack.init();

        // initialize logger and logFile
        if (!quiet)
            Logger.addOutput(System.out);
        if (logFile != null)
            Logger.addOutput(logFile);


        // first pause
        addFirstPause(pause);


        // add all initially known orders
        addOrders(orders);


        OrderGenerator gen = new ExponentialGenerator(orderMean, 
                                                  maxOrderAmount, homeVertex);
        generateOrders(gen, simulationTime, startOrders, orderMean);
    }

    private static void addFirstPause(String pause) {
        if (pause != null) {
            int pauseTime = 
                  TimeConverter.toMinutes(Simulator.START_TIME, pause);

            if (pauseTime == TimeConverter.NIL)
                System.err.println("Invalid pause time");
            else
                Calendar.addEvent(new PauseEvent(pauseTime));
        }
    }


    private static void generateOrders(OrderGenerator gen, int simulationTime, 
                                       int startOrders, int mean) 
    {
        int HALF_DAY   = DAY.time() / 2;

        Order generated;
        for (int i = 0; i < startOrders; i++) {
            generated = gen.generateAt(Simulator.START_TIME);
            sendOrder(generated);
        }

        while (true) {
            generated = gen.generateNext();
            if (generated.received() >= simulationTime - HALF_DAY)
                break;
            sendOrder(generated);
        }
    }

    private static void addOrders(String[] orders) {
        for (String orderString : orders) {
            StringTokenizer st = new StringTokenizer(orderString, ",");
            int customer, amount, time;
            try {
                customer  = Integer.parseInt(st.nextToken());
                amount    = Integer.parseInt(st.nextToken());
                time      = TimeConverter.toMinutes(0, st.nextToken());

                if (time < 0)
                    throw new NumberFormatException();
            } catch (NumberFormatException nfe) {
                System.err.println("Error parsing order");
                continue;
            } catch (java.util.NoSuchElementException nse) {
                System.err.println("Error parsing order");
                continue;
            }
            if (customer < 0 || customer >= CustomerList.size()) {
                System.err.printf("Customer %5d does not exist%n", customer);
                continue;
            }

            Order o = new Order(time, customer, amount);        
            Event orderEvent = new OrderEvent(time, o);
            Calendar.addEvent(orderEvent);
        }
    }


    private static void sendOrder(Order order) {
        Event orderEvent = new OrderEvent(order.received(), order);
        Calendar.addEvent(orderEvent);
    }
}

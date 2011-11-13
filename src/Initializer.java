import static constant.Times.*;

import java.io.OutputStream;
import java.util.StringTokenizer;

import graph.Graph;



/**
 * <p>
 * Takes care of initialization of simulator components in correct order.
 * </p>
 *
 * @author Tomas Marsalek
 */
public class Initializer {

    /**
     * Initializes simulation with given parameters
     *
     * @param graph Town-map represented as Graph data structure
     * @param homeVertex determine which one of graph's vertices will be 
     *                   HOME/DEPOT town
     * @param simulationTime time in minutes the simulation will terminate after
     * @param pause time in minutes of first interactive pause
     * @param orderMean mean value of length of time interval between 
     *                  generated orders
     * @param startOrders amount of initial orders (generated at 00:00:00)
     * @param orders list of manually entered orders
     * @param maxOrderAmount maximum amount a customer can order per one order
     * @param quiet true - don't print log to stdout, false - be verbose
     * @param logFile file to output log of simulation
     * @param reportFiles array of 3 report files 
     *                    (customer, order, truck) in this order
     * @param strategy scheduler strategy to be used in simulation,
     *                 supported: greedy, clarkewright
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
        generateOrders(gen, simulationTime, startOrders);
    }


    /**
     * Adds first pause event to Calendar.
     *
     * @param pause time of first interactive pause
     */
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


    /**
     * Generate initially known orders (not those manually provided by user)
     *
     * @param gen order generator implementation chosen to generate orders
     * @param simulationTime generator needs to know, when to stop generating
     *                       orders
     * @param startOrders number of orders to automatically generate at 00:00:00
     */
    private static void generateOrders(OrderGenerator gen, int simulationTime, 
                                       int startOrders) 
    {
        int HALF_DAY   = DAY.time() / 2;

        Order generated;
        for (int i = 0; i < startOrders; i++) {
            generated = gen.generateAt(Simulator.START_TIME);

            Event orderEvent = new OrderEvent(generated.received(), generated);
            Calendar.addEvent(orderEvent);
        }

        while (true) {
            generated = gen.generateNext();
            if (generated.received() >= simulationTime - HALF_DAY)
                break;

            Event orderEvent = new OrderEvent(generated.received(), generated);
            Calendar.addEvent(orderEvent);
        }
    }


    /**
     * Parse and insert all initially known manually specified orders.
     *
     * @param orders list of all manually specified orders
     */
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
}

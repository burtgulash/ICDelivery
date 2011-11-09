import static constant.Times.*;

import java.io.OutputStream;

import graph.Graph;



/**
 * Takes care of initialization of simulator components in correct order.
 */
public class Initializer {
    /**
     * Initializes simulation from given parameters
     */
    public static void initSimulation (Graph graph, int homeVertex, 
                                       int simulationTime, int pauseTime, 
                                       int orderMean, int startOrders, 
                                       int maxOrderAmount, boolean quiet, 
                                       OutputStream file, String strategy)
    {
        // Initialize components
        // Initialize Simulator first!
        Simulator.init(homeVertex, simulationTime);
        Calendar.init();
        // TODO decide on scheduler
        Scheduler s;
        if (strategy.equals("greedy"))
            s = new GreedyScheduler(graph);
        else
            s = new ClarkeWrightScheduler(graph);

        Simulator.setScheduler(s);

        CustomerList.init(graph.vertices());
        TruckStack.init();

        // initialize logger and outfile
        Logger.init();
        if (!quiet)
            Logger.addOutput(System.out);
        if (file != null)
            Logger.addOutput(file);

        // add pause
        Event pause = new PauseEvent(pauseTime);
        Calendar.addEvent(pause);


        // add all initially known orders
        OrderGenerator gen = new ExponentialGenerator(orderMean, 
                                                  maxOrderAmount, homeVertex);
        generateOrders(gen, simulationTime, startOrders, orderMean);
    }


    private static void generateOrders(OrderGenerator gen, int simulationTime, 
                                       int startOrders, int mean) 
    {
        int START_TIME = 0;
        int HALF_DAY   = DAY.time() / 2;

        Order generated;
        for (int i = 0; i < startOrders; i++) {
            generated = gen.generateAt(START_TIME);
            Simulator.sendOrder(generated);
        }

        while (true) {
            generated = gen.generateNext();
            if (generated.received() >= simulationTime - HALF_DAY)
                break;
            Simulator.sendOrder(generated);
        }
    }
}

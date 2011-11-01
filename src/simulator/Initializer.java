package simulator;

import java.io.OutputStream;

import stats.TruckStack;
import stats.CustomerList;
import stats.Logger;

import graph.Graph;


/**
 * Initializer class
 *
 * Takes care of initialization of most of the components in correct order.
 */
public class Initializer {
    /**
     * Initializes simulation from given parameters
     */
    public static void initSimulation (Graph graph,
                                       int homeVertex,
                                       int simulationTime,
                                       int pauseTime,
                                       int orderMean,
                                       int startOrders,
                                       int maxOrderAmount,
                                       boolean quiet,
                                       OutputStream file)

    {
        // Initialize components
        // Initialize Simulator first!
        Simulator.init(homeVertex, simulationTime);
        Calendar.init();
        Scheduler s = new GreedyScheduler(graph);
        Simulator.setScheduler(s);

        CustomerList.init(graph.vertices());
        TruckStack.init();

        Logger.init();
        if (!quiet)
            Logger.addOutput(System.out);
        if (file != null)
            Logger.addOutput(file);


        OrderGenerator gen = new UniformGenerator(maxOrderAmount, 
                                                  orderMean, homeVertex);
        generateOrders(gen, simulationTime, startOrders, orderMean);
    }


    private static void generateOrders(OrderGenerator gen, 
                                       int simulationTime, 
                                       int startOrders, int mean) 
    {
        int START_TIME = 0;

        for (int i = 0; i < startOrders; i++) 
            gen.generateAt(START_TIME);
                
        for (int i = 0; i < simulationTime/mean; i++)
            gen.generateNext();
    }
}

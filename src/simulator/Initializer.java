package simulator;

import stats.CustomerList;
import graph.Graph;
import stats.Logger;


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
                                       int depotVertex,
                                       int simulationTime,
                                       int pauseTime,
                                       int orderMean,
                                       int startOrders,
                                       int maxOrderAmount)

    {
        // Initialize components
        // Initialize Simulator first!
        Simulator.init(depotVertex, simulationTime);
        Calendar.init();
        Scheduler s = new GreedyScheduler(graph);
        Simulator.setScheduler(s);

        Logger.init(System.out);
        CustomerList.init(graph.vertices());


        OrderGenerator gen = new UniformGenerator(maxOrderAmount, 
                                                  orderMean, depotVertex);
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

package simulator;

import stats.CustomerList;
import graph.Graph;


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
        Scheduler s = new GreedyScheduler(graph, depotVertex, simulationTime);
        Simulator.init(s);
        Calendar.init(simulationTime);
        // TODO Logger.init();
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

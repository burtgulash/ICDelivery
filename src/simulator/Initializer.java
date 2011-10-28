package simulator;

import stats.CustomerList;
import stats.Logger;
import graph.Graph;


public static class Initializer {
    /**
     * Initializes simulation from given parameters
     */
    public static void initSimulation (Graph graph,
                                       int depotVertex,
                                       int simulationTime,
                                       int pauseTime,
                                       int startOrderCount,
                                       String logFile)

    {
        // Initialize components begin
        Scheduler s = new GreedyScheduler(graph, cal, depotVertex);
		Simulator.init(s);
        Calendar.init();
        // TODO Logger.init();
        CustomerList.init(graph.vertices());
        // Initialize components end



        // TODO Throw in initial events here

        cal.addEvent(new StopEvent(pauseTime));

        for(int i = 0; i < startOrderCount; i++)
            cal.addEvent(OrderGenerator.generateDefaultOrders(c));

        for(int i = 0; i < OrderGenerator.maxOrders(simulationTime); i++)
            cal.addEvent(OrderGenerator.generateOtherOrders(c,simulationTime));
    }
    
}

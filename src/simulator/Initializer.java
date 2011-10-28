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
                                       int pauseTime)

    {
        // Initialize components begin
        Scheduler s = new GreedyScheduler(graph, depotVertex);
		Simulator.init(s);
        Calendar.init(simulationTime);
        // TODO Logger.init();
        CustomerList.init(graph.vertices());
        // Initialize components end


    }
}

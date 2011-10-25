package simulator;

import stats.CustomerList;
import graph.Graph;


public class Initializer {
    /**
     * Initializes simulation from given parameters
     */
    public Simulator initializeSimulation(Graph graph,
                                int depotVertex,
                                int simulationTime)
    {
        // Initialize components

        int customers = graph.vertices();
        Calendar cal   = Calendar.getCalendarObject(simulationTime);
        Scheduler s    = new GreedyScheduler(graph, cal, depotVertex);
        CustomerList c = CustomerList.getCustomerListObject(customers);

        Simulator sim  = Simulator.getSimulatorObject(s, cal, c);

        

        // TODO Throw in initial events here

        // TODO Generate all orders here

        return sim;
    }
}

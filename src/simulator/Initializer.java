package simulator;

import stats.CustomerList;
import stats.Logger;
import graph.Graph;


public class Initializer {
    /**
     * Initializes simulation from given parameters
     */
    public Simulator initializeSimulation(Graph graph,
                                int depotVertex,
                                int simulationTime,
                                int startOrderCount,
                                String logFile)
    {
        // Initialize components
        Scheduler s    = new GreedyScheduler(graph, depotVertex);
        Calendar cal   = Calendar.getCalendarObject(simulationTime);
        CustomerList c = CustomerList.getCustomerListObject(graph.vertices());
        Logger l 	   = Logger.getLoggerObject(logFile);

        Simulator sim  = Simulator.getSimulatorObject(s, cal, c,l);

        

        // TODO Throw in initial events here

        for(int i = 0; i < startOrderCount; i++){
            cal.addEvent(OrderGenerator.generateDefaultOrders(c));
        }
        for(int i = 0; i < OrderGenerator.maxOrders(simulationTime); i++){
            cal.addEvent(OrderGenerator.generateOtherOrders(c,simulationTime));
        }
        

        return sim;
    }
    
}

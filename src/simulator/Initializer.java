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
                                int pauseTime,
                                int startOrderCount,
                                String logFile)

    {
        // Initialize components

        int customers = graph.vertices();
        Calendar cal   = Calendar.getCalendarObject(simulationTime);
        Scheduler s    = new GreedyScheduler(graph, cal, depotVertex);
        CustomerList c = CustomerList.getCustomerListObject(customers);
        Logger l 	   = Logger.getLoggerObject(logFile);


        Simulator sim  = Simulator.getSimulatorObject(s, cal, c,l);

        

        // TODO Throw in initial events here

        cal.addEvent(new StopEvent(pauseTime));

        for(int i = 0; i < startOrderCount; i++){
            cal.addEvent(OrderGenerator.generateDefaultOrders(c));
        }
        for(int i = 0; i < OrderGenerator.maxOrders(simulationTime); i++){
            cal.addEvent(OrderGenerator.generateOtherOrders(c,simulationTime));
        }
        

        return sim;
    }
    
}

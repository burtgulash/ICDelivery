import graph.Graph;
import graph.Path;

/**
 * Class Simulator
 *
 * Core of the simulation. Loops over events as they are stacked in Calendar
 * and logs what happened. Last event should be StopEvent, it guarantees 
 * successful termination of the application.
 */
class Simulator {
    /**
     * Strategy used by the Simulator.
     */
    static Scheduler scheduler;

    /**
     * Vertex in graph that was chosen to be DEPOT/HOME vertex.
     */
    static int HOME;

    /**
     * Start time of simulation.
     */
    static int START_TIME = 0;

    /**
     * End time of simulation.
     */
    static int TERMINATION_TIME;



    // Constants used solely by events' doWork method. 
    /**
     * Purpose of event returning this is to terminate the simulation.
     */
    final static protected int TERMINATE = 0;

    /**
     * Purpose of event returning this is to continue the simulation.
     */
    final static protected int CONTINUE  = 1;




    /**
     * Initializes simulation.
     *
     * @param depotVertex vertex in city-map chosen as truck depot/warehouse...
     * @param simulationTime time of termination
     */
    static void init(int depotVertex, int simulationTime) {
        HOME             = depotVertex;
        TERMINATION_TIME  = simulationTime;
    }


    /**
     * Sets the scheduler of simulation to different scheduler.
     *
     * @param s new Scheduler
     */
    static void setScheduler(Scheduler s) {
        scheduler = s;
    }



    /**
     * Core of the simulation, events are handled here.
     */
    static void mainLoop() {
        Event current;
        int purpose;

        do {
            current  = Calendar.nextEvent();
            purpose  = current.doWork();

            Logger.log(current.time(), current.log());
        } while (purpose != TERMINATE);
    }
}

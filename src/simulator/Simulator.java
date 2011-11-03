import graph.Graph;
import graph.Path;

/**
 * Class Simulator
 *
 * Core of the simulation. Loops over events as they are stacked in Calendar
 * and logs what happened. Last event should be StopEvent, it guarantees 
 * successful termination of the application.
 */
public class Simulator {
    static Scheduler scheduler;
    static int HOME;
    static int TERMINATION_TIME;
    final static int TERMINATE = 0;
    final static int CONTINUE  = 1;

    /**
     * Initializes simulation.
     *
     * @param depotVertex vertex in city-map chosen as truck depot/warehouse...
     * @param simulationTime time of termination
     */
    public static void init(int depotVertex, int simulationTime) {
        HOME             = depotVertex;
        TERMINATION_TIME  = simulationTime;
    }

    /**
     * Sets the scheduler of simulation to different scheduler.
     *
     * @param s new Scheduler
     */
    public static void setScheduler(Scheduler s) {
        scheduler = s;
    }

    /**
     * Core of the simulation, events are handled here.
     */
    public static void mainLoop() {
        Event current;
        int purpose;

        do {
            current  = Calendar.nextEvent();
            purpose  = current.doWork();

            Logger.log(current.time(), current.log());
        } while (purpose != TERMINATE);
    }
}

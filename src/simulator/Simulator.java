package simulator;

import stats.CustomerList;
import stats.Order;
import stats.Truck;
import stats.Logger;

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
    protected static Scheduler scheduler;
    protected final static int TERMINATE = 0;
    protected final static int CONTINUE  = 1;

    /**
     * Initializes simulation.
     *
     * @param s Scheduler strategy to be used within the simulation.
     */
    public static void init(Scheduler s) {
        scheduler  = s;
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

package simulator;

import stats.CustomerList;
import stats.Order;
import stats.Truck;

import graph.Graph;
import graph.Path;

public class Simulator {
    protected static Scheduler scheduler;
    protected final static int TERMINATE = 0;
    protected final static int CONTINUE  = 1;

    /**
     * Static constructor
     *
     * Provide with one of Scheduler strategy implementations
     */
    public static void init(Scheduler s) {
        scheduler  = s;
    }

    /**
     * Core of the simulation, events are handled here
     */
    public static void mainLoop() {
        Event current;
        int purpose;

        do {
            current  = Calendar.nextEvent();
            purpose  = current.doWork();

            // current.log();

        } while (purpose != TERMINATE);
    }
}

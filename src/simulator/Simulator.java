package simulator;

import stats.Order;
import truckDepot.Scheduler;
import truckDepot.GreedyScheduler;
import graph.Graph;

public class Simulator {
    private final int ORDER       = 1;
    private final int STOP        = 2;
    private final int TRUCK       = 3;
    private final int TRUCK_SEND  = 4;
    private final int TRUCK_LOAD  = 5;

    Calendar timeline;
    Scheduler scheduler;



    public Simulator(int simulationTime, Graph graph) {
		int depotVertex = 0; // change later

        timeline  = new Calendar(simulationTime);
        scheduler = new GreedyScheduler(graph, depotVertex);
    }

    /**
     * Core of the simulation, events are handled here
     */
    public void mainLoop() {
        Event current;

        MAIN_LOOP:
        while (true) {
            current = timeline.nextEvent();

            switch (current.eventType) {
                case STOP:
                    break MAIN_LOOP;

                case ORDER:
					Order currentOrder = ((OrderEvent) current).order;
                    scheduler.receiveOrder(currentOrder);
                    break;

                default:
                    System.err.println("Unexpected event occured");
                    return;
            }
        }
    }

    /**
     * Returns summary of all trucks and customers
     * to be used by logger
     */
    public void getSummary() {
		// TODO row
    }
}

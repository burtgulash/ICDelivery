package simulator;

import stats.Logger;
import stats.CustomerList;
import stats.Order;
import stats.Truck;

import graph.Graph;
import graph.Path;

public class Simulator {
    private static Scheduler scheduler;

    /**
     * Static constructor
     *
     * Provide with one of Scheduler strategy implementations
     */
    public static void init(Scheduler s) {
        scheduler = s;
    }

    /**
     * Core of the simulation, events are handled here
     */
    public static void mainLoop() {
        Event current;

        MAIN_LOOP:
        while (true) {
            current = Calendar.nextEvent();

            switch (current.type) {
                case STOP:
                    break MAIN_LOOP;

                case ORDER:
                    Order currentOrder = ((OrderEvent) current).order;
                    scheduler.receiveOrder(currentOrder);
                    break;


                case TRUCK_LOAD:
                    break;


                case TRUCK_SEND:
                    break;
                    

                default:
                    System.err.println("Unexpected event occured");
                    return;
            }
        }
    }
}

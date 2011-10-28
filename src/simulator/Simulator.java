package simulator;

import stats.Logger;
import stats.CustomerList;
import stats.Order;
import stats.Truck;

import graph.Graph;
import graph.Path;

public static class Simulator {
	private static scheduler;

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
                    TruckSend e = (TruckSend) current;
                    Truck t = e.truck;
                    if (t.arrived()) {
                        // unload -->> ??  UnloadEvent ??
                        // send back
                    } else {
                        // advance truck by one town
                        // refactor to separate method
                        int timeInNextTown = e.time() + t.timeToNextTown();
                        Path  fromNextTown = t.advance();
                        Event nextTownSend = new TruckSend(timeInNextTown,
                                                           fromNextTown,
                                                           t);
                        Calendar.addEvent(nextTownSend);
                    }
                    break;
                    

                default:
                    System.err.println("Unexpected event occured");
                    return;
            }
            Logger.note(current.log());
        }
        Logger.closeLog();
    }


    /**
     * Returns summary of all trucks and customers
     * to be used by Logger
     */
    public static void getSummary() {
        // TODO rov
    }
}

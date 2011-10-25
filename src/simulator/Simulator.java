package simulator;

import stats.Logger;
import stats.CustomerList;
import stats.Order;
import stats.Truck;

import graph.Graph;
import graph.Path;

public class Simulator {
    // singleton reference
    private static Simulator onlySimulator;
    

    Calendar timeline;
    Scheduler scheduler;
    Logger logger;
    CustomerList customerList;


    /**
      * Constructor for Simulator, provide all needed components
     */
    public static Simulator getSimulatorObject(Scheduler s,
                                               Calendar  cal,
                                               CustomerList cust,
                                               Logger log)
    {
        if (onlySimulator == null)
            onlySimulator = new Simulator(s, cal, cust, log);
        return onlySimulator;
    }

    // keep private, Simulator is singleton
    private Simulator(Scheduler s, 
                      Calendar cal, 
                      CustomerList cust,
                      Logger log)
    {
        scheduler    = s;
        timeline     = cal;
        customerList = cust;
        logger = log;
    }


    /**
     * Core of the simulation, events are handled here
     */
    public void mainLoop() {
        Event current;
        MAIN_LOOP:
        while (true) {
            current = timeline.nextEvent();

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
                    if (e.truck.arrived()) {
                        // unload -->> ??  UnloadEvent ??
                        // send back
                    } else {
                        // advance truck by one town
                        // refactor to separate method
                        Path  fromNextTown = t.advance();
                        int timeInNextTown = e.time() + t.timeToNextTown();
                        Event nextTownSend = new TruckSend(timeInNextTown,
                                                           fromNextTown,
                                                           t);
                        timeline.addEvent(nextTownSend);
                    }
                    break;
                    

                default:
                    System.err.println("Unexpected event occured");
                    return;
            }
            logger.note(current.log());
        }
        logger.closeLog();
    }

    /**
     * Returns summary of all trucks and customers
     * to be used by logger
     */
    public void getSummary() {
        // TODO rov
    }
}

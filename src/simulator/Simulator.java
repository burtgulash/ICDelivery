package simulator;

import stats.Customers;
import stats.Order;
import graph.Graph;

public class Simulator {
    

    Calendar timeline;
    Scheduler scheduler;
    Customers customerList;



    public Simulator(int simulationTime,int startOrderCount, Graph graph) {
		int depotVertex = 0; // change later
		
		customerList = new Customers(graph.vertices());
        timeline   = new Calendar(simulationTime);
        scheduler  = new GreedyScheduler(graph, depotVertex);
        
        addOrderEvents(simulationTime,startOrderCount);
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
    
    private void addOrderEvents(int simulationTime,int startOrderCount){
		for(int i = 0; i < startOrderCount; i++){
			timeline.addEvent(OrderGenerator.generateDefaultOrders(customerList));
		}
		for(int i = 0; i < OrderGenerator.maxOrders(simulationTime); i++){
			timeline.addEvent(OrderGenerator.generateOtherOrders(customerList,simulationTime));
		}
	}
}

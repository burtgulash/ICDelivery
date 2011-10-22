package events;
import priorityQueue.PriorityQueue;

public class EventHandler {
	
	PriorityQueue<Event> timeline = new PriorityQueue<Event>();
	private final static int MAX_ORDER_TIME = 6480;

	
	EventHandler(){
		Event e; // 1 - ORDER event, 2 - STOP event, 3 - TRUCK event, 4 - TRUCKSEND event, 5 - TRUCKLOAD event
		while(true){
			e = timeline.extractMin();
			switch (e.eventType){
			
			case 1:;break;
			case 2:;break;
			case 3:;break;
			case 4:;break;
			case 5:;break;
			
			}
		}
	}
	
	private void getOrders(int startOrderCount){
		for(int i = 0; i < startOrderCount; i++){
			timeline.insert(OrderGenerator.generateFirstOrders());
		}
		for(int i = 0; i < MAX_ORDER_TIME/10; i++){
			timeline.insert(OrderGenerator.generateOtherOrders());
		}
	}

}

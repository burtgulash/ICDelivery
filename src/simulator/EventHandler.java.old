package simulator;
import priorityQueue.PriorityQueue;

public class EventHandler {
	
	PriorityQueue<Event> timeline = new PriorityQueue<Event>();
	private final static int TWELVE_HOURS = 720;
	private final int SIM_TIME;
	private boolean isRunning = true;

	
	public EventHandler(int simTime, int pauseTime,int startOrderCount){
		this.SIM_TIME = simTime;
		insertStopEvent(pauseTime);
		getOrders(startOrderCount);
		Event e; // 1 - ORDER event, 2 - STOP event, 3 - TRUCK event, 4 - TRUCKSEND event, 5 - TRUCKLOAD event
		while(isRunning){
			e = timeline.extractMin();
			
			switch (e.eventType){
			
			case 1:;break;
			case 2: isRunning = false; break;
			case 3:;break;
			case 4:;break;
			case 5:;break;
			
			}
		}
	}
	
	/*
	 * Inserts number of OrderEvents based on user input and number of randomly placed OErderEvents
	 * 
	 */
	
	private void getOrders(int startOrderCount){
		for(int i = 0; i < startOrderCount; i++){
			timeline.insert(OrderGenerator.generateFirstOrders());
		}
		for(int i = 0; i < (SIM_TIME-TWELVE_HOURS)/10; i++){
			timeline.insert(OrderGenerator.generateOtherOrders());
		}
	}
	
	/*
	 * Inserts StopEvent in timeline based on user input
	 * 
	 */
	
	private void insertStopEvent(int time){
		timeline.insert(new StopEvent(time));
		
	}

}

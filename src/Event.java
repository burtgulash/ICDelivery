package events;

import priorityQueue.Queable;

public class Event implements Queable {
	private static int eventCount;
	long id;
	int priority;
	public int eventType;
	
	/*
	 * 1 - ORDER event
	 * 2 - STOP event
	 * 3 - TRUCK event
	 * 4 - TRUCKSEND event
	 * 5 - TRUCKLOAD event
	 */
	
	Event(){
		eventCount++;
		id = eventCount;
	}
		
	public int priority(){
		return priority;
	}
	
	public void setPriority(int newPriority){
		this.priority = newPriority;
	}
	
	public long id(){
		return id;
	}

}

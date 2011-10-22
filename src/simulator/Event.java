package simulator;

import priorityQueue.Queable;

public class Event implements Queable {
	private static int eventCount;
	int id;
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
		
	@Override
	public int priority(){
		return priority;
	}
	
	@Override
	public void setPriority(int newPriority){
		this.priority = newPriority;
	}
	
	@Override
	public int id(){
		return id;
	}

}

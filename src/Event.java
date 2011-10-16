package events;

public class Event {
	private static int eventCount;
	private int startTime;
	private int endTime;
	private int id;
	
	Event(){
		eventCount++;
		id = eventCount;
		
	}
	
	public int getId(){
		return id;
	}
	
	public int getStartTimet(){
		return startTime;
	}
	
	public int getEndTimet(){
		return endTime;
	}

}

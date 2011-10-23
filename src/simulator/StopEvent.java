package simulator;

public class StopEvent extends Event {
	
	public final int TYPE = 2;  // STOP event
	
	StopEvent(int priority){
		super.eventType = TYPE;
		setPriority(priority);
	}

}


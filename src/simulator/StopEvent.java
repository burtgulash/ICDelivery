package simulator;

public class StopEvent extends Event {
	
	public final int TYPE = 2;  // STOP event
	
	StopEvent(int time){
		super(time);
		super.eventType = TYPE;
	}

}

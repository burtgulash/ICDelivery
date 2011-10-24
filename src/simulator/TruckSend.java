package simulator;

public class TruckSend extends TruckEvent {

    public final int TYPE = 4; // TRUCKSEND event
	
	public TruckSend(int time) {
		super(time);
	}
}

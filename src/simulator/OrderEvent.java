package simulator;


public class OrderEvent extends Event {
	
	public final int TYPE = 1;  // ORDER event
		
	int cargoTons;
	int dest;

	

	// TRUCKLOAD event
	
	public OrderEvent(int cargoTons,int dest, int priority){
		this.cargoTons = cargoTons;
		this.dest = dest;
		setPriority(priority);
	}
	
	

}

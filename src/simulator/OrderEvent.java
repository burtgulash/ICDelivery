package simulator;

import stats.Order;


public class OrderEvent extends Event {
	
	public final int TYPE = 1;  // ORDER event

	public final Order order;

	// TRUCKLOAD event
	
	public OrderEvent(Order order) {
		this.order = order;
		setPriority(priority);
	}
}

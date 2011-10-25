package simulator;

import stats.LogEntry;
import stats.Order;


class OrderEvent extends Event {
	final Order order;

	// TRUCKLOAD event
	
	OrderEvent(int time, Order order) {
		super(time, EventType.ORDER);
		this.order = order;
	}

	@Override
	LogEntry log () {
		return new LogEntry(super.time(), "Received order n."+super.id()+" from "+ order.customer.vertex + " for " + order.amount() + " tons of ice cream.");
	}
}

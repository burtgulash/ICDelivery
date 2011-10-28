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
}

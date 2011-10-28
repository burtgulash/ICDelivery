package simulator;

import stats.Order;


class OrderEvent extends Event {
    final Order order;

    // TRUCKLOAD event
    
    OrderEvent(int time, Order order) {
        super(time);
        this.order = order;
    }

    @Override
    protected int doWork() {
        Simulator.scheduler.receiveOrder(order);
        return Simulator.CONTINUE;
    }    
}

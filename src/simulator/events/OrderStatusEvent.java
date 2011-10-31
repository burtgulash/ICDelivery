package simulator;

import stats.Order;


class OrderStatusEvent extends Event {
    Order order;
    boolean status;
    
    OrderStatusEvent(int time, Order order, boolean status) {
		super(time);
        this.status = status;
        this.order  = order;
    }

    @Override
    protected int doWork() {
        // accept or reject order
        return Simulator.CONTINUE;
    }

    @Override
    protected String log() {
        String statusString = status ? "accepted" : "rejected";
        return String.format("Order %5d %s", order.getId(), statusString);
    }
}

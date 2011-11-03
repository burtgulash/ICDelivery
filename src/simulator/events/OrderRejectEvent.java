class OrderRejectEvent extends Event {
    Order order;

    OrderRejectEvent(int time, Order order) {
        super(time);
        this.order  = order;
    }

    @Override
    protected int doWork() {
        // accept or reject order
        return Simulator.CONTINUE;
    }

    @Override
    protected String log() {
        return String.format("Order %5d rejected", order.getId());
    }
}

class OrderEvent extends Event {
    final Order order;

    // TRUCKLOAD event

    OrderEvent(int time, Order order) {
        super(time);
        this.order = order;
    }

    @Override
    protected int doWork() {
        // update stats
        order.sentBy().addOrder(order);
        OrderStack.add(order);
        Simulator.scheduler.receiveOrder(order);
        return Simulator.CONTINUE;
    }    

    @Override
    protected String log() {
        return String.format("Order %5d received from %4d for %2d containers", 
                              order.getId(), 
                              order.sentBy().customerId(), 
                              order.amount());
    }
}

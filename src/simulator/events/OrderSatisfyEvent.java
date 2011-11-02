class OrderSatisfyEvent extends Event {
    private Order order;
    private int satisfyAmount;

    OrderSatisfyEvent(int time, Order order, int amount) {
        super(time);
        this.order     = order;
        satisfyAmount  = amount;
    }

    @Override
    protected int doWork() {
        order.satisfy(satisfyAmount);
        return Simulator.CONTINUE;
    }

    @Override 
    protected String log() {
        return String.format("Order %5d completed [%2d/%2d]", 
                             order.getId(), order.satisfied(), order.amount());
    }
}

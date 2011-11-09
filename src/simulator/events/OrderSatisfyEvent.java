class OrderSatisfyEvent extends Event {
    private Order order;
    private int satisfyAmount;
	private Truck truck;

    OrderSatisfyEvent(int time, int amount, Truck truck, Order order) {
        super(time);
        this.order  = order;
        this.truck     = truck;
        satisfyAmount  = amount;
    }

    @Override
    protected int doWork() {
        order.satisfy(satisfyAmount);
        return Simulator.CONTINUE;
    }

    @Override 
    protected String log() {
        return String.format("Truck %5d delivered %2d tons to customer %5d", 
                   truck.getId(), satisfyAmount, order.sentBy().customerId());
    }
}

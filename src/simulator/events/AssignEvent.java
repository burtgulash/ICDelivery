class AssignEvent extends Event {
    private Truck truck;
    private Order order;
	private int satisfyAmount;

    AssignEvent(int time, int amount, Truck truck, Order order) {
        super(time);
        this.truck = truck;
        this.order = order;
		satisfyAmount = amount;
    }

    @Override
    protected int doWork() {
        return Simulator.CONTINUE;
    }

    @Override
    protected String log() {
        return String.format("Truck %5d assigned to order %5d : [%2d/%2d] tons"
                             ,
                             truck.getId(), 
                             order.getId(),
                             satisfyAmount,
                             order.amount());
    }
}

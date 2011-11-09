class CustomerAssignEvent extends Event {
    private Truck truck;
    private Customer customer;
    private int satisfyAmount;

    CustomerAssignEvent(int time, int amount, Truck truck, Customer customer) {
        super(time);
        this.truck = truck;
        this.customer = customer;
        satisfyAmount = amount;
    }

    @Override
    protected int doWork() {
        return Simulator.CONTINUE;
    }

    @Override
    protected String log() {
        return String.format("Truck %5d will deliver %2d containers to %5d" ,
                             truck.getId(), 
                             satisfyAmount,
                             customer.customerId(),
                             satisfyAmount);
    }
}

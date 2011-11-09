class CustomerSatisfyEvent extends Event {
    private Customer customer;
    private int satisfyAmount;
    private Truck truck;

    CustomerSatisfyEvent(int time, int amount, Truck truck, Customer customer) {
        super(time);
        this.customer  = customer;
        this.truck     = truck;
        satisfyAmount  = amount;
    }

    @Override
    protected int doWork() {
        customer.satisfy(satisfyAmount);
        return Simulator.CONTINUE;
    }

    @Override 
    protected String log() {
        return String.format("Truck %5d delivered %2d tons to customer %5d", 
                     truck.getId(), satisfyAmount, customer.customerId());
    }
}

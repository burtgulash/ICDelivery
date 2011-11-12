abstract class TruckEvent extends Event {
    protected Truck truck;
    private int eventCost;



    TruckEvent (int time, int cost, Truck truck) {
        super(time);
        this.truck = truck;
        this.eventCost = cost;
    }


    @Override
    protected abstract int doWork();


    @Override
    protected abstract String log();


    protected void updateTruck() {
        truck.addActionReport(this);
    }


    protected abstract String report();


    int actionCost() {
        return eventCost;
    }
}

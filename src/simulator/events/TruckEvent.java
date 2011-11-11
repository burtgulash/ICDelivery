abstract class TruckEvent extends Event {
    Truck truck;

    TruckEvent (int time, Truck truck) {
        super(time);
        this.truck = truck;
    }

    @Override
    protected abstract int doWork();

    @Override
    protected abstract String log();

    protected void updateTruck() {
        truck.addActionReport(this);
    }
}

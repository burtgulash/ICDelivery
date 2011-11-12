class TruckArrivedEvent extends TruckEvent {
    private int dst;


    TruckArrivedEvent(int time, int cost, Truck truck, int arrivedTo) {
        super(time, cost, truck);
        dst = arrivedTo;
    }


    @Override 
    protected int doWork() {
        truck.setTown(dst);
        updateTruck();
        return Simulator.CONTINUE;
    }


    @Override
    protected String log() {
        return String.format("Truck %5d arrived to %4d",
                             truck.getId(), dst);
    }

    @Override
    protected String report() {
        return String.format("Arrive to %4d", dst);
    }
}

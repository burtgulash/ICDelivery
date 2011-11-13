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
		String dstString = dst == Simulator.HOME ? 
                              "HOME" : String.format("%4d", dst);

        return String.format("Truck %5d arrived to %s",
                             truck.getId(), dstString);
    }

    @Override
    protected String report() {
        return String.format("Arrive to %4d", dst);
    }
}

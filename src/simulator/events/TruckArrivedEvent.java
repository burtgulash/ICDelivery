class TruckArrivedEvent extends TruckEvent {
    private int dst;
    private int pathCost;
    
    TruckArrivedEvent(int time, Truck truck, int arrivedTo, int cost) {
        super(time, truck);
        dst = arrivedTo;
        pathCost = cost;
    }

    
    @Override 
    protected int doWork() {
        truck.setTown(dst);
        return Simulator.CONTINUE;
    }


    @Override
    protected String log() {
        return String.format("Truck %5d arrived to %4d, cost: %d CZK",
                             truck.getId(), dst, pathCost);
    }
}

class TruckLoad extends TruckEvent {
    int amount;

    TruckLoad(int time, int cost, int amount, Truck truck) {
        super(time, cost, truck);
        this.amount = amount;
    }


    @Override
    protected int doWork() {
        truck.load(amount);
        updateTruck();
        return Simulator.CONTINUE;
    }


    @Override
    protected String log() {
        return String.format("Truck %5d loading %2d containers",                                            truck.getId(), amount);
    }


    @Override
    protected String report() {
        return String.format("Load %2d containers", amount);
    }
}

class TruckUnload extends TruckEvent {
    int amount;

    TruckUnload(int time, int cost, int amount, Truck truck) {
        super(time, cost, truck);
        this.amount = amount;
    }

    @Override
    protected int doWork() {
        truck.unload(amount);
        updateTruck();
        return Simulator.CONTINUE;
    }

    @Override
    protected String log() {
        return String.format("Truck %5d unloading %2d containers", 
                             truck.getId(), amount);
    }


    @Override
    protected String report() {
        return String.format("Unload %2d containers", amount);
    }
}

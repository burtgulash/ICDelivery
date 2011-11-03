class TruckReturn extends TruckEvent {
    TruckReturn(int time, Truck truck) {
        super(time, truck);
    }

    @Override
    protected int doWork() {
        truck.setTown(Simulator.HOME);
        return Simulator.CONTINUE;
    }

    @Override
    protected String log () {
        return String.format("Truck %5d returned back", 
                             truck.getId());
    }
}

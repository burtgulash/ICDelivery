package simulator;

import stats.Truck;

class TruckArrive extends TruckEvent {
    private int arrivedTo;

    TruckArrive(int time, Truck truck, int dst) {
        super(time, truck);
        arrivedTo = dst;
    }

    @Override
    protected int doWork() {
        truck.setTown(arrivedTo);
        return Simulator.CONTINUE;
    }
    
    @Override
    protected String log () {
        return String.format("Truck %5d arrived to %3d", 
                             truck.getId(), arrivedTo);
    }
}

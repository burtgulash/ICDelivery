package simulator;

import stats.Truck;

class TruckSend extends TruckEvent {
    private int src, dst;

    TruckSend(int time, Truck truck, int src, int dst) {
        super(time, truck);
        this.src = src;
        this.dst = dst;
    }

    @Override 
    protected int doWork() {
        truck.setTown(dst);
        return Simulator.CONTINUE;
    }

    @Override
    protected String log() {
        return String.format("Truck %5d sent from %3d to %3d", 
                             truck.getId(), src, dst);
    }
}

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
}

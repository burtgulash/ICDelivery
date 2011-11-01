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
        String srcString = 
               src == Simulator.DEPOT ? "HOME" : String.format("%4d", src);
        String dstString = 
               dst == Simulator.DEPOT ? "HOME" : String.format("%4d", dst);

        return String.format("Truck %5d sent from %s to %s", 
                             truck.getId(), srcString, dstString);
    }
}

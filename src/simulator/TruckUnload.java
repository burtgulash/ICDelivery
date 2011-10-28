package simulator;

import stats.Truck;

class TruckUnload extends TruckEvent {
    int amount;

    TruckUnload(int time, int amount, Truck truck) {
        super(time, truck);
        this.amount = amount;
    }

    @Override
    protected int doWork() {
        truck.unload(amount);
        return Simulator.CONTINUE;
    }
}

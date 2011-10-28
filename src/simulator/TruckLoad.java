package simulator;

import stats.Truck;

class TruckLoad extends TruckEvent {
    int amount;

    TruckLoad(int time, int amount, Truck truck) {
        super(time, truck);
        this.amount = amount;
    }

    @Override
    protected int doWork() {
        truck.load(amount);
        return Simulator.CONTINUE;
    }
}

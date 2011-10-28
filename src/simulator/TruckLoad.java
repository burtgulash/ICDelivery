package simulator;

import stats.Truck;

class TruckLoad extends TruckEvent {
    int amount;

    TruckLoad(int time, int amount, Truck truck) {
        super(time, EventType.TRUCK_LOAD, truck);
        this.amount = amount;
    }
}

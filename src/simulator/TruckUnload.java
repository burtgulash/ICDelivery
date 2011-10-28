package simulator;

import stats.Truck;

class TruckUnload extends TruckEvent {
    int amount;

    TruckUnload(int time, int amount, Truck truck) {
        super(time, EventType.TRUCK_UNLOAD, truck);
        this.amount = amount;
    }
}

package simulator;

import stats.Truck;

class TruckSend extends TruckEvent {
    private int src, dst;

    TruckSend(int time, Truck truck, int src, int dst) {
        super(time, EventType.TRUCK_SEND, truck);
        this.src = src;
        this.dst = dst;
    }
}

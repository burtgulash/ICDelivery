package simulator;

import stats.Truck;

class TruckArrive extends TruckEvent {
    private int arrivedTo;

    TruckArrive(int time, Truck truck, int dst) {
        super(time, EventType.TRUCK_ARRIVE, truck);
        arrivedTo = dst;
    }
    
    /**
     * Return town of arrival
     */
    public int arrivedTo() {
        return arrivedTo;
    }
}

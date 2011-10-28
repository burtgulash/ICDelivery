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
    
    /**
     * Return town of arrival
     */
    public int arrivedTo() {
        return arrivedTo;
    }
}

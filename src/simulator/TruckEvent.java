package simulator;

import stats.Truck;

abstract class TruckEvent extends Event {
    Truck truck;

    TruckEvent (int time, Truck truck) {
        super(time);
        this.truck = truck;
    }

	@Override
	protected abstract int doWork();
}

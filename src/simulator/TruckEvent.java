package simulator;

import stats.Truck;

abstract class TruckEvent extends Event {
	Truck truck;

	TruckEvent (int time, EventType type) {
		super(time, type);
	}
}

package simulator;

import stats.LogEntry;
import stats.Truck;

class TruckLoad extends TruckEvent {
	int amount;

	TruckLoad(int time, int amount, Truck truck) {
		super(time, EventType.TRUCK_LOAD, truck);
		this.amount = amount;
	}

	@Override
	LogEntry log () {
		return new LogEntry(super.time(), "Truck n."+truck.getId()+" was loaded with "+ amount  + " tons of ice cream.");
	}
}

package simulator;

import stats.LogEntry;
import stats.Truck;
import graph.Path;

class TruckSend extends TruckEvent {
	// redundant ?
	private Path toDestination;

	TruckSend(int time, Path path, Truck truck) {
		super(time, EventType.TRUCK_SEND, truck);
		toDestination = path;
	}

	@Override
	LogEntry log () {
		return new LogEntry(super.time(), "Truck n."+super.id()+" was sent to "+ truck.assignedOrderInfo().customer.vertex  + ".");
	}
}

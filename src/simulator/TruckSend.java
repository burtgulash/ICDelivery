package simulator;

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
	void log () {
	}
}

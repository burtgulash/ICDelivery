package simulator;

class TruckLoad extends TruckEvent {
	TruckLoad(int time) {
		super(time, EventType.TRUCK_LOAD);
	}

	@Override
	void log () {
	}
}

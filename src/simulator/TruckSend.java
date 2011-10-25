package simulator;

class TruckSend extends TruckEvent {
	TruckSend(int time) {
		super(time, EventType.TRUCK_SEND);
	}

	@Override
	void log () {
	}
}

package simulator;

import stats.LogEntry;

class StopEvent extends Event {
	
	StopEvent(int time){
		super(time, EventType.STOP);
	}

	@Override
	LogEntry log () {
		return new LogEntry(super.time(), "Simulation has been stoped. (ID "+ super.id() +")");
	}
}

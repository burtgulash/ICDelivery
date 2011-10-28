package simulator;

import stats.LogEntry;

class StopEvent extends Event {
    
    StopEvent(int time){
        super(time, EventType.STOP);
    }
}

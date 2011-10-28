package simulator;

class StopEvent extends Event {
    
    StopEvent(int time){
        super(time, EventType.STOP);
    }
}

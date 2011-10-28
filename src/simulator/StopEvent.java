package simulator;

class StopEvent extends Event {
    
    StopEvent(int time){
        super(time);
    }

    @Override
    protected int doWork() {
        return Simulator.TERMINATE;
    }
}

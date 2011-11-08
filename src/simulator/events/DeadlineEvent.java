class DeadlineEvent extends Event {
    DeadlineEvent(int time) {
        super(time);
    }

    @Override
    protected int doWork() {
        Simulator.scheduler.releaseAll();
        return Simulator.CONTINUE;
    } 

    @Override
    protected String log() {
        return String.format("All trucks released");
    }
}

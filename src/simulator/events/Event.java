import priorityQueue.Queable;

abstract class Event implements Queable {
    private static int eventCount;
    private int id;
    private int time;

    Event(int time) {
        eventCount++;
        id = eventCount;

        this.time = time;
    }

    /**
     * Let the event do something
     */
    protected abstract int doWork ();

    /**
     * Selector for event time
     */
    protected int time() {
        return time;
    }

    protected abstract String log ();


    // DONT USE THESE METHODES
    @Override
    public int priority(){
        return time;
    }

    @Override
    public void setPriority(int newPriority){
        time = newPriority;
    }

    @Override
    public int id(){
        return id;
    }

}

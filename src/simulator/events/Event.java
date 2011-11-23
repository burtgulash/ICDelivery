import priorityQueue.Queable;

/**
 * Object that is used as an event in simulation.
 * Holds its own time, which specifies when this event will be seen.
 */
abstract class Event implements Queable {
    private static int eventCount;
    private int id;
    private int time;

    /**
     * Create event with given time.
     *
     * @param time time when this event is seen.
     */
    Event(int time) {
        eventCount++;
        id = eventCount;

        this.time = time;
    }

    /**
     * Let the event do something
     *
     * @return simulation TERMINATE or CONTINUE command.
     */
    protected abstract int doWork ();

    /**
     * Selector for event time.
     *
     * @return Time of this event.
     */
    protected int time() {
        return time;
    }


    /**
     * Gets log message of this event.
     *
     * @return Log string of this event.
     */
    protected abstract String log ();


    // DONT USE THESE METHODES (only for priority queue)
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

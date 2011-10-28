package simulator;

import priorityQueue.Queable;

abstract class Event implements Queable {
    private static int eventCount;
    private int id;
    private int time;
    final EventType type;
    
    Event(int time, EventType type) {
        eventCount++;
        id = eventCount;

        this.time = time;
        this.type = type;
    }

    /**
     * Selector for event time
     */
    int time() {
        return time;
    }


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

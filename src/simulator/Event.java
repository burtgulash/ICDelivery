package simulator;

import priorityQueue.Queable;
import stats.LogEntry;

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
	 * Calendar calls this method when nextEvent is drawn
	 */
	abstract LogEntry log ();

	/**
	 * Selector for event time
	 */
    int time() {
        return time;
    }


  // ---- Don't use methods below this point ----- \\
 // ------- Unless you are Priority Queue --------- \\ 
// ------------------------------------------------- \\

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

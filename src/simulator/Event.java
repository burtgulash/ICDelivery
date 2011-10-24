package simulator;

import priorityQueue.Queable;

public class Event implements Queable {
    private static int eventCount;
    private int id;
    private int time;
    public int eventType;
    
    /*
     * 1 - ORDER event
     * 2 - STOP event
     * 3 - TRUCK event
     * 4 - TRUCKSEND event
     * 5 - TRUCKLOAD event
     */
    
    Event(int time){
        eventCount++;
        id = eventCount;

		this.time = time;
    }

	/**
	 * Selector for event time
	 */
    public int time() {
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
        this.time = newPriority;
    }
    
    @Override
    public int id(){
        return id;
    }

}

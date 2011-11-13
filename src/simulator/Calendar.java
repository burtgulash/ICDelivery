import priorityQueue.PriorityQueue;

import static constant.Times.*;


/**
 * Calendar class
 *
 * Only one Calendar should exist and it should be publicly available
 * to every class that communicates by Events
 */
class Calendar {
    private static PriorityQueue<Event> queue;


    /**
     * Initialized static Calendar object.
     *
     * @param terminationTime time when the simulation will end
     */
    public static void init() {
        queue = new PriorityQueue<Event>();

        // make sure we terminate
        addEvent(new StopEvent(Simulator.TERMINATION_TIME));
    }



    /**
     * Adds event into calendar.
     *
     * @param event event to be inserted
     */
    static void addEvent(Event event) {
        queue.insert(event);
    }


    private static int lastEventTime = 0;
    /**
     * Returns next event in timeline.
     *
     * @return next Event
     */
    static Event nextEvent() {
        Event next;
        try {
            next = queue.extractMin();
        } catch (PriorityQueue.EmptyQueueException ex) {
            // should never happen
            System.err.println("Queue is empty");
            return null;
        }
        assert(next.time() >= lastEventTime);
        lastEventTime = next.time();
        return next;
    }
}

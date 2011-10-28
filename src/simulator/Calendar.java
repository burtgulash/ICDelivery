package simulator;

import priorityQueue.PriorityQueue;


/**
 * Calendar class
 *
 * Only one instance should exist and it should be publicly available
 * to every class that communicates by Events
 */
static class Calendar {

    private static PriorityQueue<Event> queue;
    

    // Private constructor for Calendar singleton
    public static void init(int terminationTime) {
        queue = new PriorityQueue<Event>();

        // make sure we terminate
        addEvent(new StopEvent(terminationTime));
    }



    /**
     * Adds event into calendar
     */
    static void addEvent(Event event) {
        queue.insert(event);
    }

    /**
     * Returns next event in timeline
     */
    static Event nextEvent() {
        Event extractedEvent = queue.extractMin();

        // log what happened
        return extractedEvent;
    }
}

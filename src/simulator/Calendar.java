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

    /**
     * Returns next event in timeline.
     *
     * @return next Event
     */
    static Event nextEvent() {
        return queue.extractMin();
    }

    static String ascTime(int time) {
        int days   = time / DAY.time() + 1;
        int hours  = (time % DAY.time()) / MINUTES_IN_HOUR.time();
        int mins   = (time % DAY.time()) % MINUTES_IN_HOUR.time();;
        return String.format("Day %1d  %02d:%02d", days, hours, mins);
    }
}

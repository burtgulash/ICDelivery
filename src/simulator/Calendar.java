package simulator;

import priorityQueue.PriorityQueue;


/**
 * Calendar class
 *
 * Only one instance should exist and it should be publicly available
 * to every class that communicates by Events
 */
class Calendar {

	// singleton reference
	private static Calendar onlyCalendar;

	private PriorityQueue<Event> queue;
	
	public static Calendar getCalendarObject(int terminationTime) {
		if (onlyCalendar == null)
			onlyCalendar = new Calendar(terminationTime);
		return onlyCalendar;
	}

	// Private constructor for Calendar singleton
	private Calendar(int terminationTime) {
		queue = new PriorityQueue<Event>();

		// make sure we terminate
		addEvent(new StopEvent(terminationTime));
	}



	/**
	 * Adds event into calendar
	 */
	void addEvent(Event event) {
		queue.insert(event);
	}

	/**
	 * Returns next event in timeline
	 */
	Event nextEvent() {
		Event extractedEvent = queue.extractMin();

		// log what happened
		extractedEvent.log();
		return extractedEvent;
	}
}

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
	
	Calendar getCalendarObject() {
		if (onlyCalendar == null)
			onlyCalendar = new Calendar();
		return onlyCalendar;
	}

	// Private constructor for Calendar singleton
	private Calendar() {
		queue = new PriorityQueue<Event>();
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
		return queue.extractMin();
	}
}

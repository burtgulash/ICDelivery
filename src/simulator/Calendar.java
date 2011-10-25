package simulator;

import priorityQueue.PriorityQueue;


/**
 * Calendar class
 *
 * Only one instance should exist and it should be publicly available
 * to every class that communicates by Events
 */
class Calendar {
	private PriorityQueue<Event> cal;

	Calendar(int simulationTime) {
		cal = new PriorityQueue<Event>();
		addEvent(new StopEvent(simulationTime));
	}


	/**
	 * Adds event into calendar
	 */
	void addEvent(Event event) {
		cal.insert(event);
	}

	/**
	 * Returns next event in timeline
	 */
	Event nextEvent() {
		return cal.extractMin();
	}
}

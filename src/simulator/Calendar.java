package simulator;

import priorityQueue.PriorityQueue;


/**
 * Calendar class
 *
 * Only one instance should exist and it should be publicly available
 * to every class that communicates by Events
 */
public class Calendar {
	private PriorityQueue<Event> cal;

	public Calendar(int simulationTime) {
		cal = new PriorityQueue<Event>();
		addEvent(new StopEvent(simulationTime));
	}


	/**
	 * Adds event into calendar
	 */
	public void addEvent(Event event) {
		cal.insert(event);
	}

	/**
	 * Returns next event in timeline
	 */
	public Event nextEvent() {
		return cal.extractMin();
	}
}

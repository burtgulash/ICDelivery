/**
 * OrderGenerator interface
 *
 * Generates orders that are then processed by Scheduler 
 * and sent to Calendar
 */
public interface OrderGenerator {
	public void generateAt(int time);
	public void generateNext();	
}

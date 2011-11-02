/**
 * OrderGenerator interface
 *
 * Generates orders that are then processed by Scheduler 
 * and sent to Calendar
 */
public interface OrderGenerator {
    public Order generateAt(int time);
    public Order generateNext();    
}

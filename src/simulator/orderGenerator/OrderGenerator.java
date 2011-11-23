/**
 * OrderGenerator interface
 *
 * Generates orders that are then processed by Scheduler 
 * and sent to Calendar
 */
public interface OrderGenerator {

    /**
     * Generates order that will be seen in specified time.
     *
     * @param time time the order will be sent by customer.
     */
    public Order generateAt(int time);


    /**
     * Generates order that comes after last generated order
     * according to implementation.
     */
    public Order generateNext();    
}

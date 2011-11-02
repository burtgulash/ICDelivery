/**
 * Scheduler interface
 *
 * Responsible for every planning within the simulation.
 * Maps Orders to Trucks, which get sent to a corresponding customer
 * according to a strategy implemented by different implementors.
 *
 */
public interface Scheduler {

    /**
     * Receives Order from Simulator class and plans every following action.
     *
     * @param order Order to be processed
     */
    public void receiveOrder(Order order);
}

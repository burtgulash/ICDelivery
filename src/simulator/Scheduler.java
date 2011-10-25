package simulator;

import stats.Order;


/**
 * Scheduler interface
 *
 * assigns Orders to Trucks, which get sent directly to Dispatcher
 * Tries to minimize transportation costs
 *
 * Implemented by GreedyScheduler and ClarkeWrightScheduler
 *
 */
public interface Scheduler {

    /**
     * Receives Order from Simulator class and sends response to Dispatcher
     */
    public void receiveOrder(Order order);

    /**
     * Forces the scheduler to release every car in its wait queue
     */
    public void forceDispatch();
}

import graph.Path;

import static constant.Times.*;
import static constant.Costs.*;

/**
 * <p>
 * Keeps information about trip that truck is going to take where its purpose
 * is to deliver containers.
 * </p>
 */
class DeliveryTrip extends Trip {
    private int dispatchTime;
    private int arrivalTime;

    private int loadAmount;
    private int unloadAmount;


    /**
     * Create new trip with purpose of delivering goods.
     *
     * @param startTime initial estimate of starting time of this trip
     *                  (that is when the truck will start to load goods)
     * @param path path a truck will take to destination, cannot be changed
     *             later
     * @param loadAmount amount the truck that gets this trip 
     *                   will load at startTime
     * @param unloadAmount amount the truck that gets this trip
     *                     will unload at destination
     * @param cargo amount that will be carried by truck that this trip will be
     *              will be assigned to. Is not the same as loadAmount.
     */
    DeliveryTrip (int startTime, Path path, 
                  int loadAmount, int unloadAmount, int cargo)
    {
        super(startTime, cargo, path);
        this.loadAmount = loadAmount;
        this.unloadAmount = unloadAmount;

        computeTimes();
        computeCost();
    }


    /**
     * Create new trip with purpose of delivering goods. Short constructor.
     *
     * @param startTime initial estimate of starting time of this trip
     *                  (that is when the truck will start to load goods)
     * @param path path a truck will take to destination, cannot be changed
     *             later
     * @param assignedAmount in case where a truck only goes from HOME to 
     *                       destination and only carries loaded amount
     */
    DeliveryTrip(int startTime, Path path, int assignedAmount) {
        this(startTime, path, assignedAmount, assignedAmount, assignedAmount);
    }


    /**
     * Gets dispatch time of this trip (time after successful loading)
     *
     * @return Time of dispatch.
     */
    int dispatchTime() {
        return dispatchTime;
    }


    /**
     * Gets arrival time of this trip (time before unloading)
     *
     * @return Time of arrival.
     */
    int arrivalTime() {
        return arrivalTime;
    }


    @Override
    /**
     * Computes dispatch, arrival and end time of this trip.
     */
    protected void computeTimes() {
        dispatchTime = startTime     + loadAmount * LOAD.time();
        arrivalTime  = dispatchTime  + path.pathLength() * 
                                       MINUTES_IN_HOUR.time() / Truck.SPEED;
        endTime      = arrivalTime   + unloadAmount * UNLOAD.time();
    }


    @Override
    /**
     * Computes total cost of this trip. (not real cost)
     */
    protected void computeCost() {
        totalCost = path.pathLength() * 
                    (BASE.cost() + cargo * TRANSPORT.cost()); 
        totalCost += unloadAmount * UNLOADING.cost();
    }


    @Override
    /**
     * Delays this trip by specified time.
     *
     * @param delayTime time in minutes to delay this trip by
     */
    void delay(int delayTime) {
        startTime     += delayTime;
        dispatchTime  += delayTime;
        arrivalTime   += delayTime;
        endTime       += delayTime;
    }


    /**
     * Dispatches a truck from HOME 
     * and takes care of all TruckSend and TruckArrival events.
     * 
     * @param truck a truck to dispatch.
     */
    void sendTruck(Truck truck) {
        sendTruck(dispatchTime, Simulator.HOME, truck);
    }
}

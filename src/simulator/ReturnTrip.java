import graph.Path;

import static constant.Times.*;
import static constant.Costs.*;


/**
 * Keeps information about trip where a truck only returns from somewhere
 * to HOME.
 *
 */
class ReturnTrip extends Trip {
    ReturnTrip (int startTime, Path toHOME) {
        super(startTime, 0, toHOME);

        computeTimes();
        computeCost();
    }


    @Override
    /**
     * Computes all times required by this trip.
     */
    protected void computeTimes() {
        endTime = startTime + path.pathLength() * 
                              MINUTES_IN_HOUR.time() / Truck.SPEED;
    }


    @Override
    /**
     * Computes all costs a truck will spend on this trip.
     */
    protected void computeCost() {
        totalCost = path.pathLength() * BASE.cost(); 
    }


    @Override
    /**
     * Delays this trip by specified amount of time.
     *
     * @param delayTime time in minutes to delay this trip by.
     */
    void delay(int delayTime) {
        startTime += delayTime;
        endTime += delayTime;
    }


    /**
     * Sends a truck on this trip. Takes care of all TruckSend and TruckArrival
     * events and puts them into Calendar.
     *
      * @param customer customer vertex a truck is going back from
     * @param truck a truck to dispatch.
     */
    void sendTruck(int customer, Truck truck) {
        sendTruck(startTime, customer, truck);
    }
}

import graph.Path;

import static constant.Times.*;
import static constant.Costs.*;

import graph.Path;



/**
 * Keeps information about trip a truck is going to take.
 *
 */
abstract class Trip {
    protected int startTime;
    protected int endTime;
    protected int totalCost;

    protected int cargo;
    protected Path path;


    /**
     * Construct new Trip, should be used by extended classes.
     *
     * @param startTime estimated start time of this trip (can be delayed)
     * @param cange number of containers a truck will carry taking this trip
     * @param path path a truck will take on this trip
     */
    protected Trip (int startTime, int cargo, Path path) {
        this.startTime  = startTime;
        this.path       = path;
        this.cargo      = cargo;
    }


    /**
     * Gets start time of this trip.
     *
     * @return Start time of this trip.
     */
    int startTime() {
        return startTime;
    }


    /**
     * Gets end time of this trip.
     *
     * @return End time of this trip.
     */
    int endTime() {
        return endTime;
    }


    /**
     * Gets path of this trip.
      *
     * @return Path of this trip.
     */
    Path path() {
        return path;
    }

    protected abstract void computeTimes();
    protected abstract void computeCost();

    /**
     * Delays this trip by specified time.
     *
     * @param delayTime time in minutes to delay this trip by.
     */
    abstract void delay(int delayTime);


    /**
     * Gets total cost spent by one truck that would take this trip.
     */
    final int tripCost() {
        return totalCost;
    }


    /**
     * Adds TruckSend and TruckArrival events to Calendar.
     * 
     * @param dispatchAt dispatch the truck at this time
     * @src   path doesn't remember source vertex, provide it
     * @truck truck to dispatch
     */
    void sendTruck(int dispatchAt, int src, Truck truck) {
        // update cost for this truck
        truck.updateTravelCost(this);

        Path p       = path;
        int fromTime = dispatchAt;
        int distance = p.distanceToNext();
        int toTime   = fromTime + distance * 
                                  MINUTES_IN_HOUR.time() / Truck.SPEED;
        int dst      = p.to();
        int cost     = distance * (BASE.cost() + cargo * TRANSPORT.cost());

        while (p.rest() != null) {
            Event advance = new TruckSend(fromTime, cost, truck, src, dst);
            Calendar.addEvent(advance);

            p = p.rest();
            src = dst;
            dst = p.to();
            fromTime = toTime;
            distance = p.distanceToNext();
            toTime = fromTime + distance * 
                                MINUTES_IN_HOUR.time() / Truck.SPEED;
            cost     = distance * (BASE.cost() + cargo * TRANSPORT.cost());
        }

        Event toLastTown = new TruckSend(fromTime, cost, truck, src, dst);
        Event arrived = new TruckArrivedEvent(toTime, 0, truck, dst);

        Calendar.addEvent(toLastTown);
        Calendar.addEvent(arrived);
    }
}

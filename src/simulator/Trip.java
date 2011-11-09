import graph.Path;

import static constant.Times.*;
import static constant.Costs.*;

import graph.Path;


abstract class Trip {
    protected int startTime;
    protected int endTime;
    protected int totalCost;

    protected Path path;

    Trip (int startTime, Path path) {
        this.startTime      = startTime;
        this.path           = path;
    }

    int startTime() {
        return startTime;
    }

    int endTime() {
        return endTime;
    }

    Path path() {
        return path;
    }

    protected abstract void computeTimes();
    protected abstract void computeCost();
    abstract void delay(int delayTime);

    final int tripCost() {
        return totalCost;
    }


    /**
     * Adds send-events to Calendar.
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
        int toTime   = fromTime +  p.distanceToNext() * 
                                   MINUTES_IN_HOUR.time() / Truck.SPEED;
        int dst      = p.to();

        while (p.rest() != null) {
            Event advanceByTown = new TruckSend(fromTime, truck, src, dst);
            Calendar.addEvent(advanceByTown);

            p = p.rest();
            src = dst;
            dst = p.to();
            fromTime = toTime;
            toTime = fromTime + path.distanceToNext() * 
                                MINUTES_IN_HOUR.time() / Truck.SPEED;
        }

        Event toLastTown = new TruckSend(fromTime, truck, src, dst);

        Event arrived = 
             new TruckArrivedEvent(fromTime + 1, truck, dst, tripCost());

        Calendar.addEvent(toLastTown);
        Calendar.addEvent(arrived);
    }
}

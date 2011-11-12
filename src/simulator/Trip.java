import graph.Path;

import static constant.Times.*;
import static constant.Costs.*;

import graph.Path;


abstract class Trip {
    protected int startTime;
    protected int endTime;
    protected int totalCost;

    protected int cargo;
    protected Path path;


    Trip (int startTime, int cargo, Path path) {
        this.startTime  = startTime;
        this.path       = path;
        this.cargo      = cargo;
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

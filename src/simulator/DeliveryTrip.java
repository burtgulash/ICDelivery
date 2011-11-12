import graph.Path;

import static constant.Times.*;
import static constant.Costs.*;

class DeliveryTrip extends Trip {
    private int dispatchTime;
    private int arrivalTime;

    private int loadAmount;
    private int unloadAmount;

    DeliveryTrip (int startTime, Path path, 
                  int loadAmount, int unloadAmount, int cargo)
    {
        super(startTime, cargo, path);
        this.loadAmount = loadAmount;
        this.unloadAmount = unloadAmount;

        computeTimes();
        computeCost();
    }

    DeliveryTrip(int startTime, Path path, int assignedAmount) {
        this(startTime, path, assignedAmount, assignedAmount, assignedAmount);
    }

    int dispatchTime() {
        return dispatchTime;
    }

    int arrivalTime() {
        return arrivalTime;
    }

    @Override
    protected void computeTimes() {
        dispatchTime = startTime     + loadAmount * LOAD.time();
        arrivalTime  = dispatchTime  + path.pathLength() * 
                                       MINUTES_IN_HOUR.time() / Truck.SPEED;
        endTime      = arrivalTime   + unloadAmount * UNLOAD.time();
    }

    @Override
    protected void computeCost() {
        totalCost = path.pathLength() * 
                    (BASE.cost() + cargo * TRANSPORT.cost()); 
        totalCost += unloadAmount * UNLOADING.cost();
    }

    @Override
    void delay(int delayTime) {
        startTime     += delayTime;
        dispatchTime  += delayTime;
        arrivalTime   += delayTime;
        endTime       += delayTime;
    }


    void sendTruck(Truck truck) {
        sendTruck(dispatchTime, Simulator.HOME, truck);
    }
}

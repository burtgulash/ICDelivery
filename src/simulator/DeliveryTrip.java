import graph.Path;

import static constant.Times.*;
import static constant.Costs.*;

public class DeliveryTrip extends Trip {
    private int dispatchTime;
    private int arrivalTime;

    private int assignedAmount;

    public DeliveryTrip (int startTime, Path path, int assignedAmount) {
        super(startTime, path);
        this.assignedAmount = assignedAmount;

        computeTimes();
        computeCost();
    }

    public int dispatchTime() {
        return dispatchTime;
    }

    public int arrivalTime() {
        return arrivalTime;
    }

    @Override
    protected void computeTimes() {
        dispatchTime = startTime     + assignedAmount * LOAD.time();
        arrivalTime  = dispatchTime  + path.pathLength() * 
                                       MINUTES_IN_HOUR.time() / Truck.SPEED;
        endTime      = arrivalTime   + assignedAmount * UNLOAD.time();
    }

    @Override
    protected void computeCost() {
        totalCost += path.pathLength() * 
                     (BASE.cost() + assignedAmount * TRANSPORT.cost()); 
        totalCost += assignedAmount * UNLOADING.cost();
    }

    @Override
    public void delay(int delayTime) {
        startTime     += delayTime;
        dispatchTime  += delayTime;
        arrivalTime   += delayTime;
        endTime       += delayTime;
    }


    public void sendTruck(Truck truck) {
        sendTruck(dispatchTime, Simulator.HOME, truck);
    }
}

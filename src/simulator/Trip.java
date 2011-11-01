package simulator;

import graph.Path;
import stats.Truck;
import static simulator.Times.*;
import static simulator.Costs.*;

public class Trip {
    int startTime;
    int dispatchTime;
    int arrivalTime;
    int endTime;
    private int totalCost;

    Path path;
    private int assignedAmount;

    // only package protected constructor
    Trip (int startTime, Path path, int assignedAmount) {
        this.startTime      = startTime;
        this.path           = path;
        this.assignedAmount    = assignedAmount;
        totalCost           = 0;

        computeTimes();
    }

    private void computeTimes() {
        dispatchTime = startTime     + assignedAmount * LOAD.time();
        arrivalTime  = dispatchTime  + path.pathLength() * 
                                       MINUTES_IN_HOUR.time() / Truck.SPEED;
        endTime      = arrivalTime   + assignedAmount * UNLOAD.time();
    }

    private void computeCost() {
        totalCost += path.pathLength() * 
                     (BASE.cost() + assignedAmount * TRANSPORT.cost()); 
        totalCost += assignedAmount * UNLOADING.cost();
    }

    void delay(int delayTime) {
        startTime     += delayTime;
        dispatchTime  += delayTime;
        arrivalTime   += delayTime;
        endTime       += delayTime;
    }

    int tripCost() {
        return totalCost;
    }
}

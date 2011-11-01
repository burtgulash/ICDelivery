package simulator;

import graph.Path;
import static simulator.Times.*;
import static simulator.Costs.*;

public class Trip {
    int startTime;
    int dispatchTime;
    int arrivalTime;
    int endTime;
    private int totalCost;

    Path path;
    private final int SPEED;
    private int orderedAmount;

    // only package protected constructor
    Trip (int startTime, Path path, int orderedAmount, int truckSpeed) {
        this.startTime      = startTime;
        this.path           = path;
        this.SPEED          = truckSpeed;
        this.orderedAmount  = orderedAmount;
        totalCost           = 0;

        computeTimes();
    }

    private void computeTimes() {
        dispatchTime = startTime     + orderedAmount * LOAD.time();
        arrivalTime  = dispatchTime  + path.pathLength() * 
                                       SPEED / MINUTES_IN_HOUR.time();
        endTime      = arrivalTime   + orderedAmount * UNLOAD.time();
    }

    private void computeCost() {
        totalCost += path.pathLength() * 
                     (BASE.cost() + orderedAmount * TRANSPORT.cost()); 
        totalCost += orderedAmount * UNLOADING.cost();
    }

    void delay(int delayTime) {
        startTime     += delayTime;
        dispatchTime  += delayTime;
        arrivalTime   += delayTime;
        endTime       += delayTime;
    }
    
    boolean arrivesAfterEnd(int terminationTime) {
        return endTime >= terminationTime;
    }

    boolean arrivesBefore(int timeInDay) {
        return endTime % DAY.time() < timeInDay;
    }

    boolean arrivesAfter(int timeInDay) {
        return endTime % DAY.time() > timeInDay;
    }

	int tripCost() {
		return totalCost;
	}
}

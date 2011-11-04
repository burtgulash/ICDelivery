import graph.Path;

import static constant.Times.*;
import static constant.Costs.*;

class ReturnTrip extends Trip {
    ReturnTrip (int startTime, Path toHOME) {
        super(startTime, toHOME);

        computeTimes();
        computeCost();
    }

    @Override
    protected void computeTimes() {
        endTime = startTime + path.pathLength() * 
                              MINUTES_IN_HOUR.time() / Truck.SPEED;
    }

    @Override
    protected void computeCost() {
        totalCost = path.pathLength() * BASE.cost(); 
    }

    @Override
    void delay(int delayTime) {
        startTime += delayTime;
        endTime += delayTime;
    }

    void sendTruck(int customer, Truck truck) {
        sendTruck(startTime, customer, truck);
    }
}

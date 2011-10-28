package stats;

import stats.Order;


public class Truck {
    private static int truckCount = 0;
    private int truckId;

    private Order assigned;
    private int loadedCargo;

    // remaining Path to destination for this truck
    private int currentTown;


    public Truck(Order assigned, int loadedCargo) {
        truckId = ++truckCount;

        this.assigned = assigned;
        this.loadedCargo = loadedCargo;
    }

    
    public Order assignedOrder(){
        return assigned;
    }
    
    public int getId(){
        return truckId;
    }
}

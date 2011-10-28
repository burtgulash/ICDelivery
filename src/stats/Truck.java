package stats;

import stats.Order;


public class Truck {
    public static int MAX_CAPACITY = 10;
    private static int truckCount  = 0;
    private int truckId;

    private Order assigned;
    private int loadedCargo;
    private int currentTown;


    public Truck(Order assigned) {
        truckId = ++truckCount;

        this.assigned = assigned;
        this.loadedCargo = 0;
    }

    
    public Order assignedOrder(){
        return assigned;
    }

    public int loaded() {
        return loadedCargo;
    }

    public void load(int amount) {
        loadedCargo += amount;
    }

    public void unload(int amount) {
        loadedCargo -= amount;
    }
    
    public void setTown(int town) {
        currentTown = town;
    }
    public int currentTown() {
        return currentTown;
    }
    
    public int getId(){
        return truckId;
    }
}

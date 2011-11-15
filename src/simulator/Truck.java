import java.util.List;
import java.util.LinkedList;


/**
 * Truck object abstraction.
 *
 */
public class Truck {
    public static int MAX_CAPACITY = 10;
    public final static int SPEED = 70;
    private static int truckCount  = 0;
    private int truckId;

    // assigned Order list
    private List<Order> assignedOrders;
    // private List<Trip>  completedTrips;
    private int loadedCargo = 0;

    // total cost of actions of this truck after it returns to DEPOT
    private int travelCost = 0;
    private int realCost = 0;
    private int currentTown;
    List<TruckEvent> actions;


    public Truck() {
        truckId = truckCount ++;
        TruckStack.add(this);

        assignedOrders = new LinkedList<Order>();
        actions        = new LinkedList<TruckEvent>();
    }

    public void assignOrder(Order order) {
        assignedOrders.add(order);
    }

    public List<Order> assignedOrders() {
        return assignedOrders;
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

    public void addActionReport(TruckEvent action) {
        actions.add(action);
    }

    public int currentTown() {
        return currentTown;
    }

    public int totalTravelCost() {
        return travelCost;
    }

    public int totalRealCost() {
        return realCost;
    }

    public void updateTravelCost(Trip trip) {
        travelCost += trip.tripCost();
    }

    public void updateRealCost(int cost) {
        realCost += cost;
    }

    public int getId(){
        return truckId;
    }
}

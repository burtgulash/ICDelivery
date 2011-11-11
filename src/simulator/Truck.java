import java.util.List;
import java.util.LinkedList;


public class Truck {
    public static int MAX_CAPACITY = 10;
    public final static int SPEED = 70;
    private static int truckCount  = 0;
    private int truckId;

    // assigned Order list
    private List<Order> assignedOrders;
    // private List<Trip>  completedTrips;
    private int loadedCargo;

    // total cost of actions of this truck after it returns to DEPOT
    private int travelCost;
    private int currentTown;
    private List<TruckEvent> actions;


    public Truck() {
        truckId = ++truckCount;
        TruckStack.add(this);

        assignedOrders = new LinkedList<Order>();
        actions        = new LinkedList<TruckEvent>();
        loadedCargo = 0;
        travelCost  = 0;
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

    public void updateTravelCost(Trip trip) {
        travelCost += trip.tripCost();
    }

    public int getId(){
        return truckId;
    }
}

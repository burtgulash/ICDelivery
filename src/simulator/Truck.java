import java.util.List;
import java.util.LinkedList;


public class Truck {
    public static int MAX_CAPACITY = 10;
	public final static int SPEED = 70;
    private static int truckCount  = 0;
    private int truckId;

	// assigned Order list
    private List<Order> assignedOrders;
    private int loadedCargo;

	// total cost of actions of this truck after it returns to DEPOT
	private int travelCost;
    private int currentTown;


    public Truck() {
        truckId = ++truckCount;

		assignedOrders = new LinkedList<Order>();
        this.loadedCargo = 0;
		this.travelCost  = 0;
    }

	public void assignOrder(Order order) {
		assignedOrders.add(order);
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

	public int travelCost() {
		return travelCost;
	}

	public void setTravelCost(int travelCost) {
		this.travelCost = travelCost;
	}
    
    public int getId(){
        return truckId;
    }
}

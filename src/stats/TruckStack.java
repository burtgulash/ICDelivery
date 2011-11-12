import java.util.Map;
import java.util.TreeMap;


/**
 * Class TruckStack
 *
 * Holds every created truck to be accessed by id
 */
public class TruckStack {
    private static Map<Integer, Truck> allTrucks;

    private TruckStack() {/*,*/}

    public static void init() {
        allTrucks = new TreeMap<Integer, Truck>();
    }

    public static void add(Truck truck) {
        allTrucks.put(truck.getId(), truck);
    }

    public static Truck get(int truckId) {
        Truck ret =  allTrucks.get(truckId);
        assert(ret != null);
        return ret;
    }

    public static int size() {
        return allTrucks.size();
    }

    public static int totalCost() {
        int tot = 0;
        for (Map.Entry KVpair : allTrucks.entrySet()) {
            int truckId = (Integer) KVpair.getKey();
            tot += allTrucks.get(truckId).totalTravelCost();
        }

        return tot;
    }
}

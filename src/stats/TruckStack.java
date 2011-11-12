import java.util.Map;
import java.util.TreeMap;


/**
 * Holds every truck object
 */
class TruckStack {
    private static Map<Integer, Truck> allTrucks;

    private static boolean initialized = false;


    // disable default constructor
    private TruckStack() {/*,*/}



    /**
     * Initialize this object, must be called before 
     * using this class.
     */
    private static void init() {
        initialized = true;
        allTrucks = new TreeMap<Integer, Truck>();
    }


    /**
     * Add created truck to stack.
     *
     * @param truck created Truck object
      */
    static void add(Truck truck) {
        if (!initialized)
            init();
        allTrucks.put(truck.getId(), truck);
    }


    /**
     * Get truck by its id.
     *
     * @param truckId truck id
     * @return Truck object with given id or null if it doesn't exist.
     */
    static Truck get(int truckId) {
        assert allTrucks != null;

        Truck ret =  allTrucks.get(truckId);
        assert ret != null;
        return ret;
    }


    /**
     * Get number of all created trucks.
     *
     * @return Number of all trucks.
     */
    static int size() {
        assert allTrucks != null;

        return allTrucks.size();
    }


    /**
     * Get total cost that has been planned and would be spent
     * given sufficiently long simulation time.
     * (Some trucks might not arrive at HOME before end of simulation)
     *
     * @return Total planned cost of all actions of all trucks.
     */
    static int totalCost() {
        assert allTrucks != null;

        int tot = 0;
        for (Map.Entry KVpair : allTrucks.entrySet()) {
            int truckId = (Integer) KVpair.getKey();
            tot += allTrucks.get(truckId).totalTravelCost();
        }

        return tot;
    }


    /**
     * Get total cost that has been spent so far.
     * Will always be less than or equal to totalCost()
     *
     * @return Total spendings by all trucks so far.
     */
    static int totalRealCost() {
        assert allTrucks != null;

        int tot = 0;
        for (Map.Entry KVpair : allTrucks.entrySet()) {
            int truckId = (Integer) KVpair.getKey();
            tot += allTrucks.get(truckId).totalRealCost();
        }

        return tot;
    }
}

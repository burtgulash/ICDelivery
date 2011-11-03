package constant;

public enum Costs {

    BASE       (5),
    TRANSPORT  (1),
    UNLOADING  (100),
    WAITING    (150);

    private final int cost;

    private Costs(int cost) {
        this.cost = cost;
    }

    public int cost() {
        return cost;
    }
}
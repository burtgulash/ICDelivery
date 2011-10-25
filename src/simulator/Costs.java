package simulator; // put to another package

public enum Costs {

    BASE       (5),
    TRANSPORT  (1),
    UNLOAD     (100),
    WAITING    (150);

	private final int cost;

	private Costs(int cost) {
		this.cost = cost;
	}

	public int cost() {
		return cost;
	}
}

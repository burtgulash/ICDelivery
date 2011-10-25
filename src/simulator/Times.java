package simulator; // change later

public enum Times {
    MIN_ACCEPT_TIME (360),
    MAX_ACCEPT_TIME (1080),
    DAY             (1440),

    LOADING_TIME    (15),
    UNLOADING_TIME  (15);

	private final int minutes;
	private Times (int minutes) {
		this.minutes = minutes;
	}

	public int minutes() {
		return minutes;
	}
}

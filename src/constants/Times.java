package constant;

/**
 * Enumerates all time related constants needed for truck scheduling.
 */
public enum Times {

    MINUTES_IN_HOUR (60),

    MIN_ACCEPT (360),
    MAX_ACCEPT (1080),
    DAY        (1440),

    LOAD    (15),
    UNLOAD  (15);

    private final int time;
    private Times (int time) {
        this.time = time;
    }

    public int time() {
        return time;
    }
}

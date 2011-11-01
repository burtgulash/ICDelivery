package stats;

import static simulator.Times.*;

public class Logger {
	private Logger() {/*,*/}

	public static void log(int time, String message) {
		System.out.printf("%s |  %s\n", ascTime(time), message);
	}

	private static String ascTime(int time) {
		int days   = time / DAY.time() + 1;
		int hours  = (time % DAY.time()) / MINUTES_IN_HOUR.time();
		int mins   = (time % DAY.time()) % MINUTES_IN_HOUR.time();;
		return String.format("Day %2d  %02d:%02d", days, hours, mins);
	}
}

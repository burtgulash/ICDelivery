package stats;

import java.io.PrintWriter;
import java.io.OutputStream;
import java.util.List;
import java.util.LinkedList;

import static simulator.Times.*;

public class Logger {
    private static List<PrintWriter> writers;
    private Logger() {/*,*/}

    public static void init () {
        writers = new LinkedList<PrintWriter>();
    }

    public static void addOutput(OutputStream out) {
        writers.add(new PrintWriter(out, true));
    }

    public static void log(int time, String message) {
        for (PrintWriter writer : writers) 
            writer.println(String.format("%s | %s", ascTime(time), message));
    }

    private static String ascTime(int time) {
        int days   = time / DAY.time() + 1;
        int hours  = (time % DAY.time()) / MINUTES_IN_HOUR.time();
        int mins   = (time % DAY.time()) % MINUTES_IN_HOUR.time();;
        return String.format("Day %1d  %02d:%02d", days, hours, mins);
    }
}

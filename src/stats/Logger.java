import static constant.Times.*;

import java.io.PrintWriter;
import java.io.OutputStream;
import java.util.List;
import java.util.LinkedList;


/**
 * Logger is responsible for logging all events that occured 
 * in simulation to all provided output files.
 */
class Logger {
    private static List<PrintWriter> writers;
    // disable constructor, keep class static
    private Logger() {/*,*/}

    private static boolean initialized = false;


    /**
     * Static class constructor. Must be called before
     * any call to Logger.
     */
    private static void init () {
        initialized = true;
        writers = new LinkedList<PrintWriter>();
    }


    /**
     * Add output file to be logged to.
     *
     * @param out Output file.
     */
    static void addOutput(OutputStream out) {
        if (!initialized)
            init();

        writers.add(new PrintWriter(out, true));
    }


    /**
     * Logs current event to all output files.
     * Gets called by Calendar after each new discovered event.
     * 
     * @param time Time of occurence of event
     * @param message Message containing log information
     */
    static void log(int time, String message) {
        assert writers != null;

        for (PrintWriter writer : writers) 
            writer.println(String.format("%s | %s", 
                                       TimeConverter.ascTime(time), message));
    }
}

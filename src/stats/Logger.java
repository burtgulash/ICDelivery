import static constant.Times.*;

import java.io.PrintWriter;
import java.io.OutputStream;
import java.util.List;
import java.util.LinkedList;


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
            writer.println(String.format("%s | %s", 
                                       Calendar.ascTime(time), message));
    }
}

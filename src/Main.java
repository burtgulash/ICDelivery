import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import graph.Graph;
import graph.GraphLoader;


public class Main {

    public static void main(String[] args) {
        Parser p = new Parser(
           "Pan Zmrzlik, syn a vnukove - diskretni simulace rozvozu zmrzliny", 
           "usage: java -jar sim.jar [OPTION]... [ORDER]...");

        p.addBooleanOption("quiet", "q", "don't print to screen");
        p.addIntegerOption("initial", "i", "number of initial orders", 300);
        p.addIntegerOption("mean", "m", "mean of interval between orders", 10);
        p.addIntegerOption("max", "M", 
                     "amount that can be ordered by one customer", 5);

        p.addStringOption("time", "t", "total <TIME> of simulation",
                                                                   "05:00:00");
        p.addStringOption("pause", "p", "<TIME> of first pause", null);
        p.addStringOption("log", "l", "output file for log", "");
        p.addStringOption("report", "r", "output file for final report");
        p.addStringOption("graph", "g", "input file containing graph", 
                                                       "test.graph");
        p.addStringOption("strategy", "s", "greedy or clarkewright", "greedy");

        p.addParagraph(TimeConverter.TIME_HELP);


        String[] orders = loadArgs(p, args);


        int HOME = 0;

        boolean quiet        = (Boolean) p.getValue("quiet");
        int startOrderCount  = (Integer) p.getValue("initial"); 
        int orderMean        = (Integer) p.getValue("mean");
        int maxTonsPerOrder  = (Integer) p.getValue("max");
        String termination   = (String) p.getValue("time");
        String pause         = (String) p.getValue("pause"); 
        String graphFile     = (String) p.getValue("graph");
        String outFile       = (String) p.getValue("output");
        String strategy      = (String) p.getValue("strategy");

        // set pause after termination time if it is not specified

        int terminationTime, pauseTime;
        terminationTime = TimeConverter.toMinutes(0, termination);
        if (terminationTime == TimeConverter.NIL) {
            System.err.println("Invalid simulation time");
            System.exit(1);
        }    


        if (pause == null)
            pauseTime = terminationTime + 1;
        else {
            pauseTime = TimeConverter.toMinutes(0, pause);
            if (pauseTime == TimeConverter.NIL) {
                System.err.println("Invalid pause time");
                pauseTime = terminationTime + 1;
            }
        }

        // open output file
        OutputStream file = openOutFile(outFile);

        Graph graph = GraphLoader.getGraph(graphFile);
        if (graph == null)
            System.exit(1);


        if (strategy.matches("[Gg](?:reedy)?"))
            strategy = "greedy";
        else if (strategy.matches("[Cc](?:larke)?[Ww](?:right)?"))
            strategy = "clarkewright";
        else {
            System.err.println("Unknown strategy: " + strategy);
            System.exit(1);
        }


        Initializer.initSimulation(graph, HOME, terminationTime, pauseTime, 
                                   orderMean, startOrderCount, orders, 
                                   maxTonsPerOrder, quiet, file, strategy);

        // run the simulation
        Simulator.mainLoop();
        // end the simulation


        // print end summary
        System.out.println();
        TruckStack.summary();
        CustomerList.summary();

        System.exit(0);
    }

    private static OutputStream openOutFile(String outFile) {
        OutputStream file = null;
        try {
            if (outFile != null && !outFile.equals(""))
                file = new FileOutputStream(outFile);
        } catch (FileNotFoundException fnf) {
            System.err.printf("File %s can't be opened for writing%n", outFile);
            System.exit(1);
        } catch (SecurityException sex) {
            System.err.printf("Don't have permission to write to %s%n",outFile);
            System.exit(1);
        }

        return file;
    }

    private static String[] loadArgs(Parser p, String [] args) {
        String[] leftOvers = null;
        try {
            leftOvers = p.parse(args);
        } catch(Parser.MissingValueException ex) {
            System.err.println("Missing value after " + ex.arg);
            System.exit(1);
        } catch(Parser.UnknownArgumentException ex) {
            System.err.println("Invalid argument: " + ex.arg);
            System.exit(1);
        } catch (NumberFormatException ex) {
            System.err.println("Number parsing error");
            System.exit(1);
        }

        return leftOvers;
    }
}

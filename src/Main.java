import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import graph.Graph;
import graph.GraphLoader;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Main {

    public static void main(String[] args) {
        Parser p = new Parser(
           "Pan Zmrzlik, syn a vnukove - diskretni simulace rozvozu zmrzliny", 
           "usage: java -jar sim.jar [OPTIONS...] GRAPHFILE");

        p.addBooleanOption("quiet", "q", "don't print to screen");
        p.addIntegerOption("time", "t", "total time of simulation", 7200);
        p.addIntegerOption("initial", "i", "number of initial orders", 300);
        p.addIntegerOption("pause", "p", "time of first pause", -1);
        p.addIntegerOption("mean", "m", "mean of interval between orders", 10);
        p.addIntegerOption("max", "M", 
                     "amount that can be ordered by one customer", 5);

        p.addStringOption("output", "o", "output file for log", "");
        p.addStringOption("graph", "g", "input file containing graph", 
                                                       "test.graph");
        String[] arg = null;
        try {
            arg = p.parse(args);
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


        int HOME = 0;

        boolean quiet        = (Boolean) p.getValue("quiet");
        int simTime          = (Integer) p.getValue("time");
        int pauseTime        = (Integer) p.getValue("pause"); 
        int startOrderCount  = (Integer) p.getValue("initial"); 
        int orderMean        = (Integer) p.getValue("mean");
        int maxTonsPerOrder  = (Integer) p.getValue("max");
        String graphFile     = (String) p.getValue("graph");
        String outFile       = (String) p.getValue("output");

        if (arg.length >= 1)
            graphFile = arg[0];

        if (pauseTime < 0)
            pauseTime = simTime + 1;

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

        Graph graph = GraphLoader.getGraph(graphFile);
        if (graph == null)
            System.exit(1);

        Initializer.initSimulation(graph,
                                   HOME,
                                   simTime, 
                                   pauseTime,
                                   orderMean,
                                   startOrderCount,
                                   maxTonsPerOrder,
                                   quiet,
                                   file);

        Simulator.mainLoop();

		System.out.println();
		TruckStack.summary();
		CustomerList.summary();
    }
}

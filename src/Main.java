import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;

import graph.Graph;
import graph.Loader;


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
        p.addStringOption("pause", "p", "<TIME> of first pause");
        p.addStringOption("log", "l", "output file for log");
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
        String logFileName   = (String) p.getValue("log");
        String strategy      = (String) p.getValue("strategy");
        String reportDir     = (String) p.getValue("report");

        int terminationTime;
        terminationTime = TimeConverter.toMinutes(0, termination);
        if (terminationTime == TimeConverter.NIL) {
            System.err.println("Invalid simulation time");
            System.exit(1);
        }    

        // open output file
        OutputStream logFile = null;
        if (logFileName != null) {
            logFile = openLogFile(logFileName);
            if (logFile == null)
                System.exit(1);
        }

        // open report files
        OutputStream[] reportFiles   = null;
        OutputStream customerReport  = null;
        OutputStream orderReport     = null;
        OutputStream truckReport     = null;

        if (reportDir != null) {
            reportFiles = openReportFiles(reportDir);
            if (reportFiles == null)
                System.exit(1);

            customerReport  = reportFiles[0];
            orderReport     = reportFiles[1];
            truckReport     = reportFiles[2];
        }

        Graph graph = Loader.getGraph(graphFile);
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


        Initializer.initSimulation(graph, HOME, terminationTime, pause, 
                                   orderMean, startOrderCount, orders, 
                                   maxTonsPerOrder, quiet, logFile, reportFiles,
                                   strategy);

        // run the simulation
        Simulator.mainLoop();
        // end the simulation


        // print report
        if (customerReport != null)
            Reporter.printCustomerReport(customerReport);
        if (orderReport != null)
            Reporter.printOrderReport(orderReport);
        if (truckReport != null)
            Reporter.printTruckReport(truckReport);


        // print end summary
        System.out.println();
        System.out.println(Reporter.truckSummary());
        System.out.println(Reporter.customerSummary());

        if (closeFiles(reportFiles) && closeFiles(logFile))
            System.exit(0);
        else
            System.exit(1);
    }


    private static boolean closeFiles(OutputStream... files) {
        boolean success = true;
        if (files == null)
            return success;
        for (OutputStream file : files) {
            try {
                if (file != null) {
                    file.flush();
                    file.close();
                }
            } catch (IOException ex) {
                System.err.println("Error closing file");
                success = false;
            }
        }
        return success;
    }


    private static OutputStream[] openReportFiles(String reportDir) {
        File reportDirectory = new File(reportDir);
        try {
            if (!reportDirectory.exists())
                reportDirectory.mkdirs();
            if (!reportDirectory.isDirectory()) {
                System.err.println(
                     "Report directory already exists and is not directory");
                return null;
            }
        } catch (SecurityException sex) {
            System.err.println("Don't have permissions to write to directory "
                               + reportDir);
            return null;
        }


        File customerReport  = new File(reportDirectory, "customers.txt");
        File orderReport     = new File(reportDirectory, "orders.txt");
        File truckReport     = new File(reportDirectory, "trucks.txt");

        OutputStream[] statFiles = new OutputStream[3];
        try {
            statFiles[0] = new FileOutputStream(customerReport);
            statFiles[1] = new FileOutputStream(orderReport);
            statFiles[2] = new FileOutputStream(truckReport);
        } catch (FileNotFoundException fnf) {
            System.err.println("Can not open report file for writing");
            return null;
        } catch (SecurityException sex) {
            System.err.println(
                     "Don't have permissions to write to report file");
            return null;
        }

        return statFiles;
    }


    private static OutputStream openLogFile(String logFileName) {
        OutputStream file = null;
        try {
            if (logFileName != null && !logFileName.equals(""))
                file = new FileOutputStream(logFileName);
        } catch (FileNotFoundException fnf) {
            System.err.printf(
                   "File %s can't be opened for writing%n", logFileName);
            System.exit(1);
        } catch (SecurityException sex) {
            System.err.printf(
                   "Don't have permission to write to %s%n",logFileName);
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

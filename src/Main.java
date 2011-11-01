import graph.GraphLoader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import simulator.Initializer;
import simulator.Simulator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Main {

    public static void main(String[] args) {
        try {
            parse(args);
        } catch(MissingValueException ex) {
            System.err.println("Missing value after " + ex.arg);
            System.exit(1);
        } catch(InvalidArgumentException ex) {
            System.err.println("Invalid argument: " + ex.invalidArg);
            System.exit(1);
        } catch (NumberFormatException ex) {
            System.err.println("Argument parsing error");
            System.exit(1);
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("Missing argument");
            System.exit(1);
        }

        if (pauseTime < 0)
            pauseTime = simTime + 1;

        OutputStream file = null;
        try {
            if (outFile != null)
                file = new FileOutputStream(outFile);
        } catch (FileNotFoundException fnf) {
            System.err.printf("File %s can't be opened for writing%n", outFile);
            System.exit(1);
        } catch (SecurityException sex) {
            System.err.printf("Don't have permission to write to %s%n",outFile);
            System.exit(1);
        }

        Initializer.initSimulation(GraphLoader.getGraph(graphFile),
                                   HOME,
                                   simTime, 
                                   pauseTime,
                                   orderMean,
                                   startOrderCount,
                                   maxTonsPerOrder,
                                   beQuiet,
                                   file);

        Simulator.mainLoop();
    }




    static final String NL = String.format("%n");
    static final int HOME = 0;

    static int simTime = 7200;
    static int pauseTime = -1;
    static int startOrderCount = 0;
    static String graphFile = "test.graph";
    static String outFile = null;
    static boolean beQuiet = false;
    static int orderMean  = 10;
    static int maxTonsPerOrder = 5;
    
    public static final String HELP = 
"Pan Zmrzlik, syn a vnukove - diskretni simulace rozvozu zmrzliny" + NL + NL +
"Usage: main [OPTIONS...] GRAPHFILE" + NL +
"  -h | --help        <- print help" + NL + 
"  -q | --quiet       <- don't print to screen" + NL +
"  -t | --time        <- total time of simulation" + NL +
"  -i | --initial     <- number of initial orders" + NL +
"  -p | --pause       <- time of first pause" + NL +
"  -o | --output      <- output file for log" + NL +
"  -g | --graph       <- input  file containing graph" + NL +
"  -m | --mean        <- mean of interval between orders" + NL +
"  -M | --max         <- maximal amount that can be ordered by one customer";

    
    private static int DEPOT = 0;

    static void parse(String[] argv) 
                     throws IndexOutOfBoundsException, 
                            NumberFormatException, 
                            InvalidArgumentException,
                            MissingValueException
    {
        String tmp = "";
        Matcher m;
        String help     = "-h|--help";
        String quiet    = "-q|--quiet";
        String orders   = "(?:-i|--initial=)(\\d*)|--initial";
        String time     = "(?:-t|--time=)(\\d*)|--time";
        String pause    = "(?:-p|--pause=)(\\d*)|--pause";
        String mean     = "(?:-m|--mean=)(\\d*)|--mean";
        String max      = "(?:-M|--max=)(\\d*)|--max";
        String out      = "--output=(\\S+)|-o|--output";
        String graph    = "--graph=(\\S+)|-g|--graph";


        for (int i = 0; i < argv.length; i++) {
            if (!argv[i].startsWith("-")) {
                graphFile = argv[i];
            } 
            else {
                if (argv[i].matches(help)) {
                    System.out.println(HELP);
                    System.exit(1);
                }

                else if (argv[i].matches(orders)) {
                    m = Pattern.compile(orders).matcher(argv[i]);
                    m.find();

                    tmp = m.group(1);
                    if (tmp == null || tmp.equals("")) {
                        if (argv[i].endsWith("="))
                            throw new MissingValueException(argv[i]);
                        tmp = argv[++i]; 
                    }
                    startOrderCount = Integer.parseInt(tmp);
                }

                else if (argv[i].matches(mean)) {
                    m = Pattern.compile(mean).matcher(argv[i]);
                    m.find();

                    tmp = m.group(1);
                    if (tmp == null || tmp.equals("")) {
                        if (argv[i].endsWith("="))
                            throw new MissingValueException(argv[i]);
                        tmp = argv[++i];
                    }
                    orderMean = Integer.parseInt(tmp);
                }

                else if (argv[i].matches(max)) {
                    m = Pattern.compile(max).matcher(argv[i]);
                    m.find();

                    tmp = m.group(1);
                    if (tmp == null || tmp.equals("")) {
                        if (argv[i].endsWith("="))
                            throw new MissingValueException(argv[i]);
                        tmp = argv[++i];
                    }
                    maxTonsPerOrder = Integer.parseInt(tmp);
                }

                else if (argv[i].matches(pause)) {
                    m = Pattern.compile(pause).matcher(argv[i]);
                    m.find();

                    tmp = m.group(1);
                    if (tmp == null || tmp.equals("")) {
                        if (argv[i].endsWith("="))
                            throw new MissingValueException(argv[i]);
                        tmp = argv[++i];
                    }
                    pauseTime = Integer.parseInt(tmp);
                }

                else if (argv[i].matches(time)) {
                    m = Pattern.compile(time).matcher(argv[i]);
                    m.find();

                    tmp = m.group(1);
                    if (tmp == null || tmp.equals("")) {
                        if (argv[i].endsWith("="))
                            throw new MissingValueException(argv[i]);
                        tmp = argv[++i];
                    }
                    simTime = Integer.parseInt(tmp);
                }

                else if (argv[i].matches(quiet)) {
                    beQuiet = true;
                }

                else if (argv[i].matches(out)) {
                    m = Pattern.compile(out).matcher(argv[i]);
                    m.find();

                    outFile = m.group(1);
                    if (outFile == null || outFile.equals("")) {
                        if (argv[i].endsWith("="))
                            throw new MissingValueException(argv[i]);
                        outFile = argv[++i];
                    }
                }

                else if (argv[i].matches(graph)) {
                    m = Pattern.compile(graph).matcher(argv[i]);
                    m.find();

                    graphFile = m.group(1);
                    if (graphFile == null || graphFile.equals("")) {
                        if (argv[i].endsWith("="))
                            throw new MissingValueException(argv[i]);
                        graphFile = argv[++i];
                    }
                }

                else {
                    throw new InvalidArgumentException(argv[i]);
                }
            }
        }
    }

    // debugging
    private static void printConfig() {
        System.out.println("quiet    :   " + beQuiet);    
        System.out.println("time     :   " + simTime);    
        System.out.println("initial  :   " + startOrderCount);    
        System.out.println("pause    :   " + pauseTime);    
        System.out.println("output   :   " + outFile);    
        System.out.println("graph    :   " + graphFile);    
        System.out.println("mean     :   " + orderMean);    
        System.out.println("max      :   " + maxTonsPerOrder);    
    }


    
}

class MissingValueException extends Exception {
    final String arg;
    MissingValueException(String arg) {
        super();
        this.arg = arg;
    }
}

class InvalidArgumentException extends Exception {
    final String invalidArg;
    InvalidArgumentException(String arg) {
        super();
        invalidArg = arg;
    }
}

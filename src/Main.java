import graph.GraphLoader;
import simulator.Initializer;
import simulator.Simulator;


public class Main {
    
    public static final String HELP = "Pan Zmrzlik, syn a vnukove - diskretni simulace rozvozu zmrzliny\n" +
            "Usage: main <options>\n"+
            "where options include:\n"+
            "-p <minutes>\t\t Sets time when the simulation will be paused\n"+
            "-h \t\t\t Displays this help message\n"+
            "-n <number> \t\t Sets number of orders generated on start of simulation\n";
    public static int simTime;
    public static int pauseTime;
    public static int startOrderCount;
    public static String fileName;
    public static String logFile;
    
    private static int DEPOT = 0;

    public static void main(String[] args) {
        loadParams();
        parseCmdArgs(args);


        int orderMean = 300;
        int maxTonsPerOrder = 5;

		
        Initializer.initSimulation(GraphLoader.getGraph(fileName), 
                                   DEPOT,
                                   simTime,
                                   pauseTime, 
                                   orderMean, 
                                   startOrderCount,
                                   maxTonsPerOrder);

        Simulator.mainLoop();
    }
    
    /**
     * 
     * Parses command line arguments and sets options of simulation
     * 
     * @param args command line arguments
     */
    

    // ty kravo begin
    static void parseCmdArgs(String[] args){
        if (args.length > 0){
          for (int i = 0; i < args.length; i++){
               if(args[i].matches("-[phn]")){
                switch (args[i].charAt(1)){
                  case 'p': if (i+1 < args.length){
                            pauseTime = Integer.parseInt(args[++i]);
                            }else System.out.println("Bad argument! Expected number after flag -p");
                            break;
                  case 'h': System.out.println(HELP); break;
                  case 'n': if (i+1 < args.length){
                            startOrderCount = Integer.parseInt(args[++i]);
                            } else System.out.println("Bad argument! Expected number after flag -n");
                            break;
                  default:System.out.println("Unrecognized switch \"-" + args[i].charAt(1) +"\"!"  );
                }
            }else
                System.out.println("Unrecognized argument \"" + args[i] +"\"!"  );
            }
        }
        else{
            // System.out.println(HELP);
        }
        
    }
     
    
    // ty kravo end


    //TODO
    // decide on format of pause-time input, in case of simple minute format this method can be removed
    static int setPauseTime(String time) throws NumberFormatException{
        int t = 0;
        

        for(int i = 0; i < time.length();i++){
            if (time.charAt(i) > 97 && time.charAt(i) < 110 ){
                switch (time.charAt(i)){
                case 'd': t += Integer.parseInt(time.substring(0, i))*1440;
                
                            time = time.substring(i+1);
                            i = 0;
                            break;
                case 'h':t += Integer.parseInt(time.substring(0, i))*60;
                            
                            time = time.substring(i+1);
                            i = 0;
                            break;
                case 'm':t += Integer.parseInt(time.substring(0, i));
                            
                            time = time.substring(i);
                            i = 0;
                            break;
                default: break;
                }
            }
        }
        return t;
    }
    
    static void loadParams(){
        Config.readConfig();
        simTime = Config.getSimTime();
        pauseTime = Config.getPauseTime();
        startOrderCount = Config.getStartOrderCount();
        fileName = Config.getgraphFileName();
        logFile = Config.getLogFileName();

    }
}


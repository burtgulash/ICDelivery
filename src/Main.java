import simulator.EventHandler;

/**
 * 
 * Discreet simulation 
 * 
 *
 */

public class Main {
	
	public static final String HELP = "Pan Zmrzlik, syn a vnukove - diskretni simulace rozvozu zmrzliny\n" +
			"Usage: main <options>\n"+
			"where options include:\n"+
			"-p <minutes>\t\t Sets time when the simulation will be paused\n"+
			"-h \t\t\t\t Displays this help message\n"+
			"-n <number> \t\t Sets number of orders generated on start of simulation";
	public static final int SIM_TIME = 7200;
	public static int pauseTime = SIM_TIME;
	public static int startOrderCount = 150;
	public static EventHandler simulation;
	

	public static void main(String[] args) {
	
		parseCmdArgs(args);
		simulation = new EventHandler(SIM_TIME,pauseTime,startOrderCount);
		
	
		
	}
	
	/**
	 * 
	 * Parses command line arguments and sets options of simulation
	 * 
	 * @param args command line arguments
	 */
	
	static void parseCmdArgs(String[] args){
		
		if (args.length > 0){
			char option;
			
			for (int i = 0; i < args.length; i++){
				option = args[i].charAt(1);
				
				switch (option){
					case 'p': pauseTime = Integer.parseInt(args[i+1]); break;
					case 'h': System.out.println(HELP); break;
					case 'n': startOrderCount = Integer.parseInt(args[i+1]); break;
				}
			}
		}
		else{
			System.out.println(HELP);
		}
		
	}
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

}

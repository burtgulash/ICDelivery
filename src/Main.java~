public class Main {
	
	public static final String HELP = "Pan Zmrzlík, syn a vnukové - diskretni simulace rozvozu zmrzliny\n" +
			"";
	public static final int SIM_TIME = 7200;
	public static int pauseTime = SIM_TIME;
	public static int startOrderCount;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
	
		parseCmdArgs(args);
		
	
		
	}
	
	static void parseCmdArgs(String[] args){
		if (args.length > 0){
			
			char option;
			
			for (int i = 0; i < args.length; i++){
				option = args[i].charAt(1);
				
				switch (option){
					case 'p': pauseTime = setPauseTime(args[i+1]); break;
					case 'h': System.out.println(HELP); break;
					case 'n': startOrderCount = Integer.parseInt(args[i+1]); break;
				}
			}
		}
		else{
			System.out.println("\n");
		}
		
	}
	
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

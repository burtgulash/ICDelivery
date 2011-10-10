import java.io.IOException;


public class Main {
	
	public static final String HELP = "Pan Zmrzlík, syn a vnukové - diskretni simulace rozvozu zmrzliny\n" +
			"";
	public static final int TOTAL_TIME = 7200;
	
	public static int pauseTime = TOTAL_TIME;
	public static int currentTime = 0;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		boolean simRunning = true;
				
		parseCmdArgs(args);
		LogWriter lw = new LogWriter(true);
		JSONLoader jl = new JSONLoader();
		
		lw.writeLog(currentTime,"test");
		while (simRunning){
			
			//System.out.println(currentTime/1440 + ". den " + (currentTime%1440)/60 + ":" + (currentTime%1440)%60);
			lw.writeLog(currentTime,currentTime/1440 + ". den " + (currentTime%1440)/60 + ":" + (currentTime%1440)%60);
			
			if(currentTime == pauseTime || currentTime == TOTAL_TIME)
				simRunning = false;
			currentTime++;
		}
		
	lw.closeLog();
		
	}
	
	static void parseCmdArgs(String[] args){
		if (args.length > 0){
			
			char option;
			
			for (int i = 0; i < args.length; i++){
				option = args[i].charAt(1);
				
				switch (option){
					case 'p': pauseTime = setPauseTime(args[i+1]); break;
					case 'h': System.out.println(HELP); break;
					case 't': System.out.println("zapis do souboru"); break;
				}
			}
		}
		else{
			System.out.println("Nebyly zadany zadne argumenty programu, simulace je spustena s vychozimi hodnotami.\n");
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

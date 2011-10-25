package stats;

public class Logger {
	
	private static Logger onlyRef;
	Formatter output;
	
	
	public static Logger getLoggerObject(String fileName) {
		if (onlyRef == null)
			onlyRef = new Logger(fileName);
		return onlyRef;
	}
	
	public Logger(String fileName){
		output = new PlainFT(fileName);
		output.writeHead();
		
	}
	
	public void note(LogEntry le){
		System.out.println(timeFormat(le.time) +" - "+ le.text);
		output.write(le);
	}
	
	private String timeFormat(int time){
		return time/1440 + " - "+ (time%1440)/60 + ":"+ ((time%1440)%60);
	}
	
	public void closeLog(){
		output.writeFoot();
	}
}

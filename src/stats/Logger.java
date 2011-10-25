package stats;

public class Logger {
	
	Formatter output = new PlainFT("log.txt");
	
	public Logger(){
		
	}
	
	public void note(LogEntry le){
		output.write(le);
		System.out.println(le.time +" - "+ le.text);
	}
	
	public void closeLog(){
		output.writeFoot();
	}
}

package stats;

import java.io.IOException;


public class PlainFT extends Formatter {
	
	public PlainFT(String fileName){
		super(fileName,FileType.PLAIN);
	}
	
	public void writeHead(){
		try {
			bfr.write("Plain log file ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(LogEntry le){
		try {
			bfr.write(le.time +" - "+ le.text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeFoot(){
		try {
			bfr.write("END");
			endFileWrite();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

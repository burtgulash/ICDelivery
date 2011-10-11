import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



public class LogWriter {
	
	final private File LOG = new File("log.txt");
	private BufferedWriter bfr;
	private StringBuffer sb = new StringBuffer();
	boolean toFile = false;
	
	LogWriter(boolean toFile){
		if (toFile){
			this.toFile = toFile;
			try {
				if (!(LOG.isFile())){
					System.out.println("Vytvářím nový soubor log.txt");
					LOG.createNewFile();
					
				}
				bfr = new BufferedWriter(new FileWriter(LOG));
				bfr.write("Zaznam simulace\n");
			}
			catch (IOException e)  {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void writeLog(int time,String entry){
		int timeOld = 0;
		
		if (time == timeOld){
			sb.append(entry + "\n");
		}
		else{
			
			System.out.print(sb);
			if(toFile){
				try {
					bfr.append(sb);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			sb.setLength(0);
			timeOld = time;
			sb.append(entry + "\n");
		}
		
	}
	
	public void closeLog(){
		if(toFile){ 
			try {
				bfr.append(sb);
				bfr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

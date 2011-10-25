import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class Config {
	private static final File CFG = new File("sim.conf");
	private static BufferedReader br;
	
    private static int simTime = 7200;
    private static int pauseTime = 7200;
    private static int startOrderCount = 100;
    private static String graphFile = "test.graph";
    private static String logFile = "log.txt";
    
    public static int getSimTime() {
		return simTime;
	}

	public static int getPauseTime() {
		return pauseTime;
	}

	public static int getStartOrderCount() {
		return startOrderCount;
	}

	public static String getgraphFileName() {
		return graphFile;
	}

	public static String getLogFileName() {
		return logFile;
	}

	
	
	public static void readConfig(){
		if(CFG.canRead()){
			try {
				br = new BufferedReader(new FileReader(CFG));
				parseConf();
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void parseConf(){
		String s;
		String[] tmp = new String [2]; 
		try {
			while ((s = br.readLine()) != null){
				tmp=s.split("=");
				if(tmp[0].matches("simTime"))simTime=Integer.parseInt(tmp[1]);
				if(tmp[0].matches("pauseTime"))pauseTime=Integer.parseInt(tmp[1]);
				if(tmp[0].matches("startOrderCount"))startOrderCount=Integer.parseInt(tmp[1]);
				if(tmp[0].matches("graphFile"))graphFile=tmp[1];
				if(tmp[0].matches("logFile"))logFile=tmp[1];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}

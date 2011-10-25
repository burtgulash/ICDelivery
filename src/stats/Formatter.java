package stats;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

abstract class Formatter {
	
	final FileType type;
	BufferedWriter bfr;
	File file;
	
	public Formatter(String fileName, FileType type){
		this.type = type;
		this.file = new File(fileName);
		initFileWrite();
	}
	
	private void initFileWrite(){
		try {
			if (!(file.isFile()))
				file.createNewFile();
			bfr = new BufferedWriter(new FileWriter(file));
		}
		catch (IOException e)  {
			e.printStackTrace();
		}
	}
	
	abstract void writeHead();
	abstract void writeFoot();
	abstract void write(LogEntry le);
		
	
	void endFileWrite() throws IOException{
			bfr.close();
	}
		
}
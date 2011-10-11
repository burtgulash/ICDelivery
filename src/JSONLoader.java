import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class JSONLoader {
	final private File GRAPHFILE = new File("graph.json"); // Soubor s definici grafu
	private BufferedReader br;
	Graph g;
	int max = 0;
	
	
	public JSONLoader(){
		try {
			br = new BufferedReader(new FileReader(GRAPHFILE));
			loadGraph();
		} catch (IOException e) {
			System.out.println("Chyba při čtení souboru grafu!");
			e.printStackTrace();
		}		
	}
	
	/**
	 * Metoda nacte ze souboru data o grafu v JSON formatu
	 * a rozdeli data na jednotlive retezce pro kazdy vrchol
	 * @throws IOException
	 */
	
	private void loadGraph() throws IOException{
		
		String s = "";
		String[] vString = null;
		StringBuilder sb = new StringBuilder();
		
		while ((s = br.readLine()) != null){
			sb.append(s);
		}	
		s = sb.toString();
		s = s.substring(2,s.length()-2);
		vString = s.split(", '");
		
		g = new Graph(vString.length);
		
		for (int i = 0; i < vString.length;i++){
			parseVString(vString[i]);
		}
	}
	
	/**
	 * 
	 * 
	 * @param vString
	 */
	
	private void parseVString(String vString){
		vString = vString.substring(0,vString.length());
		
		String[] temp = vString.substring(0,vString.length()-2).split("': \\[\\(");
		
		int vertexNumber = Integer.parseInt(temp[0]);
		
		temp = temp[1].split("\\), \\(");
		for (int i = 0; i < temp.length; i++){
			g.addEdge(vertexNumber, parseEString(temp[i]));			
		}
		
		
	}
	 /**
	  * 
	  * @param s
	  * @return
	  */
	
	private Edge parseEString(String s){
		String[] edgeData = s.substring(1).split("', ");
		return new Edge(Integer.parseInt(edgeData[0]), Integer.parseInt(edgeData[1]));
	}
}

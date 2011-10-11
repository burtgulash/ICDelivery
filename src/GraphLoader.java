import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GraphLoader {
	final private File GRAPHFILE = new File("test.graph"); // Soubor s definici grafu
	private BufferedReader br;
	private final int MAX_EDGES = 500;
	private Edge[][] v;
	
	
	
	public GraphLoader(){
		try {
			br = new BufferedReader(new FileReader(GRAPHFILE));
			parseGraphFile(loadGraphFile());
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
	
	private List<String> loadGraphFile() throws IOException{
		List<String> nodeList = new ArrayList<String>();
		String s;	
		
		while ((s = br.readLine()) != null){
			nodeList.add(s);
		}	
		
		return nodeList;	
	}
	

	
	private void parseGraphFile(List<String> nodeList){
		
		v = new Edge[nodeList.size()][];
		Iterator<String> i = nodeList.iterator();
		Pattern vertex = Pattern.compile("\\d+\\s");
		Matcher mVertex;
		int vertexNumber = 0;
		String s;		
		
				
		while (i.hasNext()){
			s = i.next();
			mVertex = vertex.matcher(s);
			
			if (mVertex.find()){
				vertexNumber = Integer.parseInt(mVertex.group().trim());
			}		
			
			v[vertexNumber] = parseEdgeList(s);
					
		}
		
		
	}
	
	private Edge parseEdge(String s){
		String[] tmp = s.split(":");
		return new Edge(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]));
	}	
	
	private Edge[] parseEdgeList(String s){
		Edge [] edgeList = new Edge[MAX_EDGES];
		Pattern edges = Pattern.compile("\\d+:\\d+");
		Matcher mEdges = edges.matcher(s);
		int count = 0;
		
		while (mEdges.find()){
			edgeList[count++] = parseEdge(mEdges.group());
		}
				
		return Arrays.copyOf(edgeList, count);
	}
		
	
	public Graph getGraph(){
		return new Graph(v);	
	}
	
}
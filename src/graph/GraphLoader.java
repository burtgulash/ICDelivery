package graph;


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
	private static File graphFile;
	private static BufferedReader br;
	private final static int MAX_EDGES = 500;
	private static Edge[][] v;
	
	
	
	/**
	 * 
	 * Loads graph definition from CSS-like formated file, 
	 * @throws IOException
	 */
	
	private static List<String> loadGraphFile() throws IOException{
		List<String> nodeList = new ArrayList<String>();
		String s;	
		
		while ((s = br.readLine()) != null){
			nodeList.add(s);
		}	
		
		return nodeList;	
	}
	

	
	private static void parseGraphFile(List<String> nodeList){
		
		v = new Edge[nodeList.size()][];
		Iterator<String> i = nodeList.iterator();
		Pattern vertex = Pattern.compile("\\d+\\s");
		Matcher mVertex;
		int vertexNumber = 0;
		String s;		
		
				
		while (i.hasNext()){
			s = i.next();
			mVertex = vertex.matcher(s);
						
			if (mVertex.find())
				vertexNumber = Integer.parseInt(mVertex.group().trim());
					
			if (vertexNumber < v.length){
				v[vertexNumber] = parseEdgeList(s);
				// wtf
				// System.out.println(vertexNumber);}
			} else
				throw new MalformedGraphFileException();
					
		}
		
		
	}
	
	private static Edge parseEdge(String s){
		String[] tmp = s.split(":");
		return new Edge(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]));
	}	
	
	private static Edge[] parseEdgeList(String s){
		Edge [] edgeList = new Edge[MAX_EDGES];
		Pattern edges = Pattern.compile("\\d+:\\d+");
		Matcher mEdges = edges.matcher(s);
		int count = 0;
		
		while (mEdges.find()){
			edgeList[count++] = parseEdge(mEdges.group());
		}
				
		return Arrays.copyOf(edgeList, count);
	}
		
	
	public static Graph getGraph(String fileName){
		graphFile = new File(fileName);
		assert(graphFile.canRead());

		try {
			br = new BufferedReader(new FileReader(graphFile));
			parseGraphFile(loadGraphFile());
			br.close();
		} catch (IOException e) {
			System.err.println("Error, couldn't read graph file!");
			return null;
		}
		return new Graph(v);	
	}
	
}

class MalformedGraphFileException extends RuntimeException {
	public MalformedGraphFileException() {
		super();
	}
}

package graph;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Graph Loader class
 *
 * Reads graph from file and returns result as parsed data structure
 *
 *
 * Graph format:
 *      <line>       ::= <number> "{" <edge-list> "}" "\n"
 *      <edge-list>  ::= <edge> ";" <edge-list> | ""
 *      <edge>       ::= <number> ":" <number>
 *      <number>     ::= "[0-9]+"
 *
 *
 */
public class GraphLoader {
    
    /**
     * Reads graph from file
     * @return parsed Graph data structure
     */
    public static Graph getGraph(String fileName){
        File graphFile = new File(fileName);
        assert(graphFile.canRead());
        BufferedReader reader = null;
        Graph graph = null;

        try {
            reader = new BufferedReader(new FileReader(graphFile));
            graph = parseGraphFile(reader, loadGraphFile(reader));
        } catch (IOException e) {
            System.err.println("Error, couldn't read graph file!");
            graph = null;
        } catch (MalformedGraphFileException ex) {
            System.err.println("File is not graph");
            graph = null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Error closing file");
                }
            }
        }

        return graph;
    }


    /**
     * Loads graph definition from CSS-like formated file, 
     * @throws IOException
     */
    private static List<String> loadGraphFile(BufferedReader reader) 
                                                          throws IOException
    {
        List<String> nodeList = new ArrayList<String>();
        String s;    
        
        while ((s = reader.readLine()) != null)
            nodeList.add(s);
        
        return nodeList;    
    }
    

    
    private static Graph parseGraphFile(BufferedReader reader, 
                                        List<String> nodeList)
    {
        Pattern vertexPattern = Pattern.compile("\\d+\\s*");
        Matcher mVertex;
        int vertexNumber = 0;
        int numVertices  = nodeList.size();
        
        Graph graph = new Graph(numVertices);
                
        for (String line : nodeList) {
            mVertex = vertexPattern.matcher(line);
                        
            if (mVertex.find()) {
                try {
                    vertexNumber = Integer.parseInt(mVertex.group().trim());
                } catch(NumberFormatException ex) {
                    throw new MalformedGraphFileException();
                }
            }

            if (vertexNumber < numVertices)
                addEdges(graph, vertexNumber, line);
            else
                throw new MalformedGraphFileException();
        }
        return graph;
    }

    // Add edges from src vertex into graph 
    private static void addEdges(Graph graph, int src, String line) {
        Pattern edgePattern = Pattern.compile("(\\d+):(\\d+)");
        Matcher edgeMatcher = edgePattern.matcher(line);

        try {
            while (edgeMatcher.find()) {
                int dst       = Integer.parseInt(edgeMatcher.group(1));
                int weight    = Integer.parseInt(edgeMatcher.group(2));

				// insert bidirectional edge
                graph.addEdge(src, dst, weight);
                graph.addEdge(dst, src, weight);
            }
        } catch (NumberFormatException ex) {
            throw new MalformedGraphFileException();
        }
    }
}

class MalformedGraphFileException extends RuntimeException {
    public MalformedGraphFileException() {
        super();
    }
}

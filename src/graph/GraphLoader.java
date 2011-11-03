package graph;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
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
        } catch (FileNotFoundException fnf) {
            System.err.printf("File %s not found\n", fileName);
            graph = null;
        } catch (IOException e) {
            System.err.println("Error, couldn't read graph file!");
            graph = null;
        } catch (MalformedGraphFileException ex) {
            System.err.println("File is not graph");
            System.err.println("Error on line " + ex.lineNumber);
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
        if (graph != null && graph.vertices() == 0)
            graph = null;

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



    // possibly time consuming
    private static boolean checkLine(String line) {
        String lineRegex = "\\d+\\s*\\{(?:\\d+:\\d+;?\\s*)*\\}";
        return Pattern.matches(lineRegex, line);
    }

    
    private static Graph parseGraphFile(BufferedReader reader, 
                                        List<String> nodeList)
    {
        Pattern vertexPattern = Pattern.compile("\\d+\\s*");
        Matcher mVertex;
        int vertexNumber = 0;
        int numVertices  = nodeList.size();
        
        Graph graph = new Graph(numVertices);
                
        int lineNumber = 0;
        for (String line : nodeList) {
            lineNumber++;

            if (!checkLine(line))
                throw new MalformedGraphFileException(lineNumber);

            mVertex = vertexPattern.matcher(line);
                        
            if (mVertex.find()) {
                try {
                    vertexNumber = Integer.parseInt(mVertex.group().trim());
                } catch(NumberFormatException ex) {
                    throw new MalformedGraphFileException(lineNumber);
                }
            }

            if (vertexNumber < numVertices)
                addEdges(graph, vertexNumber, line, lineNumber);
            else
                throw new MalformedGraphFileException(lineNumber);
        }
        return graph;
    }



    // Add edges from src vertex into graph 
    private static void addEdges(Graph graph, int src, String line, int i) {
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
            throw new MalformedGraphFileException(i);
        }
    }


    private static class MalformedGraphFileException extends RuntimeException {
        final int lineNumber;
        public MalformedGraphFileException(int lineNumber) {
            super();
            this.lineNumber = lineNumber;
        }
    }
}


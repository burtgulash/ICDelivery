package graph;

import java.util.List;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;



/**
 * <p>
 * Loads graph from file and returns result as Graph 
 * data structure.
 * </p>
 *
 * <p>
 * Exceptional cases are checked, only need to check if returned 
 * Graph is null.
 * </p>
 *
 *
 * <pre>
 * {@code
 * Graph format:
 *      <line>                 ::= <number> "{" <edge-list> "}" "\n"
 *      <edge-list>            ::= <edge> <remaining-edge-list> | ""
 *      <remaining-edge-list>  ::= ";" <edge-list> | ""
 *      <edge>                 ::= <number> ":" <number>
 *      <number>               ::= "[0-9]+"
 * }
 * </pre>
 *
 * @see graph.Graph
 *
 * @author Tomas Marsalek
 *
 */
public class Loader {
    // disable default constructor
    private Loader() {/*,*/}



    /**
     * Loads graph from file.
     *
     * @param fileName name of file containing serialized graph
     * @return Parsed Graph object or null if something failed.
     */
    public static Graph getGraph(String fileName) {
        Graph graph;
        try {
            graph = makeGraph(fileName);
        } catch (GraphFormatException gtfo) {
            gtfo.printErrorMessage();
            graph = null;
        }

        return graph;
    }


    /**
     * Loads lines of graph to List.
     *
     * @param fileName name of file to get lines from
     * @return List of all lines of file.
     */
    private static List<String> getLines(String fileName) {
        List<String> lines = new LinkedList<String>();


        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException fnf) {
            System.err.printf("File %s not found%n", fileName);
            lines = null;
        } catch (IOException ex) {
            System.err.printf("Can not open %s for reading%n", fileName);
            lines = null;
        }
        if (lines == null)
            return null;

        // success opening file
        String line;
        try {
        while ((line = reader.readLine()) != null)
            lines.add(line);
        } catch (IOException ex) {
            System.err.println("Error reading line");
            return null;
        }

        return lines;
    }


    /**
     * Constructs new Graph object as read from file.
     *
     * @param fileName name of file to read from
     * @return Graph object or null if somehow failed to parse the input.
     * @throws GraphFormatException if the file can not be parsed for some
     *                              reason.
     */
    private static Graph makeGraph(String fileName) 
                               throws GraphFormatException 
    {
        Pattern linePattern = Pattern.compile("^(\\d+)\\s*\\{(.*)\\}$");

        List<String> lines = getLines(fileName);
        if (lines == null)
            return null;

        int vertexCount = lines.size();

        Graph graph;
        // don't allow smaller graphs
        if (vertexCount > 1)
            graph = new Graph(vertexCount);
        else
            return null;


        int src, dst, weight;

        int lineCounter = 1;
        for (String line : lines) {
            Matcher m = linePattern.matcher(line);
            if (m.find()) {
                try {
                    src = Integer.parseInt(m.group(1));    
                } catch (NumberFormatException nfe) {
                    throw new GraphFormatException(lineCounter,
                                "Source vertex must be integer");
                } catch (IndexOutOfBoundsException ex) {
                    throw new GraphFormatException(lineCounter,
                                "Source vertex not recognized");
                }
                if (src < 0 || src >= vertexCount)
                    throw new GraphFormatException(lineCounter,
                                "Source vertex out of range: " + src);


                String [] edgeStrings;
                try {
                    edgeStrings = m.group(2).split(";");
                } catch (IndexOutOfBoundsException ex) {
                    throw new GraphFormatException(lineCounter,
                                "No edges found");
                }
                if (edgeStrings == null)
                    throw new GraphFormatException(lineCounter,
                                "No edges found");

                // if no edges for this vertex
                if (edgeStrings.length == 1 && edgeStrings[0].equals(""))
                    continue;

                for (String edgeString : edgeStrings) {
                    String[] dst_weightString = edgeString.split(":");
                    if (dst_weightString.length != 2 ||
                        dst_weightString[0] == null ||
                        dst_weightString[1] == null)
                        throw new GraphFormatException(lineCounter,
                             "Invalid edge: " + edgeString);

                    try {
                        dst     = Integer.parseInt(dst_weightString[0].trim());
                    } catch (NumberFormatException nfe) {
                        throw new GraphFormatException(lineCounter,
                             "Invalid destination vertex: " + edgeString);

                    }
                    try {
                        weight  = Integer.parseInt(dst_weightString[1].trim());
                    } catch (NumberFormatException nfe) {
                        throw new GraphFormatException(lineCounter,
                             "Invalid edge weight: " + edgeString);

                    }

                    if (dst < 0 || dst >= vertexCount)
                        throw new GraphFormatException(lineCounter, 
                             "Destination vertex out of range: " + edgeString);
                    if (weight <= 0)
                        throw new GraphFormatException(lineCounter,
                             "Negative weight: " + edgeString);

                    // success
                    graph.addEdge(src, dst, weight);
                    graph.addEdge(dst, src, weight);
                }
            } else 
                throw new GraphFormatException(lineCounter,
                                 "Line does not match graph format");


            lineCounter++;
        }

        return graph;
    }


    private static class GraphFormatException extends Exception {
        private final int line;
        private final String msg;

        GraphFormatException(int lineNumber, String msg) {
            super();
            line = lineNumber;
            this.msg = msg;
        }

        void printErrorMessage() {
            System.err.println("Error on line " + line + ":  " + msg);
        }
    }
}

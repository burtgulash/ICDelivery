package graph;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;

public class ShortestPathsTest {
    final String graphString =  "0 {3:7; 4:4;}\n" +
                                "1 {2:3; 3:4; 5:6;}\n" +
                                "2 {3:5;}\n" +
                                "3 {5:2;}\n" +
                                "4 {}\n" +
                                "5 {}\n";

    private File tmp;
    private Graph g;

    // Test constructor
    @Before
    public void setUp() {

        try {
            tmp = File.createTempFile("tmp", "graph");
        } catch (IOException ioe) {
            fail("Couldn't create temporary file");
            return;
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(tmp));
            out.write(graphString);
            out.close();
        } catch (IOException ioe) {
            fail("Error writing to tmp file");
            return;
        }

        g = GraphLoader.getGraph(tmp.getAbsolutePath());
    }


    @Test
    public void hardCodedTestFloydWarshall () {
        hardCodedTest(new FloydWarshall(g));    
    }

    @Test
    public void hardCodedTestDijkstra () {
        hardCodedTest(new Dijkstra(g, 0));
    }


    // Tests hard coded and hand checked graph
    private void hardCodedTest (ShortestPaths implementation) {
        ShortestPaths sp = implementation;

        // hardcoded graph has 6 vertices
        assertEquals(6, g.vertices());


        // check all shortest paths in matrix lower triangle
        assertEquals(0, sp.shortestPath(0, 0).weight);
        assertEquals(11, sp.shortestPath(0, 1).weight);
        assertEquals(12, sp.shortestPath(0, 2).weight);
        assertEquals(7,  sp.shortestPath(0, 3).weight);
        assertEquals(4,  sp.shortestPath(0, 4).weight);
        assertEquals(9,  sp.shortestPath(0, 5).weight);

        assertEquals(0,  sp.shortestPath(1, 1).weight);
        assertEquals(3,  sp.shortestPath(1, 2).weight);
        assertEquals(4,  sp.shortestPath(1, 3).weight);
        assertEquals(15, sp.shortestPath(1, 4).weight);
        assertEquals(6,  sp.shortestPath(1, 5).weight);

        assertEquals(0,  sp.shortestPath(2, 2).weight);
        assertEquals(5,  sp.shortestPath(2, 3).weight);
        assertEquals(16, sp.shortestPath(2, 4).weight);
        assertEquals(7,  sp.shortestPath(2, 5).weight);

        assertEquals(0,  sp.shortestPath(3, 3).weight);
        assertEquals(11, sp.shortestPath(3, 4).weight);
        assertEquals(2,  sp.shortestPath(3, 5).weight);

        assertEquals(0,  sp.shortestPath(4, 4).weight);
        assertEquals(13, sp.shortestPath(4, 5).weight);

        assertEquals(0, sp.shortestPath(5, 5).weight);
    }
}

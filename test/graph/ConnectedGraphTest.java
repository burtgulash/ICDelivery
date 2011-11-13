package graph;

import org.junit.*;
import static org.junit.Assert.*;

public class ConnectedGraphTest {
    private static String GRAPH_FILE = "test.graph";
    private Graph graph;


    @Before
    public void init() {
        graph = Loader.getGraph(GRAPH_FILE);
    }

    @Test
    public void ConnectednessTest () {
        ShortestPaths s = new Dijkstra(graph, 0);

        int v = graph.vertices();
        for (int i = 0; i < v; i++)
            for (int j = 0; j <= i; j++)
                assertNotNull(s.shortestPath(i, j));
    }
}

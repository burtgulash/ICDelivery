package graph;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Set of tests that compare shortestPath weights by more implementations
 */
public class CrossImplementationTest {
    private static String GRAPH_FILE = "test.graph";
    private Graph graph;


    @Before
    public void init() {
        graph = GraphLoader.getGraph(GRAPH_FILE);
    }


    @Test
    public void crossFloydWarshallsTest() {
        crossImplementationTest(new FloydWarshall(graph),
                                new OptimizedFloydWarshall(graph));
    }

    @Test
    public void crossFloydWarshallDijkstraTest() {
        crossImplementationTest(new Dijkstra(graph),
                                new OptimizedFloydWarshall(graph));
    }


    // Test different shortestPaths implementations against themselves
    private void crossImplementationTest(ShortestPaths s1, ShortestPaths s2) {
        int v = graph.vertices();
        for (int i = 0; i < v; i++)
            for (int j = 0; j <= i; j++) {
                Path path1 = s1.shortestPath(i, j);
                Path path2 = s2.shortestPath(i, j);

                if (path1 == null)
                    assertEquals(path1, path2);
                else
                    assertEquals(path1.weight, path2.weight);
            }
    }
}

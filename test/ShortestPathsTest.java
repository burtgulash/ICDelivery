import org.junit.*;
import static org.junit.Assert.*;

import graph.*;

public class ShortestPathsTest {
	@Test // UNCOMMENT TO ENABLE TEST
	// Graph for simulation is assumed to be connected
	// This test ensures that
	public void ConnectivityTest () {
		Graph graph = GraphLoader.getGraph("test.graph");
		ShortestPaths sp = new FloydWarshall(graph);

		int v = graph.vertices();
		for (int i = 0; i < v; i++)
			for (int j = 0; j < v; j++)
				// null shortest path doesn't exist
				assertNotNull(sp.shortestPath(i, j));
	}
}

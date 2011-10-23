import org.junit.*;
import static org.junit.Assert.*;

import graph.*;

public class ShortestPathsTest {
	// @Test // UNCOMMENT TO ENABLE TEST
	// Graph for simulation is assumed to be connected
	// This test ensures that
	public void ConnectivityTest () {
		Graph graph = GraphLoader.getGraph("test.graph");
		ShortestPaths sp = new FloydWarshall(graph);
		int v = graph.vertices();
		for (int i = 0; i < v; i++)
			for (int j = 0; j < v; j++)
				assertNotNull(sp.shortestPath(i, j));
	}

	@Test
	// Let junit run without errors as junit needs at least one @Test in file
	public void DummyTest () {}
}

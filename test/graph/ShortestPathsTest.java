package graph;

import org.junit.*;
import static org.junit.Assert.*;

import graph.*;
import java.io.*;

public class ShortestPathsTest {
	final String graphString =  "0 {3:7; 4:4}\n" +
								"1 {2:3; 3:4; 5:6}\n" +
								"2 {1:3; 3:5}\n" +
								"3 {0:7; 1:4; 2:5; 5:2}\n" +
								"4 {0:4}\n" +
								"5 {1:6; 3:2}\n";

	private File tmp;
	private Graph g;

	@Test
	public void rarach () {
		long start = System.currentTimeMillis();
		ShortestPaths sp = new OptimizedFloydWarshall(GraphLoader.getGraph("test.graph1"));
		
		System.err.println((System.currentTimeMillis() - start)/1000);
	}

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
	// generated graph is assumed to be connected
	// This test ensures that
	// Also checks that paths are symmetrc, p[i, j] == p[j, i]
	public void ConnectivityTest () {
		Graph graph = GraphLoader.getGraph("test.graph");
		ShortestPaths sp = new FloydWarshall(graph);

		int v = graph.vertices();
		for (int i = 0; i < v; i++)
			for (int j = 0; j < v; j++) {
				// null shortest path doesn't exist
				assertNotNull(sp.shortestPath(i, j));
				assertEquals(sp.shortestPath(i, j).weight, 
                             sp.shortestPath(j, i).weight);
			}
	}


	
	@Test
	public void HardCodedFloydWarshall () {
		HardCodedTest(new FloydWarshall(g));	
	}


	// Tests hard coded and hand checked graph
	private void HardCodedTest (ShortestPaths implementation) {
		ShortestPaths sp = implementation;

		// hardcoded graph has 6 vertices
		assertEquals(6, g.vertices());
	
		// test hardcoded neighbor counts
		assertEquals(2, g.neighbors(0));
		assertEquals(3, g.neighbors(1));
		assertEquals(2, g.neighbors(2));
		assertEquals(4, g.neighbors(3));
		assertEquals(1, g.neighbors(4));
		assertEquals(2, g.neighbors(5));

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

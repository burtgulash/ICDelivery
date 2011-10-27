import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;

import graph.*;



public class GraphLoaderTest {
	
	final String testFile = "test.graph";
	Graph g;
	
	@Test
	public void testLoader(){
		java.io.File readFile = new java.io.File(testFile);
		assertTrue(readFile.exists());
		assertTrue(readFile.length() > 0);
		assertTrue(readFile.canRead());

		g = GraphLoader.getGraph(testFile);
		assertNotNull(g);

		assertTrue(g.vertices() != 0);
	}
}

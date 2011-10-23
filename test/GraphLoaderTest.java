import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;

import graph.*;



public class GraphLoaderTest {
	
	Graph g;
	
	@Test
	public void testLoader(){
		g = GraphLoader.getGraph("test.graph");
		
		
		assertTrue(g.vertices() != 0);
	}
	
}

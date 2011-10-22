import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;

import graph.*;



public class GraphLoaderTest {
	
	File f = new File("test.graph");
		
	GraphLoader gl;
	Graph g;
	
	@Test
	public void testLoader(){
		if (f.isFile()&& f.canRead()){
			gl = new GraphLoader("test.graph");
			g = gl.getGraph();
		}
		
		assertTrue(g.vertices() != 0);
	}
	
}
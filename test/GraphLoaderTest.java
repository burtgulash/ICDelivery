import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import graph.*;



public class GraphLoaderTest {

    final String testFile = "test.graph";
    Graph g;

    @Test
    public void testLoader(){
        File readFile = new File(testFile);
        if (!readFile.exists()) {
            System.err.printf("File %s does not exist\n", testFile);
            return;
        }


        assertTrue(readFile.length() > 0);
        assertTrue(readFile.canRead());

        g = GraphLoader.getGraph(testFile);
        assertNotNull(g);

        assertTrue(g.vertices() != 0);
    }
}

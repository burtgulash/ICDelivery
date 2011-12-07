package graph;

import org.junit.*;
import static org.junit.Assert.*;

public class PathTest {
    @Test
    public void testConcat() {
        Path p1 = new Path(1, 6, new Path(2, 3, null));
        Path p2 = new Path(3, 6, new Path(4, 3, null));

        Path concatenated = Path.concat(p1, p2);
        Path check = new Path(1, 12, 
                     new Path(2, 9,
                     new Path(3, 6,
                     new Path(4, 3, null))));


        assertEquals(str(concatenated), 
            "(1, 12), (2, 9), (3, 6), (4, 3)");

        assertEquals(str(concatenated), str(check));
    }


    @Test
    public void reverseTest () {
        Path testPath = new Path(1, 18, 
                        new Path(2, 12,
                        new Path(3, 7,
                        new Path(4, 3, null))));
        Path reversed = Path.reversed(0, testPath);
        assertEquals("(3, 18), (2, 15), (1, 11), (0, 6)", 
                     str(reversed));

        testPath = new Path(5, 144,
                   new Path(3, 102,
                   new Path(8, 83,
                   new Path(2, 17,
                   new Path(4, 9, null)))));
        reversed = Path.reversed(7, testPath);
        assertEquals("(2, 144), (8, 135), (3, 127), (5, 61), (7, 42)", 
                     str(reversed));
    }

    // debugging toString method for Path
    private static String str(Path p) {
        String res = "(" + p.vertex + ", " + p.weight + ")";
        for (Path iter = p.rest; iter != null; iter = iter.rest)
            res += ", (" + iter.vertex + ", " + iter.weight + ")";
        return res;
    }
}

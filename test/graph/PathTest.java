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

    // debugging toString method for Path
    private static String str(Path p) {
        String res = "(" + p.vertex + ", " + p.weight + ")";
        for (Path iter = p.rest; iter != null; iter = iter.rest)
            res += ", (" + iter.vertex + ", " + iter.weight + ")";
        return res;
    }
}

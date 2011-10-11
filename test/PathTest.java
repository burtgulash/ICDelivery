import org.junit.*;
import static org.junit.Assert.*;

import graph.*;

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
			

		assertEquals(concatenated.toString(), 
			"(1, 12), (2, 9), (3, 6), (4, 3)");

		assertEquals(concatenated.toString(), check.toString());
	}
}

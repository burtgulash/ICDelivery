import org.junit.*;
import static org.junit.Assert.*;

public class PriorityQueueTest {

	@Test
	public void insertionTest() {
		int numInsertions = 123456;
		PriorityQueue<Testing> pq = new PriorityQueue<Testing>();	

		pq.insert(new Testing(Integer.MIN_VALUE));

		for (int i = 0; i < numInsertions; i++) {
			int priority = i * 101 % 991;
			pq.insert(new Testing(priority));
		}

		pq.insert(new Testing(Integer.MIN_VALUE));
		pq.insert(new Testing(Integer.MAX_VALUE));
		pq.insert(new Testing(Integer.MIN_VALUE));

		assertTrue(pq.checkMinHeapProperty());
	}
}

class Testing implements Queable {
	private static long ids = 1;
	private long id;
	private int priority;
	
	// Construct
	public Testing(int priority) {
		id = ids++;
		this.priority = priority;
	}


	public String toString() {
		return priority + "";
	}

	@Override
	public int priority() { 
		return priority;
	}


	@Override
	public long id() { 
		return id;
	}


	@Override
	public void setPriority(int newPriority) {
		this.priority = newPriority;
	}
}

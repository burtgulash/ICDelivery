import org.junit.*;
import static org.junit.Assert.*;

import priorityQueue.*;

public class PriorityQueueTest {

	@Test
	public void insertExtractTest() {
		PriorityQueue<Testing> pq = new PriorityQueue<Testing>();
		int numInsertions = 12345;
		int numExtracts = numInsertions;


	// ---------------------------------------------- \\

		System.out.println("Test correctness of inserting into queue...");

		for (int i = 0; i < numInsertions; i++) {
			int priority = i * 171 % 993; // Random nums
			pq.insert(new Testing(priority));
		}

		assertTrue(pq.checkMinHeapProperty());


		System.out.println("Test correctness of extracting min from queue...");
		int lastPriority = pq.extractMin().priority();
		for (int i = 1; i < numExtracts; i++) {
			int curPriority = pq.extractMin().priority();

			assertTrue(curPriority >= lastPriority);
			lastPriority = curPriority;
		}
		
		assertTrue(pq.checkMinHeapProperty());
		assertEquals(pq.length(), numInsertions - numExtracts);
	}

	
	@Test
	public void changePriorityTest() {
		PriorityQueue<Testing> pq = new PriorityQueue<Testing>();
		int numInsertions = 12345;
		int numChanges = numInsertions / 7;


	// ---------------------------------------------- \\
		
		for (int i = 1; i <= numInsertions; i++) {
			int priority = i * 19 % 103; // Random nums
			pq.insert(new Testing(priority));
		}
		assertTrue(pq.checkMinHeapProperty());

		System.out.println("Test correctness of changePriority...");

		for (int iter = 0; iter < numChanges; iter++) {
			if (pq.empty())
				break;

			Testing current = pq.min();
			long currentId = current.id();
			pq.changePriority(currentId, current.priority() * 3 + 11);	

			while (pq.extractMin().id() != currentId)
				;

			if (pq.empty())
				break;




			current = pq.min();
			currentId = current.id();
			pq.changePriority(currentId, current.priority() * 2 + 3);
			assertNotNull(pq.remove(currentId));

			assertTrue(pq.checkMinHeapProperty());
		}
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

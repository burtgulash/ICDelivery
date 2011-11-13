import org.junit.*;
import static org.junit.Assert.*;

import priorityQueue.*;
import java.util.ArrayList;

public class PriorityQueueTest {

    @Test
    public void insertExtractTest() {
        PriorityQueue<Testing> pq = new PriorityQueue<Testing>();
        int numInsertions = 12345;
        int numExtracts = numInsertions;


    // ---------------------------------------------- \\

        for (int i = 0; i < numInsertions; i++) {
            int priority = i * 171 % 993; // Random nums
            pq.insert(new Testing(priority));
        }

        assertTrue(pq.checkMinHeapProperty());


        int lastPriority = 0;
        try {
            lastPriority = pq.extractMin().priority();
        } catch (PriorityQueue.EmptyQueueException ex) {
            fail("Empty queue");
        }
        for (int i = 1; i < numExtracts; i++) {
            int curPriority = 0;
            try {
                curPriority = pq.extractMin().priority();
            } catch (PriorityQueue.EmptyQueueException ex) {
                fail("Empty queue");
            }

            assertTrue(curPriority >= lastPriority);
            lastPriority = curPriority;
        }

        assertTrue(pq.checkMinHeapProperty());
        assertEquals(numInsertions - numExtracts, pq.size());
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


        for (int iter = 0; iter < numChanges; iter++) {
            if (pq.empty())
                break;

            try {
                Testing current = pq.min();
                int currentId = current.id();
                pq.changePriority(currentId, current.priority() * 3 + 11);    

                while (pq.extractMin().id() != currentId)
                    ;

            if (pq.empty())
                break;



                current = pq.min();
                currentId = current.id();
                pq.changePriority(currentId, current.priority() * 2 + 3);
                assertNotNull(pq.remove(currentId));
            } catch (PriorityQueue.EmptyQueueException ex) {
                fail("Empty queue");
            }

            assertTrue(pq.checkMinHeapProperty());
        }
    }


    @Test
    public void removeTest() {
        PriorityQueue<Testing> pq = new PriorityQueue<Testing>();
        int numInsertions = 1337;

        // if i % 71 == 0, delete it after inserted all elems
        int testModulus = 71; 
        ArrayList<Integer> toBeDeleted = new ArrayList<Integer>();


    // ---------------------------------------------- \\

        for (int i = 0; i < numInsertions; i++) {
            int priority = i * 31 % 193; // Random nums
            Testing inserted = new Testing(priority);
            pq.insert(inserted);
            if (inserted.id() % testModulus == 0)
                // Append random elems to delete queue
                toBeDeleted.add(inserted.id()); 
        }

        int numDeletions = toBeDeleted.size();
        for (int i = 0; i < numDeletions; i++) {
            try {
                pq.remove(toBeDeleted.get(i));
            } catch (PriorityQueue.EmptyQueueException ex) {
                fail("Empty queue");
            }
            assertTrue(pq.checkMinHeapProperty());
        }

        // Check counts
        assertEquals(numInsertions - numDeletions, pq.size());

        while (!pq.empty()) {
            int curId = 0;
            try {
                curId = pq.min().id();
            } catch (PriorityQueue.EmptyQueueException ex) {
                fail("Empty queue");
            }
            // IDs divisible by testModulus already removed
            assertTrue(curId % testModulus != 0);
            try {
                pq.remove(curId);
            } catch (PriorityQueue.EmptyQueueException ex) {
                fail("Empty queue");
            }

            // already deleted item must not be present in priority queue
            assertFalse(toBeDeleted.contains(curId));
        }

        assertTrue(pq.empty());
        assertTrue(pq.checkMinHeapProperty());
    }
}

class Testing implements Queable {
    private static int ids = 1;
    private int id;
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
    public int id() { 
        return id;
    }


    @Override
    public void setPriority(int newPriority) {
        this.priority = newPriority;
    }
}

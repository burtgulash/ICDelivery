package priorityQueue;

import java.util.Map;
import java.util.TreeMap;


/**
 * Priority Queue implemented by binary min-heap
 * Used as Calendar in simulation and as helper structure for Dijkstra's alg.
 */
public class PriorityQueue<T extends Queable> {
    private int used, alloc;
    // underlying array for array-based binary heap
    private T[] ar;
    private Map<Integer, Integer> position;


    @SuppressWarnings("unchecked")
    /**
     * Creates priority queue initialized to size 4.
     */
    public PriorityQueue() {
        used = 0;
        alloc = 4;

        ar = (T[]) new Queable[1 + 4];
        position = new TreeMap<Integer, Integer>();
    }


    @SuppressWarnings("unchecked")
    /**
     * Resizes array to new size.
      *
     * @param newSize new size of heap array
     */
    private void resize(int newSize) {
        T[] oldAr = ar;
        ar = (T[]) new Queable[1 + newSize];
        for (int i = 1; i < used + 1; i++) 
            ar[i] = oldAr[i];
        alloc = newSize;
    }


    /**
     * Test for emptiness of queue.
     *
     * @return true if the queue is empty, false otherwise.
     */
    public boolean empty() {
        return used == 0;
    }


    /**
     * Returns number of elements in queue
     *
     * @return Number of elements in queue.
     */
    // not used, but useful 
    public int size() {
        return used;
    }


    /**
     * Swaps two elements in queue and updates their positions in map
     *
     * @param pos1 position of first element
     * @param pos2 position of second element
     */
    private void swap(int pos1, int pos2) {
        T tmp = ar[pos1];
        ar[pos1] = ar[pos2];
        ar[pos2] = tmp;

        position.put(ar[pos1].id(), pos1);
        position.put(ar[pos2].id(), pos2);
    }


    /** 
     * Bubbles specified element down.
     * Down as if the heap was pictured as a triangle pointing upwards.
     *
     * @param pos position of element to bubble down.
     */
    private void down(int pos) {
        int old = pos;
        pos *= 2;

        while(pos < 1 + used) {
            if (pos + 1 < ar.length && 
                    ar[pos + 1].priority() < ar[pos].priority())
                pos++;

            if (ar[pos].priority() >= ar[old].priority())
                break;

            swap(old, pos);    

            old = pos;
            pos *= 2;
        }
    }


    /** 
     * Bubbles specified element up.
     *
     * @param pos position of element to bubble up.
     */
    private void up(int pos) {
        int old = pos;
        pos /= 2;
        while(pos > 0 && ar[pos].priority() > ar[old].priority()) {
            swap(old, pos);

            old = pos;
            pos /= 2;
        }
    }


    /**
     * Inserts element to the queue.
     *
     * @param elem element to insert.
     */
    public void insert(T elem) {
        if (used >= alloc)
            resize(2 * alloc);
        used++;

        ar[used] = elem;
        position.put(elem.id(), used);

        up(used);
    }


    /**
     * Peeks for element that would be taken next by extractMin.
     *
     * @return Next element in queue.
     */
    public T min() {
        if (used < 1)
            throw new EmptyQueueException();
        return ar[1];
    }


    /**
     * Removes element with least weight from queue.
      *
     * @return Next element in queue.
     */
    public T extractMin() {
        if (used < 1)
            throw new EmptyQueueException();
        if (used < alloc / 2)
            resize(alloc / 2);

        T ret = ar[1];
        ar[1] = ar[used];
        used--;

        position.put(ar[1].id(), 1);
        position.remove(ret.id());

        down(1);
        return ret;
    }


    /**
     * Changes weight of element with elemId to newPriority.
     * Don't use INTEGER.MIN_VALUE as newPriority.
     *
     * @param elemId id of element, returned by implemented Queable.id()
     * @param newPriority new priority of element
     * @return true if element was present in queue or false if not.
     */
    public boolean changePriority(int elemId, int newPriority) {
        Integer elemPos_ob = position.get(elemId);
        if (elemPos_ob == null)
            return false;
        int elemPos = elemPos_ob.intValue();

        int oldPriority = ar[elemPos].priority();
        ar[elemPos].setPriority(newPriority);

        if (oldPriority < newPriority) 
            down(elemPos);
        else 
            up(elemPos);

        return true;
    }


    /**
     * Removes and returns element from queue.
     *
     * @param elemId id of element, returned by implemented Queable.id()
     * @return Element with elemId or null if no element with elemId.
     */
    public T remove(int elemId) {
        if(changePriority(elemId, Integer.MIN_VALUE))
            return extractMin();
        return null;
    }


    /**
     * Returns priority of selected element.
     *
     * @param elemId id of element, returned by implemented Queable.id()
     * @return Priority of specified element.
     */
    public int priority(int elemId) {
        Integer elemPos_ob = position.get(elemId);
        if (elemPos_ob == null)
            return -1;
        return ar[elemPos_ob.intValue()].priority();
    }



    /**
     * Checks if min-heap property is satisfied.
     * Exists only to keep underlying array private.
     * Used in junit tests.
     *
     * @return true if satisfied, false if not.
     */
    public boolean checkMinHeapProperty() {
        for (int i = 1 + 1; i < 1 + used; i++)
            if (ar[i].priority() < ar[i / 2].priority()) {
                System.out.println("MinHeapProperty failed: " + 
                                   i + ", " + ar[i].priority()+", " 
                                    + ar[i / 2].priority());
                return false;
            }
        return true;
    }
}


/**
 * Thrown when trying to get element from an empty queue.
 */
class EmptyQueueException extends RuntimeException {
    /**
     * Constructs an EmptyQueueException with null as its error message string.
     */
    public EmptyQueueException() {
        super();
    }
}

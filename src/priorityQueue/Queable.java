package priorityQueue;

/**
 * Interface for classes that can be used as elements of priority queue.
 *
 * Priority of element must not change outside priority queue or the behavior
 * will be unspecified.
 */
public interface Queable {

    /**
     * Id of element. Allows for O(1) lookup of elements in queue.
     *
     * @return Unique id of an element.
     */
    public int id();


    /**
     * Get priority of an element.
     *
     * @return Priority determining when the element will be drawn from queue.
     */
    public int priority();


    /**
     * Sets new priority for the element.
     * Only mutator in Queable interface, can only be used by priority queue.
     *
     * @param newPriority new priority of the element
     */
    public void setPriority(int newPriority);
}

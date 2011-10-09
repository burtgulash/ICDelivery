import java.util.Map;
import java.util.TreeMap;

public class PriorityQueue<T extends Comparable<T>> {
	private int used, alloc;
	private T[] ar;
	private Map<Long, Integer> pos;
	
	@SuppressWarnings("unchecked")
	public PriorityQueue() {
		used = 0;
		alloc = 4;
		ar = (T[]) new Object[1 + 4];
		pos = new TreeMap<Long, Integer>();
	}
	
	private void swap(int pos1, int pos2) {
		T tmp = ar[pos1];
		ar[pos1] = ar[pos2];
		ar[pos2] = tmp;
		tmp = null;
	}


	private void resize(int newSize) {
		// TODO
	}

	// Bubbles element on position pos down and returns its new position.
	private int down(int pos) {
		int old = pos;
		pos *= 2;
		for (; pos < ar.length; pos += pos) {
			old = pos;
			if (pos + 1 < ar.length && ar[pos + 1].compareTo(ar[pos]) < 0)
				pos++;
			swap(old, pos);	
		}
		return old;
	}

	private int up(int pos) {
		int old = pos;
		pos /= 2;
		for (; pos > 0 && ar[pos].compareTo(ar[old]) > 0 ; pos /= 2)
			swap(old, pos);
		return old;
	}

	public void insert(T elem) {
		if (used >= alloc)
			resize(2 * alloc);
		ar[used] = elem;
		up(used);
		used++;
	}

	public T min() {
		if (used < 1)
			throw new EmptyQueueException();
		return ar[1];
	}
	
	public T extractMin() {
		// TODO
		return null;
	}

	public void decreaseKey(T elem) {
		// TODO
	}

	public void remove(T elem) {
		// TODO
	}
}

class EmptyQueueException extends RuntimeException {
	public EmptyQueueException() {
		super();
	}
}

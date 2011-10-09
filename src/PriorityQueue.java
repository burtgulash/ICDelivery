import java.util.Map;
import java.util.TreeMap;

public class PriorityQueue<T extends Comparable<T>> {
	private T[] ar;
	private Map<Long, Integer> pos;
	
	@SuppressWarnings("unchecked")
	public PriorityQueue() {
		ar = (T[]) new Object[1];
		pos = new TreeMap<Long, Integer>();
	}
	
	private void swap(int pos1, int pos2) {
		T tmp = ar[pos1];
		ar[pos1] = ar[pos2];
		ar[pos2] = tmp;
		tmp = null;
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

	public void insert(T elem, int priority) {
	}

	public T min() {
		return null;
	}
	
	public T extractMin() {
		return null;
	}

	public void decreaseKey(T elem) {
	}

	public void remove(T elem) {
	}
}

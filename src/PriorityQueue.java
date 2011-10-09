import java.util.Map;
import java.util.TreeMap;

public class PriorityQueue<T extends Queable> {
	private int used, alloc;
	private T[] ar;
	private Map<Long, Integer> position;
	
	@SuppressWarnings("unchecked")
	public PriorityQueue() {
		used = 0;
		alloc = 4;
		ar = (T[]) new Object[1 + 4];
		position = new TreeMap<Long, Integer>();
	}
	
	private void swap(int pos1, int pos2) {
		T tmp = ar[pos1];
		ar[pos1] = ar[pos2];
		ar[pos2] = tmp;

		position.put(ar[pos1].id(), pos1);
		position.put(ar[pos2].id(), pos2);
	}


	@SuppressWarnings("unchecked")
	private void resize(int newSize) {
		T[] oldAr = ar;
		ar = (T[]) new Object[1 + newSize];
		for (int i = 1; i < used + 1; i++) 
			ar[i] = oldAr[i];
		alloc = newSize;
	}

	// Bubbles element on position pos down and returns its new position.
	private void down(int pos) {
		int old = pos;
		pos *= 2;
		for (; pos < ar.length; pos += pos) {
			old = pos;
			if (pos + 1 < ar.length && 
					ar[pos + 1].priority() < ar[pos].priority())
				pos++;
			swap(old, pos);	
		}
	}

	private void up(int pos) {
		int old = pos;
		pos /= 2;
		for (; pos > 0 && ar[pos].priority() > ar[old].priority(); pos /= 2)
			swap(old, pos);
	}

	public void insert(T elem) {
		if (used >= alloc)
			resize(2 * alloc);
		ar[used] = elem;
		position.put(elem.id(), used);

		up(used);
		used++;
	}

	public T min() {
		if (used < 1)
			throw new EmptyQueueException();
		return ar[1];
	}
	
	public T extractMin() {
		if (used < 1)
			throw new EmptyQueueException();
		if (used < alloc / 2)
			resize(alloc / 2);
		
		T ret = ar[1];
		ar[1] = ar[used];
		position.put(ar[1].id(), 1);
		position.remove(ret.id());

		down(used);
		return ret;
	}

	public boolean changePriority(long elemId, int newPriority) {
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

	public T remove(long elemId) {
		if(changePriority(elemId, Integer.MIN_VALUE))
			return extractMin();
		return null;
	}
}

class EmptyQueueException extends RuntimeException {
	public EmptyQueueException() {
		super();
	}
}

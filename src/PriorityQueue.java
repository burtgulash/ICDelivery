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

		ar = (T[]) new Queable[1 + 4];
		position = new TreeMap<Long, Integer>();
	}


	@SuppressWarnings("unchecked")
	private void resize(int newSize) {
		T[] oldAr = ar;
		ar = (T[]) new Queable[1 + newSize];
		for (int i = 1; i < used + 1; i++) 
			ar[i] = oldAr[i];
		alloc = newSize;
	}

	
	public String toString() {
		if (used < 1)
			return "";
 		String res = ar[0 + 1].toString();
 		for (int i = 1; i < used; i++)
 			res += ", " + ar[i + 1].toString();
 		return res;
	}
	

	public boolean empty() {
		return used == 0;
	}
	

	public int length() {
		return used;
	}


	private void swap(int pos1, int pos2) {
		T tmp = ar[pos1];
		ar[pos1] = ar[pos2];
		ar[pos2] = tmp;

		position.put(ar[pos1].id(), pos1);
		position.put(ar[pos2].id(), pos2);
	}


	// Bubbles element on position pos down
	private void down(int pos) {
		int old = pos;
		pos *= 2;

		while(pos < 1 + used) {
			if (pos + 1 < ar.length && 
					ar[pos + 1].priority() < ar[pos].priority()) {
				pos++;
			}
			if (ar[pos].priority() >= ar[old].priority())
				break;
			swap(old, pos);	

			old = pos;
			pos *= 2;
		}
	}


	// Bubbles element on position pos up
	private void up(int pos) {
		int old = pos;
		pos /= 2;
		while(pos > 0 && ar[pos].priority() > ar[old].priority()) {
			swap(old, pos);

			old = pos;
			pos /= 2;
		}
	}

	public void insert(T elem) {
		if (used >= alloc)
			resize(2 * alloc);
		used++;

		ar[used] = elem;
		position.put(elem.id(), used);

		up(used);
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
		used--;

		position.put(ar[1].id(), 1);
		position.remove(ret.id());

		down(1);
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




	// Remove after testing
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

class EmptyQueueException extends RuntimeException {
	public EmptyQueueException() {
		super();
	}
}

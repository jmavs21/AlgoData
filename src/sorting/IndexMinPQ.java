package sorting;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Index min riority queue of comparable keys implemented with binary tree heap (array).
 * 
 * Initialization: O(n) where n is the number of items (capacity).
 * Operations:
 *     insert, delMin, delete, changeKey, decreaseKey, increaseKey: O(log N) amortized time.
 *     minIndex, minKey, size, isEmpty, contains, keyOf: O(1)
 * 
 * @author joaquin
 */
public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
	private int maxN;        // maximum number of elements on PQ
	private int n;           // number of elements on PQ
	private int[] pq;        // binary heap using 1-based indexing
	private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
	private Key[] keys;      // keys[i] = priority of i

	@SuppressWarnings("unchecked")
	public IndexMinPQ(int maxN) {
		if (maxN < 0) throw new IllegalArgumentException();
		this.maxN = maxN;
		n = 0;
		keys = (Key[]) new Comparable[maxN + 1];
		pq   = new int[maxN + 1];
		qp   = new int[maxN + 1];
		for (int i = 0; i <= maxN; i++)
			qp[i] = -1;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public boolean contains(int i) {
		if (i < 0 || i >= maxN) throw new IllegalArgumentException();
		return qp[i] != -1;
	}

	public int size() {
		return n;
	}

	public void insert(int i, Key key) {
		if (i < 0 || i >= maxN) throw new IllegalArgumentException();
		if (contains(i)) throw new IllegalArgumentException("Index is already in the priority queue.");
		n++;
		qp[i] = n;
		pq[n] = i;
		keys[i] = key;
		swim(n);
	}

	public int minIndex() {
		if (n == 0) throw new NoSuchElementException("Priority queue underflow.");
		return pq[1];
	}

	public Key minKey() {
		if (n == 0) throw new NoSuchElementException("Priority queue underflow.");
		return keys[pq[1]];
	}

	public int delMin() {
		if (n == 0) throw new NoSuchElementException("Priority queue underflow.");
		int min = pq[1];
		swap(1, n--);
		sink(1);
		assert min == pq[n+1];
		qp[min] = -1;        // delete
		keys[min] = null;    // to help with garbage collection
		pq[n+1] = -1;        // not needed
		return min;
	}

	public Key keyOf(int i) {
		if (i < 0 || i >= maxN) throw new IllegalArgumentException();
		if (!contains(i)) throw new NoSuchElementException("Index is not in the priority queue.");
		else return keys[i];
	}

	public void changeKey(int i, Key key) {
		if (i < 0 || i >= maxN) throw new IllegalArgumentException();
		if (!contains(i)) throw new NoSuchElementException("Index is not in the priority queue.");
		keys[i] = key;
		swim(qp[i]);
		sink(qp[i]);
	}

	public void decreaseKey(int i, Key key) {
		if (i < 0 || i >= maxN) throw new IllegalArgumentException();
		if (!contains(i)) throw new NoSuchElementException("Index is not in the priority queue.");
		if (keys[i].compareTo(key) <= 0)
			throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key.");
		keys[i] = key;
		swim(qp[i]);
	}

	public void increaseKey(int i, Key key) {
		if (i < 0 || i >= maxN) throw new IllegalArgumentException();
		if (!contains(i)) throw new NoSuchElementException("Index is not in the priority queue.");
		if (keys[i].compareTo(key) >= 0)
			throw new IllegalArgumentException("Calling increaseKey() with given argument would not strictly increase the key.");
		keys[i] = key;
		sink(qp[i]);
	}

	public void delete(int i) {
		if (i < 0 || i >= maxN) throw new IllegalArgumentException();
		if (!contains(i)) throw new NoSuchElementException("Index is not in the priority queue.");
		int index = qp[i];
		swap(index, n--);
		swim(index);
		sink(index);
		keys[i] = null;
		qp[i] = -1;
	}

	private boolean greater(int i, int j) {
		return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
	}

	private void swap(int i, int j) {
		int swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
		qp[pq[i]] = i;
		qp[pq[j]] = j;
	}

	private void swim(int k) {
		while (k > 1 && greater(k/2, k)) {
			swap(k, k/2);
			k = k/2;
		}
	}

	private void sink(int k) {
		while (2*k <= n) {
			int j = 2*k;
			if (j < n && greater(j, j+1)) j++;
			if (!greater(k, j)) break;
			swap(k, j);
			k = j;
		}
	}

	public Iterator<Integer> iterator() { 
		return new HeapIterator(); 
	}

	private class HeapIterator implements Iterator<Integer> {
		private IndexMinPQ<Key> copy;
		public HeapIterator() {
			copy = new IndexMinPQ<Key>(pq.length - 1);
			for (int i = 1; i <= n; i++)
				copy.insert(pq[i], keys[pq[i]]);
		}
		public boolean hasNext()  { return !copy.isEmpty();                     }
		public Integer next() {
			if (!hasNext()) throw new NoSuchElementException();
			return copy.delMin();
		}
		public void remove()      { throw new UnsupportedOperationException();  }
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		// insert a bunch of strings
		String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };
		IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}
		// delete and print each key
		while (!pq.isEmpty()) {
			int i = pq.delMin();
			System.out.println(i + " " + strings[i]);
		}
		System.out.println();
		// reinsert the same strings
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}
		// print each key using the iterator
		for (int i : pq) {
			System.out.println(i + " " + strings[i]);
		}
		while (!pq.isEmpty()) {
			pq.delMin();
		}
	}
}

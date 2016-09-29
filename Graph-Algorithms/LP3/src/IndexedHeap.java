/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aditya
 */
import java.util.ArrayList;
import java.util.Comparator;

public class IndexedHeap<T extends Index> {

	ArrayList<T> pq;// arraylist which stores the elements
	Comparator<T> c;// comparator based on which queue is made
	int size;// number of elements currently in the array

	/**
	 * Build a priority queue with a given array q
	 */
	IndexedHeap(T[] q, Comparator<T> comp) {
		size = q.length;
		pq = new ArrayList<T>(size + 1);

		// initialize
		for (int i = 0; i < size + 1; i++) {
			pq.add(null);
		}

		for (int i = 0; i < size; i++) {
			pq.add(i + 1, q[i]);
			pq.get(i + 1).putIndex(i + 1);
		}
		c = comp;
		buildHeap();
	}

	/**
	 * Create an empty priority queue of given maximum size
	 */
	IndexedHeap(int n, Comparator<T> comp) {
		c = comp;
		size = 0;
		pq = new ArrayList<T>(n + 1);
		// initialize
		for (int i = 0; i <= n; i++) {
			pq.add(null);
		}
	}

	/**
	 * restore heap order property after the priority of x has decreased
	 */
	void decreaseKey(T x) {
		percolateUp(x.getIndex());
	}

	void buildHeap() {
		for (int i = (size) / 2; i > 0; i--) {
			percolateDown(i);
		}
	}

	void percolateUp(int i) {
		T temp = pq.get(i);
		// while the parent element is greater than
		// element being added,keep moving up
		while (i > 1 && c.compare(temp, pq.get(i / 2)) < 0) {
			pq.set(i, pq.get(i / 2));
			pq.get(i).putIndex(i);
			i /= 2;
		}
		pq.set(i, temp);
		pq.get(i).putIndex(i);
	}

	/**
	 * pq[i] may violate heap order with children
	 */
	void percolateDown(int i) {
		T temp = pq.get(i);
		int hole = i;// stores the current location of the added element
		int child;
		while (2 * hole <= size) {
			child = 2 * hole;
			if (child != size) {
				// if the right child is less than the left child
				if (c.compare(pq.get(child), pq.get(child + 1)) > 0) {
					child++;
				}
			}
			if (c.compare(temp, pq.get(child)) > 0) {
				pq.set(hole, pq.get(child));
				pq.get(hole).putIndex(hole);
				hole = child;
			} else {
				break;
			}
		}
		pq.set(hole, temp);
		pq.get(hole).putIndex(hole);
	}

	public void insert(T x) {
		add(x);
	}

	public T deleteMin() {
		return remove();
	}

	public T min() {
		return peek();
	}

	public void add(T x) {
		pq.set(size + 1, x);
		percolateUp(size + 1);
		++size;
	}

	public T remove() {
		T max_priority = pq.get(1);
		pq.set(1, pq.get(size));
		percolateDown(1);// structuring the heap again
		size--;
		return max_priority;
	}

	public T peek() {
		return pq.get(1);
	}

	public static void main(String args[]) {
		Comparator<NewInt> comp = new Comparator<NewInt>() {
			@Override
			public int compare(NewInt x, NewInt y) {
				return x.val - y.val;
			}
		};

		Integer[] B = new Integer[] { 20, 5, 9, 7, 2, 6, 2, -1 };
		NewInt[] A = new NewInt[8];
		for (int i = 0; i < 8; i++) {
			A[i] = new NewInt(B[i]);
		}

		IndexedHeap<NewInt> obj = new IndexedHeap<>(A.length, comp);
		for (int i = 0; i < A.length; i++) {
			obj.add(A[i]);
		}

		for (int i = 0; i < A.length; i++) {
			System.out.print(obj.remove().val + " ");
		}

	}
}

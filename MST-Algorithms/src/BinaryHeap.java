/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aditya
 */
import java.util.Comparator;

@SuppressWarnings("unchecked")
public class BinaryHeap<T> implements PQ<T> {

	T[] pq;// array which stores the elements
	Comparator<T> c;// comparator based on which queue is made
	int size;// the number of elements currently in the queue

	/**
	 * Build a priority queue with a given array q
	 */
	BinaryHeap(T[] q, Comparator<T> comp) {
		size = q.length;
		// Initialize a new array with given array
		pq = (T[]) new Object[size + 1];
		for (int i = 0; i < size; i++) {
			pq[i + 1] = q[i];
		}
		c = comp;
		buildHeap();
	}

	/**
	 * Create an empty priority queue of given maximum size
	 */
	BinaryHeap(int n, Comparator<T> comp) {
		c = comp;
		size = 0;
		pq = (T[]) new Object[n + 1];
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
		// setting the last element to x
		pq[size + 1] = x;
		percolateUp(size + 1);
		++size;
	}

	public T remove() {
		T max_priority = pq[1];
		pq[1] = pq[size];
		percolateDown(1);// structuring the heap again
		size--;
		return max_priority;
	}

	// returns the topmost and the smallest element
	public T peek() {
		return pq[1];
	}

	/**
	 * pq[i] may violate heap order with parent
	 */
	void percolateUp(int i) {
		T temp = pq[i];
		// while the parent element is greater than
		// element being added,keep moving up
		while (i > 1 && c.compare(temp, pq[i / 2]) < 0) {
			pq[i] = pq[i / 2];
			i /= 2;
		}
		pq[i] = temp;
	}

	/**
	 * pq[i] may violate heap order with children
	 */
	void percolateDown(int i) {
		T temp = pq[i];
		int hole = i;// stores the current location of the added element
		int child;
		while (2 * hole <= size) {
			child = 2 * hole;
			if (child != size) {
				// if the right child is less than the left child
				if (c.compare(pq[child], pq[child + 1]) > 0) {
					child++;
				}
			}
			if (c.compare(temp, pq[child]) > 0) {
				pq[hole] = pq[child];
				hole = child;
			} else {
				break;
			}
		}
		pq[hole] = temp;
	}

	/**
	 * Create a heap. Precondition: none.
	 */
	// RT = O(n)
	void buildHeap() {
		for (int i = (size) / 2; i > 0; i--) {
			percolateDown(i);
		}
	}

	/*
	 * sort array A[1..n]. A[0] is not used. Sorted order depends on comparator
	 * used to buid heap. min heap ==> descending order max heap ==> ascending
	 * order
	 */
	public static <T> void heapSort(T[] A,
			Comparator<T> comp) { /* to be implemented */

		BinaryHeap<T> obj = new BinaryHeap<>(A.length, comp);
		for (int i = 0; i < A.length; i++) {
			obj.add(A[i]);
		}

		for (int i = 0; i < A.length; i++) {
			System.out.print(obj.remove() + " ");
		}

	}

	public static void main(String args[]) {
		Comparator<Integer> comp = new Comparator<Integer>() {

			public int compare(Integer x, Integer y) {
				return x.intValue() - y.intValue();
			}

		};
		Integer[] A = new Integer[] { 20, 5, 9, 7, 2, 6, 2, -1 };
		heapSort(A, comp);
	}
}

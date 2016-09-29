
/**
 * Class to represent a vertex of a graph
 * 
 *
 */

import java.util.ArrayList;
import java.util.List;

public class Vertex implements Index {
	public int top;
	public int name; // name of the vertex
	public boolean seen; // flag to check if the vertex has already been visited
	public int count;// a counter to check for negative cycles
	public Vertex parent; // parent of the vertex
	public int distance; // distance to the vertex from the source vertex
	public List<Edge> Adj, revAdj; // adjacency list; use LinkedList or
									// ArrayList
	public int index;// index of the vertex in the priority queue
	public int inDegree;// inDegree of the vertex
	public int spCount = 0;// count of number of shortest path for the vertex

	/**
	 * Constructor for the vertex
	 * 
	 * @param n
	 *            : int - name of the vertex
	 */
	Vertex(int n) {
		name = n;
		seen = false;
		parent = null;
		Adj = new ArrayList<Edge>();
		revAdj = new ArrayList<Edge>(); /* only for directed graphs */
		inDegree = 0;
		index = -1;
	}

	/**
	 * Method to represent a vertex by its name
	 */
	public String toString() {
		return Integer.toString(name);
	}

	@Override
	public void putIndex(int index) {
		this.index = index;
	}

	@Override
	public int getIndex() {
		return this.index;
	}
}
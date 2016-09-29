import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a vertex of a graph
 *
 *
 */
class Vertex {

	public int name; // name of the vertex
	public boolean seen; // flag to check if the vertex has already been visited
	public Vertex parent; // parent of the vertex
	public int distance; // distance to the vertex from the source vertex
	public List<Edge> Adj, revAdj; // adjacency list; use LinkedList or
									// ArrayList
	public boolean color;// type of the node, False means outer and True means
							// inner
	public Vertex mate;// mate of the graph in matching

	public int label;// Label of the vertex for weighted bipartite matching
	public boolean freeze;
	public int tree;// label of the each tree formed in the graph
	public List<Vertex> children;

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

		color = false;// outer node by default
		label = 0;// inner node by default
		freeze = false;
		tree = -1;
		children = new ArrayList<>();// children of the vertex
	}

	/**
	 * Method to represent a vertex by its name
	 */
	public String toString() {
		return Integer.toString(name);
	}
}
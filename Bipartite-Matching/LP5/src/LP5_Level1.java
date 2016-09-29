import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author divyanshu, radhika, aditya, shweta
 *
 */
public class LP5_Level1 {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static boolean hasCycle;

	// marking the nodes as unseen
	public static void markUnseen(Graph g) {
		for (Vertex v : g) {
			v.seen = false;
		}
	}

	// get the weight of the edge with vertex v1 and v2
	public static int getWeight(Vertex v1, Vertex v2) {
		for (Edge e : v1.Adj) {
			if (e.otherEnd(v1) == v2) {
				return e.Weight;
			}
		}
		return -1;
	}

	/**
	 * Printing the matched edges
	 * 
	 * @param g
	 *            : graph
	 */
	public static void printMatching(Graph g) {
		markUnseen(g);
		for (Vertex v : g) {
			if (!v.seen && v.mate != null) {
				System.out.println(v.name + "  " + v.mate.name + "  " + getWeight(v, v.mate));
				v.seen = true;
				v.mate.seen = true;
			}
		}
		markUnseen(g);
	}

	/**
	 * Helper function for checkBipartite
	 * 
	 * @param v
	 *            : vertex
	 */
	public static void dfs(Vertex v) {
		v.seen = true;
		for (Edge e : v.Adj) {
			Vertex u = e.otherEnd(v);
			if (!u.seen) {
				u.color = !v.color;// updating color of vertex u to mark it as
									// inner.
				// dfs(u);
			}
			// if the vertex and it's adjacent vertex have the same color then
			// we have got a cycle
			if (u.color == v.color) {
				hasCycle = true;
			}
		}
	}

	/**
	 * Checking for bipartite graph and labeling nodes as outer or inner
	 * recursively
	 * 
	 * @param g:
	 *            graph
	 */
	public static void checkBipartite(Graph g) {
		hasCycle = false;
		for (Vertex v : g) {
			if (v.seen == false) {
				dfs(v);
			}
		}
		for (Vertex v : g) {
			v.seen = false;
		}
	}

	// tree count
	static int count = 0;

	// size of the matching
	static int msize = 0;

	/**
	 * Performing maximum matching of the bipartite graph and printing the
	 * result
	 * 
	 * @param g
	 *            : Graph
	 * 
	 * @return : size of maximum matching
	 */
	public static int maximumBipartiteMatching(Graph g) {

		ArrayList<Vertex> Q = new ArrayList<>();// a queue of the free outer
		// edges
		checkBipartite(g);// updating outer and inne nodes
		if (hasCycle) {
			System.out.println("The graph is not Bipartite");
		} else {
			// Step 3 : Matching using greedy approach
			for (Edge e : g.edges) {
				if (e.From.mate == null && e.To.mate == null) {
					e.From.mate = e.To;
					e.To.mate = e.From;
					msize++;
				}
			}
			// step 4 : finding maximum matching
			while (true) {
				for (Vertex u : g) {// initialization
					u.seen = false;
					u.parent = null;
					u.freeze = false;
					// adding all the outer free nodes to the queue
					// False color means outer node and True means inner node
					if (u.mate == null && !u.color) {
						u.seen = true;
						Q.add(u);
					}

				}
				int freeInnerNodes = 0;
				while (!Q.isEmpty()) {
					Vertex u = Q.remove(0);// outer node
					for (Edge e : u.Adj) {
						Vertex v = e.otherEnd(u);// inner node
						if (!v.seen) {
							v.seen = true;
							v.parent = u;
							// found a free inner node, then process augmenting
							// path
							if (v.mate == null) {
								processAugmentingPath(v);
								freeInnerNodes++;
								break;

							} else {// finding matched outer node and adding to
									// queue
								Vertex x = v.mate;
								x.parent = v;
								x.seen = true;
								Q.add(x);
							}
						}
					}
				}
				// if no free inner nodes found in one iteration then break
				if (freeInnerNodes == 0)
					break;

			}
			// System.out.println(msize);
		}

		return msize;

	}

	/**
	 * Increasing size of matching by processing the augmenting path found
	 * 
	 * @param u
	 *            : free inner node
	 */
	public static void processAugmentingPath(Vertex u) {

		// flag to check if the augmenting path has a node that is already
		// frozen
		boolean freeze = false;
		Vertex vt = u;// outer node
		// parent of the free outer node
		while (vt != null) {
			// if the node reached through this augmenting path is already
			// frozen, then break
			if (vt.freeze == true) {
				freeze = true;
				break;

			}
			vt = vt.parent;
		}
		// if the augmenting path does not have any conflicting frozen nodes in
		// its path, only then process the augmenting path
		if (!freeze) {
			// alternate path found, matching alternate edges of the path
			Vertex p = u.parent;
			Vertex x = p.parent;
			p.freeze = true;
			x.freeze = true;
			u.mate = p;
			p.mate = u;

			while (x != null) {
				Vertex nmx = x.parent;
				Vertex y = nmx.parent;
				if (nmx != null)
					nmx.freeze = true;
				if (y != null)
					y.freeze = true;
				x.mate = nmx;
				nmx.mate = x;
				x = y;
			}

			msize++;
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		boolean VERBOSE = false;

		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}
		if (args.length > 1) {
			VERBOSE = true;
		}
		Graph g = Graph.readGraph(in, false); // read undirected graph from
												// stream "in"
		// Create your own class and call the function to find a maximum
		// matching.
		int result = maximumBipartiteMatching(g);
		System.out.println(result);
		if (VERBOSE) {
			// Output the edges of M.
			printMatching(g);
		}
	}
}

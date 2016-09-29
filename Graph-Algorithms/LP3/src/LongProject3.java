import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author divyanshu,aditya,radhika,shweta
 *
 */
public class LongProject3 {

	public static int wsp = 0;// weight of edges of shortest-path
	public static final int INF = Integer.MAX_VALUE;// infinity

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}
		// Read the graph
		Graph g = Graph.readGraph(in, true);
		Graph d = copyGraph(g);
		pathCount(d);
		// set all flags with the checking conditions
		boolean hasSameWts = checkSameWeights(g);
		boolean hasNonNegativeWts = checkNonNegativeWeights(g);
		ArrayList<Vertex> temp = checkDAG(g);
		boolean DAG = temp != null ? true : false;
		boolean negativeCycle = false;
		/*
		 * If the graph has uniform weights , then run BFS Shortest path on the
		 * graph
		 */
		if (hasSameWts) {
			// BFSonGraph(g);
			BFS(g);
			printResult(g, "BFS");
		}
		/*
		 * If the graph has only non negative weights , then run Dijkstra's
		 * Shortest path on the graph
		 */
		else if (!hasSameWts && hasNonNegativeWts) {
			Dijkstra(g);
			printResult(g, "Dij");
		}
		/*
		 * If the graph is a DAG , then run DAG Shortest path on the
		 * 
		 */
		else if (DAG) {
			DAGShortestPath(g, temp);
			printResult(g, "DAG");
		}
		/*
		 * If the graph doesn't follow any of the check conditions, run Bellman
		 * Ford for shortest path on the graph
		 */
		else if (!hasSameWts && !hasNonNegativeWts && !DAG) {
			negativeCycle = !(bellmanFord(g, g.verts.get(1)));
			printResult(g, "BF");
		}
		/*
		 * If negative cycle exists which is obtained from Bellman Ford
		 * algorithm, the problem can't be solved as it will go in indefinite
		 * loop.
		 */
		else if (negativeCycle)
			System.out.println("Unable to solve problem. Graph has a negative cycle");

	}

	/**
	 * If graph has uniform weight edges we find the MST using Breadth First
	 * Search algorithm
	 * 
	 * @param g
	 *            Graph
	 */
	public static void BFS(Graph g) {
		for (Vertex v : g) {
			v.distance = Integer.MAX_VALUE;
			v.seen = false;
			v.parent = null;
		}
		Queue<Vertex> Q = new LinkedList<>();
		Vertex src = g.verts.get(1);
		src.distance = 0;
		Q.add(src);
		src.seen = true;

		while (!Q.isEmpty()) {
			Vertex u = Q.remove();
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (!v.seen) {
					v.seen = true;
					Q.add(v);
				}
				if (v.distance > u.distance + e.Weight) {
					v.distance = u.distance + e.Weight;
					v.parent = u;
				}

			}
		}
	}

	public static Graph copyGraph(Graph g) {
		Graph d = new Graph(g.numNodes);
		for (Vertex u : g) {
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (u.distance + e.Weight == v.distance) {
					d.addDirectedEdge(u.name, v.name, e.Weight);
				}
			}
		}
		return d;
	}

	public static void pathCount(Graph d) {
		for (Vertex u : d.verts) {
			if (u == null)
				continue;
			for (Edge e : u.revAdj) {
				Vertex v = e.otherEnd(u);
				if (v == d.verts.get(1)) {
					u.spCount = 1;
				} else {
					u.spCount = u.spCount + v.spCount;
				}
			}
		}
	}

	public static void printOutput() {

	}

	/**
	 * 
	 * Finds shortest path for graph using Dijkstra's algorithm.
	 * 
	 * Input Specification: Directed graph with non-negative weights.
	 * 
	 * Root node = 1.
	 * 
	 * @param g
	 */
	public static void Dijkstra(Graph g) {
		Vertex src = g.verts.get(1);

		// initialize
		for (Vertex u : g.verts) {
			if (u == null) {
				continue;
			}
			u.seen = false;
			u.distance = Integer.MAX_VALUE;
			u.parent = null;
		}
		src.distance = 0;

		ComparatorVertex ce = new ComparatorVertex();
		IndexedHeap<Vertex> q = new IndexedHeap<>(g.numNodes, ce);
		q.add(src);// adding src
		while (q.size != 0) {
			Vertex u = q.remove();
			u.seen = true;
			// relax edges out of u
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if ((v.distance > u.distance + e.Weight) && !v.seen) {
					v.distance = u.distance + e.Weight;
					v.parent = u;
					if (v.index == -1)
						q.add(v);
					q.decreaseKey(v);
				}
			}
		}

	}

	/**
	 * Finds shortest path for graph On Directed Acyclic graph.
	 * 
	 * Input Specification: Directed graph without cycle.
	 * 
	 * Root node = 1.
	 * 
	 * In DAG Shortest path, we first find the topological ordering of the
	 * nodes. For every node in the topological ordering, the distance is
	 * compared and the shortest distance id assigned to vertex.
	 * 
	 * @param g
	 */
	public static void DAGShortestPath(Graph g, ArrayList<Vertex> list) {

		// Get the topological ordering of the graph in a list
		// List<Vertex> list = topologicalOrdering(g);

		Vertex source = g.verts.get(1); // source vertex 1
		/*
		 * Initializing the distance for each vertex to be Infinity
		 * 
		 */
		for (Vertex u : g.verts) {
			if (u == null) {
				continue;
			}
			u.seen = false;
			u.distance = Integer.MAX_VALUE;
			u.parent = null;
		}
		// setting the distance of the source node to be 0
		source.distance = 0;
		/*
		 * Iterate through all the vertices in the list and compare the distance
		 * of the node with the summation of the distance of its parent and the
		 * edge connecting the vertex with the parent if the later is greater,
		 * assign the distance of the vertex with the same.
		 * 
		 */
		int i;

		for (i = 0; i < list.size(); i++) {
			if (list.get(i).name == g.verts.get(1).name)
				break;

		}
		for (int j = i; j < list.size(); j++) {
			Vertex u = list.get(j);
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (v.distance > u.distance + e.Weight) {
					v.distance = u.distance + e.Weight;
					v.parent = u;
				}
			}
		}

	}

	/**
	 * The topologicalOrdering returns the topological order in DFS manner of
	 * the graph given
	 * 
	 */
	public static List<Vertex> topologicalOrdering(Graph graph) {

		Stack<Vertex> topOrder = new Stack<Vertex>();
		List<Vertex> list = new LinkedList<Vertex>();

		for (Vertex u : graph.verts) {
			if (u == null)
				continue;
			u.seen = false;
			u.parent = null;
		}

		for (int i = 1; i <= graph.numNodes; i++) {
			if (!graph.verts.get(i).seen) {
				topOrder = DFSVisit(graph.verts.get(i), topOrder);
			}
		}

		// for (int i = topOrder.size() - 1; i >= 0; --i) {
		// list.add(topOrder.elementAt(i));
		// }
		for (int i = topOrder.size() - 1; i >= 0; --i) {
			list.add(topOrder.pop());
		}
		return list;

	}

	/**
	 * Function which is used for every visit of a vertex in a graph
	 * 
	 * @param vertex
	 * @param stack
	 * @return
	 */
	public static Stack<Vertex> DFSVisit(Vertex vertex, Stack<Vertex> stack) {
		vertex.seen = true;

		for (Edge e : vertex.Adj) {
			if (!e.otherEnd(vertex).seen) {
				e.otherEnd(vertex).parent = vertex;
				DFSVisit(e.otherEnd(vertex), stack);
			}
		}

		stack.push(vertex);
		return stack;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static <T> void addToQueue(ArrayList<T> Q, T element) {
		Q.add(element);
	}

	public static <T> T popFromQueue(ArrayList<T> Q) {
		T temp = Q.get(Q.size() - 1);
		Q.remove(Q.size() - 1);
		return temp;
	}

	static boolean bellmanFord(Graph g, Vertex source) {
		ArrayList<Vertex> Q = new ArrayList<Vertex>();
		int numOfVertices = 0;
		for (Vertex v : g) {
			v.distance = Integer.MAX_VALUE;
			v.parent = null;
			v.count = 0;
			v.seen = false;
			numOfVertices += 1;
		}
		source.distance = 0;
		source.seen = true;
		addToQueue(Q, source);

		while (Q.size() != 0) {
			Vertex u = popFromQueue(Q);
			u.seen = false;
			u.count += 1;
			if (u.count >= numOfVertices) {
				// negative cycle
				return true;
			}
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (v.distance > u.distance + e.Weight) {
					v.distance = u.distance + e.Weight;
					v.parent = u;
					if (v.seen == false) {
						addToQueue(Q, v);
						v.seen = true;
					}
				}
			}

		}

		return false;
	}

	/**
	 * Prints total distance of shortest path for all nodes.
	 * 
	 * Prints distance for each node along with parent when size <= 100.
	 * 
	 * @param g
	 */
	public static void printResult(Graph g, String algorithm) {

		for (Vertex u : g.verts) {// adding total weight of shortest path
			if (u == null || u.distance == Integer.MAX_VALUE)
				continue;
			wsp += u.distance;
		}

		System.out.println(algorithm + " " + wsp);// total distance of shortest
													// path for
		// each node

		if (g.verts.size() <= 101) {// printing the result when total nodes less
									// than 100
			for (Vertex u : g.verts) {
				if (u == null)
					continue;
				if (u.parent == null) {// when parent is null
					if (u.distance == Integer.MAX_VALUE) // when distance is INF
						System.out.println(u.name + " " + "INF" + " " + "-");
					else
						System.out.println(u.name + " " + u.distance + " " + "-");
				} else
					System.out.println(u.name + " " + u.distance + " " + u.parent);
			}
		}

	}

	/**
	 * This methods checks whether all the edges in a graph have same weight. If
	 * all the weights are same, it returns true, else it returns false
	 */
	public static boolean checkSameWeights(Graph graph) {

		Vertex u = graph.verts.get(1);
		Edge edge = null;
		for (Edge e : u.Adj) {
			edge = e;
			break;
		}

		// Edge edge = u.Adj.get(1);
		int weight = edge.Weight;

		for (Vertex vt : graph.verts) {
			if (vt == null)
				continue;
			for (Edge e : vt.Adj) {
				if (e.Weight != weight) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * This method checks whether all edges have non negative weights. If it has
	 * all non - negative edges it returns true, else it returns false
	 */
	public static boolean checkNonNegativeWeights(Graph graph) {

		for (Vertex vt : graph.verts) {
			if (vt == null)
				continue;
			for (Edge e : vt.Adj) {
				if (e.Weight < 0) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Finds topological order
	 * 
	 * @param g
	 * @return - list of all vertices in topological order or null
	 */
	public static ArrayList<Vertex> checkDAG(Graph g) {
		ArrayList<Vertex> list = new ArrayList<>();
		ArrayList<Vertex> q = new ArrayList<>();
		g.verts.get(1).inDegree = 0;
		for (Vertex u : g) {
			if (u.inDegree == 0) {
				addToQueue(q, u);
			}
		}

		while (!(q.isEmpty())) {
			Vertex u = popFromQueue(q);
			list.add(u);
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				v.inDegree--;
				if (v.inDegree == 0)
					addToQueue(q, v);
			}
		}
		if (list.size() != g.numNodes) {
			return null;
		}
		return list;
	}

	/**
	 * This function checks whether the graph is a DAG. So a graph to be a DAG,
	 * it must have atleast one vertex which has inDegree = 0. If there exist no
	 * such vertex for which inDegree is not 0, then the graph is not a DAG.
	 */
	public static boolean checkForDAG(Graph graph) {
		int count = 0;
		for (int i = 1; i <= graph.numNodes; i++) {

			if (graph.verts.get(i).inDegree == 0) {
				count++;
			}
		}
		if (count != 0)
			return true;
		else
			return false;
	}
}

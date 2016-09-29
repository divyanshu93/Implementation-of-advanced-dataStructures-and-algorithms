
/*
 * Group 22 :
 * Names : Radhika Simant 
 *         Divyanshu Paliwal 
 *         Aditya Mahajan
 *         Shweta Nair
 * 
 * 
 * This class has the implementation of level 2 of Long Project - 3
 * 
 * 
 * */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author divyanshu,aditya,radhika,shweta
 *
 */
public class LP3_Level2 {

	public static final int INF = Integer.MAX_VALUE;

	public static int sum = 0; // sum of all the shortest paths in a graph

	public static void main(String[] args) throws FileNotFoundException {

		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}

		System.out.println("Input the graph : ");
		Graph graph = readGraph(in, true);
		Vertex source = graph.verts.get(1);
		// check for negative cycles, if negative cycle exists then return the
		// negative cycle
		boolean negativeCycle = checkNegativeCycle(graph, source);
		if (negativeCycle) {
			System.out.println("Non-positive cycle in graph. DAC is not applicable");
		} else {
			// if no negative cycle exists then find all the shortest paths for
			// each vertex
			level2(graph);
			printResult(graph);

		}
	}

	/*
	 * Function to find the different shortest paths in a graph and printing the
	 * summation of all the shortest paths in the graph
	 * 
	 */
	public static void level2(Graph g) {

		Vertex source = g.verts.get(1);

		/**
		 * Initializing the vertices of the graph
		 */
		for (Vertex u : g.verts) {
			if (u == null) {
				continue;
			}
			u.seen = false;
			u.distance = INF;
			u.spCount = 1;
			u.parent = null;
		}
		// setting the distance of the source node to be 0
		source.distance = 0;
		// Get the topological order of the graph
		List<Vertex> list = topologicalOrdering(g);
		// initialize the source in the topologically ordered list with the
		// source vertex
		int i;
		for (i = 0; i < list.size(); i++) {
			if (list.get(i).name == source.name)
				break;

		}
		/*
		 * For each node in the topological order, find the shortest distance.
		 * And compute the number of shortest paths to each node
		 * 
		 * Case 1) If the vertex is not seen, set the distance as the distance
		 * till parent and the weight of the node Case 2) If the vertex id
		 * already seen then i) If distance from current node is equal to its
		 * distance already obtained then that means, another shortest path was
		 * discovered for this node hence, increase the spCount of the current
		 * node with the spCount of the parent node ii) If the distance from
		 * current node is less than the existing distance, update the distance
		 * as also, update the spCount with the spCount of the parent vertex.
		 * 
		 */
		for (int j = i; j < list.size(); j++) {
			Vertex u = list.get(j);
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (!v.seen) {

					v.distance = u.distance + e.Weight;
					v.spCount = u.spCount;
					v.seen = true;

				}
				if (v.seen) {
					if (v.distance == u.distance + e.Weight) {
						v.spCount = v.spCount + u.spCount;
					}

					if (v.distance > u.distance + e.Weight) {
						v.distance = u.distance + e.Weight;
						v.spCount = u.spCount;
					}
				}

			}
		}

	}

	public static Graph readGraph(Scanner in, boolean directed) {
		// read the graph related parameters
		int n = in.nextInt(); // number of vertices in the graph
		int m = in.nextInt(); // number of edges in the graph

		// create a graph instance
		Graph g = new Graph(n);
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();

			if (directed) {
				g.addDirectedEdge(u, v, w);
			} else {
				g.addEdge(u, v, w);
			}
		}

		in.close();

		return g;
	}

	/*
	 * Function to add element to the queue
	 */
	public static <T> void addToQueue(ArrayList<T> Q, T element) {
		Q.add(element);
	}

	/*
	 * Function to remove element from the queue
	 */
	public static <T> T popFromQueue(ArrayList<T> Q) {
		T temp = Q.get(Q.size() - 1);
		Q.remove(Q.size() - 1);
		return temp;
	}

	/*
	 * Function to detect negative cycle in a graph If negative cycle exists
	 * return true else return false
	 * 
	 * If a negative cycle exists, then print the cycle
	 * 
	 */
	public static boolean checkNegativeCycle(Graph graph, Vertex source) {
		ArrayList<Vertex> Q = new ArrayList<Vertex>();
		int numOfVertices = 0;

		for (Vertex v : graph) {
			v.distance = INF;
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
				printCycle(graph, u);
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

	/*
	 * Function to print the cycle.
	 */
	static void printCycle(Graph g, Vertex src) {

		Vertex temp;
		Edge e, t;
		temp = src.parent;
		while (temp != src) {
			for (int i = 0; i < temp.revAdj.size(); i++) {
				if (temp.parent == temp.revAdj.get(i).otherEnd(temp)) {
					e = temp.revAdj.get(i);
					if (e != null) {
						System.out.println(e + "");
						temp = temp.parent;
					}
					break;
				}
			}

		}
		System.out.println("(" + src.parent.name + " " + temp.name + ")");

	}

	/*
	 * Function to find the topological order of the graph
	 */
	public static List<Vertex> topologicalOrdering(Graph graph) {

		Stack<Vertex> topOrder = new Stack<Vertex>();
		List<Vertex> list = new LinkedList<Vertex>();

		for (int i = 1; i <= graph.numNodes; i++) {
			graph.verts.get(i).seen = false;
			graph.verts.get(i).parent = null;
		}

		for (int i = 1; i <= graph.numNodes; i++) {
			if (!graph.verts.get(i).seen) {
				topOrder = DFSVisit(graph.verts.get(i), topOrder);
			}
		}

		for (int i = topOrder.size() - 1; i >= 0; --i) {
			list.add(topOrder.elementAt(i));
		}
		return list;

	}

	// Function which is used for every visit of a vertex in a graph
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

	/*
	 * Function to print the results
	 */
	public static void printResult(Graph g) {

		for (Vertex u : g.verts) {
			if (u == null || u.distance == INF)
				continue;
			sum = sum + u.spCount;
		}

		System.out.println(sum);

		if (g.verts.size() <= 101) {
			for (Vertex u : g.verts) {
				if (u == null)
					continue;

				if (u.distance == INF) // when distance is INF
					System.out.println(u.name + " " + "INF" + " " + 0);
				else
					System.out.println(u.name + " " + u.distance + " " + u.spCount);
			}
		}

	}
}

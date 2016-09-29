import java.util.Scanner;

/**
 * @author divyanshu,aditya,radhika,shweta
 *
 */
public class Dijkstra {

	public int wsp = 0;// weight of edges of shortest-path

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
	public Dijkstra(Graph g) {
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
					q.add(v);
					q.decreaseKey(v);
				}
			}
		}

	}

	/**
	 * Prints total distance of shortest path for all nodes.
	 * 
	 * Prints distance for each node along with parent when size <= 100.
	 * 
	 * @param g
	 */
	public void printResult(Graph g) {

		for (Vertex u : g.verts) {// adding total weight of shortest path
			if (u == null || u.distance == Integer.MAX_VALUE)
				continue;
			wsp += u.distance;
		}

		System.out.println("Dij " + wsp);// total distance of shortest path for
											// each node

		if (g.verts.size() <= 100) {// printing the result when total nodes less
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

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Dijkstra's");

		Graph g = Graph.readGraph(in, true);
		Dijkstra d = new Dijkstra(g);
		d.printResult(g);
	}

}

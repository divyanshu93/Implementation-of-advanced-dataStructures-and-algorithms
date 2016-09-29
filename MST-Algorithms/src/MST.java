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

public class MST {

	/**
	 * @param g
	 * @return weight of minimum spanning tree.
	 *
	 */
	static int PrimMSTBinary(Graph g) {
		int wmst = 0;
		Vertex src = g.verts.get(1);

		// Code for Prim's algorithm
		for (Vertex u : g) {
			u.seen = false;
			u.parent = null;
		}
		src.seen = true;
		// comparator for comparing edges.
		ComparatorEdge ce = new ComparatorEdge();
		BinaryHeap<Edge> q = new BinaryHeap<>(g.noOfEdges, ce);
		Vertex temp;
		for (Edge e : src.Adj) {
			q.add(e);
		}
		while (q.size > 0) {

			Edge e = q.remove();
			if (e.From.seen && e.To.seen) {
				continue;
			}

			if (e.From.seen == true) {
				temp = e.To;
				e.To.seen = true;
			} else {
				temp = e.From;
				e.From.seen = true;
			}

			wmst = wmst + e.Weight;

			for (Edge f : temp.Adj) {
				Vertex w = f.otherEnd(temp);
				if (!w.seen) {
					q.add(f);
				}
			}
		}

		return wmst;
	}

	/**
	 * @param g (Graph)
	 * @return weight of minimum spanning tree.
	 *
	 */
	static int PrimMSTIndexed(Graph g) {
		int wmst = 0;
		Vertex src = g.verts.get(1);

		// Code for Prim's algorithm needs to be written
		for (Vertex u : g) {
			u.seen = false;
			u.parent = null;
			u.d = Integer.MAX_VALUE;
		}
		src.d = 0;
		// comparator for comparing edges.
		ComparatorVertex ce = new ComparatorVertex();
		IndexedHeap<Vertex> q = new IndexedHeap<>(g.numNodes, ce);
		
		for (Vertex u : g) {
			q.add(u);
		}

		while (q.size > 0) {
			Vertex u = q.remove();
			u.seen = true;
			wmst = wmst + u.d;
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (!v.seen && e.Weight < v.d) {
					v.d = e.Weight;
					v.parent = u;
					q.percolateUp(v.getIndex());
				}
			}
		}

		return wmst;
	}
	
	
	/**
	 * @param u Vertex
	 * @desc Initialize the vertex u
	 */
	static void MakeSet(Vertex u) {
		u.parent = u;
		u.rank = 0;
		u.seen = false;
	}
	
	
	/**
	 * @param u Vertex
	 * @desc Find the parent of the current vertex.
	 * @return x.parent
	 */
	static Vertex Find(Vertex x) {
		if (x != x.parent) {
			x.parent = Find(x.parent);
		}
		return x.parent;
	}

	/**
	 * @param x,y Vertices
	 * @desc Union on the pair of vertices based on rank.
	 */
	static void Union(Vertex x, Vertex y) {
		if (x.rank > y.rank)
			y.parent = x;
		else if (x.rank < y.rank)
			x.parent = y;
		else {
			x.parent = y;
			y.rank++;
		}

	}

	/**
	 * @param g
	 * @return weight of minimum spanning tree
	 */
	static int Kruskals(Graph g) {
		ComparatorEdge ce = new ComparatorEdge();
		ArrayList<Edge> edges = new ArrayList<>();
		int wmst = 0;
		
		//Initialize all the vertices in the graph
		for (Vertex u : g) {
			MakeSet(u);
		}
		
		//sort all the edges in non-decreasing order 
		BinaryHeap<Edge> q = new BinaryHeap<>(g.noOfEdges, ce);
		for (Edge e : g.edges) {
			q.add(e);
		}
		int i = g.noOfEdges;
		// add edges in non-decreasing order to list 'edges'
		while (i >= 1) {
			edges.add(q.remove());
			i--;
		}
		//Kruskal's Find and Union algorithm step
		for (Edge e : edges) {
			Vertex ru = Find(e.From); //root of u
			Vertex rv = Find(e.To); //root of v
			if (ru != rv) {
				wmst = wmst + e.Weight;
			}
			Union(ru, rv);
		}

		return wmst;

	}
}

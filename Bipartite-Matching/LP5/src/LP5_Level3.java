import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class LP5_Level3 {

	public static boolean hasCycle;

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		// flag used to show the matchings
		Boolean showmatching = true;
		if (args.length > 0) {
			if (args.length > 1)
				showmatching = true;
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}
		Graph g = Graph.readGraph(in, false);
		// maximumBipartiteMatching(g);
		weightedBipartiteMatching(g);
		if (showmatching)
			printMatching(g);
	}

	public static void markUnseen(Graph g) {
		for (Vertex v : g) {
			v.seen = false;
		}
	}

	public static int getWeight(Vertex v1, Vertex v2) {
		for (Edge e : v1.Adj) {
			if (e.otherEnd(v1) == v2) {
				return e.Weight;
			}
		}
		return -1;
	}

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

	public static void dfs(Vertex v) {
		v.seen = true;
		for (Edge e : v.Adj) {
			Vertex u = e.otherEnd(v);
			if (!u.seen) {
				u.color = !v.color;// updating color of vertex u to mark it as
									// inner.
				dfs(u);
			}
			// if the vertex and it's adjacent vertex have the same color then
			// we have got a cycle
			if (u.color == v.color) {
				hasCycle = true;
			}
		}
	}

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

	static int weightSum = 0;// max weight matching of the graph

	public static void weightedBipartiteMatching(Graph g) {

		boolean notPerfectmatching = false;
		checkBipartite(g);
		if (hasCycle == true) {
			System.out.println("Graph is not Bipartite");
			return;
		}

		// updating labels for vertices of the graph only for outer nodes
		// <-- This should be done only once, after that inside while(true)
		// don't need to do it again I guess
		for (Vertex u : g) {
			if (u.color) // inner node
				continue;
			int max = 0;
			for (Edge e : u.Adj) {// all edges of outer node
				if (e.Weight >= max) {
					max = e.Weight;
					// e.maxWeight = true;// error here
				}
			}
			for (Edge e : u.Adj) {
				if (e.Weight == max) {
					e.maxWeight = true;
					System.out.println("edge in new graph:" + e);
				}
			}
			u.label = max;// update label with max weight of adj edges
		}

		while (true) {
			for (Edge e : g.edges)
				e.maxWeight = false;

			maximumBipartiteMatching(g);
			int freeOuterCount = 0;
			for (Vertex u : g) {
				if (u.color && u.mate == null) {
					notPerfectmatching = true;
					System.out.println("I came here");
					break;
				} else if (!u.color && u.mate == null) {
					freeOuterCount++;
					if (u.label == 0)
						freeOuterCount--;
				}
			}
			// System.out.println("## " + freeOuterCount);
			if (freeOuterCount == 0)
				notPerfectmatching = true;
			if (notPerfectmatching == false) {
				System.out.println("Perfect matching complete, max weight is: " + weightSum);
				return;
			}
			// Case when no perfect matching is found
			Vertex freeOuter = null;
			for (Vertex u : g) {// finding free outer node in the graph
				if (!u.color && u.mate == null) {
					freeOuter = u;
					break;
				}
			}
			System.out.println(freeOuter);
			if (freeOuter == null) {// if there is no free outer node, then
									// break
				// System.out.println("No free outer node, How did this
				// happen?");
				System.exit(0);
			}

			// not for loop, we need a recursive or iterative way to iterate
			// through all of the tree

			// list of outer nodes in the tree containing free outer node
			ArrayList<Vertex> outerNodes = new ArrayList<>();
			// list of inner nodes in the tree containing free inner node
			ArrayList<Vertex> innerNodes = new ArrayList<>();
			outerNodes.add(freeOuter);

			Vertex outer = freeOuter;
			Vertex inner = null;

			for (Vertex v : g) {
				v.seen = false;
			}
			// if already visited this outer node, then don't iterate further
			while (outer.seen != true) {
				System.out.println("In sec while");
				// System.out.println("$$ Outer " + outer + " " + outer.seen);
				outer.seen = true;
				inner = null;
				for (Edge e : outer.Adj) {
					// System.out.println("outers adj edge:" + e);
					if (e.maxWeight && e.otherEnd(outer).seen != true) {
						inner = e.otherEnd(outer);
						// System.out.println("inner:" + inner);
					}
				}
				if (inner == null)
					break;
				inner.seen = true;
				innerNodes.add(inner);
				System.out.println("" + innerNodes);
				System.out.println("##" + inner.Adj);
				for (Edge e : inner.Adj) {
					System.out.println("inners adj edge:" + e);
					if (e.maxWeight && e.otherEnd(inner).seen != true) {
						outer = e.otherEnd(inner);
						outer.seen = true;
						// System.out.println("outer:" + outer + ":" +
						// outer.seen);
						outerNodes.add(outer);
					}
				}

				// System.out.println("" + outerNodes);
			}

			// integer array containing L(u)+L(v) - w(u,v) of edges not in T
			// connecting outer nodes in T
			// int[] slack = new int[g.numNodes];
			ArrayList<Integer> slack = new ArrayList<>();
			// int i = 0;// position of last entry in slack array
			// adding weights to the slack array
			for (Vertex u : outerNodes) {
				// slack[i] = u.label;
				slack.add(u.label);
				// i++;
				for (Edge e : u.Adj) {
					if (!e.maxWeight) {
						// System.out.println("Edge " + e);
						// slack[i] = u.label + e.otherEnd(u).label - e.Weight;
						slack.add(u.label + e.otherEnd(u).label - e.Weight);
						// System.out.println("@@" + slack[i]);
						// i++;
					}
				}
			}

			Collections.sort(slack);
			int minSlack = slack.get(0);
			// System.out.println("min slack" + minSlack);
			// decreasing outer nodes & inc inner nodes in T by minSlack
			for (Vertex u : outerNodes) {
				u.label = u.label - minSlack;
				// System.out.println("outer slack " + u.label);
			}
			for (Vertex u : innerNodes) {
				u.label = u.label + minSlack;
				// System.out.println("inner slack " + u.label);
			}
		}

	}

	public static int maximumBipartiteMatching(Graph g) {

		ArrayList<Vertex> Q = new ArrayList<>();// a queue of the free outer

		// Step 3 : Matching using greedy approach
		for (Edge e : g.edges) {
			if (e.From.mate == null && e.To.mate == null) {
				e.From.mate = e.To;
				e.To.mate = e.From;
				weightSum = weightSum + e.Weight;
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

		return weightSum;

	}

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

			weightSum++;
		}
	}
}

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class SAP {
	private Digraph g;
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		g = new Digraph(G.V());
		for (int i=0; i<G.V(); i++) {
			for (int j : G.adj(i)) {
				g.addEdge(i,j);
			}
		}
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		DeluxeBFS bfs1 = new DeluxeBFS(g, v);
		DeluxeBFS bfs2 = new DeluxeBFS(g, w);

		TreeSet<Integer> visit1 = bfs1.getVisit();
		TreeSet<Integer> visit2 = bfs2.getVisit();

		visit1.retainAll(visit2);

		int min = -1;

		for (Integer x : visit1) {
			if (min == -1 || bfs1.distTo(x) + bfs2.distTo(x) < min) {
				min = bfs1.distTo(x) + bfs2.distTo(x);
			}
		}

		return min;
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		DeluxeBFS bfs1 = new DeluxeBFS(g, v);
		DeluxeBFS bfs2 = new DeluxeBFS(g, w);

		TreeSet<Integer> visit1 = bfs1.getVisit();
		TreeSet<Integer> visit2 = bfs2.getVisit();

		visit1.retainAll(visit2);

		int min = 2147483647;
		int minId = -1;

		for (Integer x : visit1) {
			if (bfs1.distTo(x) + bfs2.distTo(x) < min) {
				min = bfs1.distTo(x) + bfs2.distTo(x);
				minId = x;
			}
		}

		return minId;
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) {
			throw new java.lang.IllegalArgumentException();
		}

		for (Integer vv : v) {
			if (vv == null) {
				throw new java.lang.IllegalArgumentException();
			}
		}

		for (Integer ww : w) {
			if (ww == null) {
				throw new java.lang.IllegalArgumentException();
			}
		}

		DeluxeBFS bfs1 = new DeluxeBFS(g, v);
		DeluxeBFS bfs2 = new DeluxeBFS(g, w);

		TreeSet<Integer> visit1 = bfs1.getVisit();
		TreeSet<Integer> visit2 = bfs2.getVisit();

		visit1.retainAll(visit2);

		int min = -1;

		for (Integer x : visit1) {
			if (min == -1 || bfs1.distTo(x) + bfs2.distTo(x) < min) {
				min = bfs1.distTo(x) + bfs2.distTo(x);
			}
		}

		return min;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) {
			throw new java.lang.IllegalArgumentException();
		}

		for (Integer vv : v) {
			if (vv == null) {
				throw new java.lang.IllegalArgumentException();
			}
		}

		for (Integer ww : w) {
			if (ww == null) {
				throw new java.lang.IllegalArgumentException();
			}
		}

		DeluxeBFS bfs1 = new DeluxeBFS(g, v);
		DeluxeBFS bfs2 = new DeluxeBFS(g, w);

		TreeSet<Integer> visit1 = bfs1.getVisit();
		TreeSet<Integer> visit2 = bfs2.getVisit();

		visit1.retainAll(visit2);

		int min = 2147483647;
		int minId = -1;

		for (Integer x : visit1) {
			if (bfs1.distTo(x) + bfs2.distTo(x) < min) {
				min = bfs1.distTo(x) + bfs2.distTo(x);
				minId = x;
			}
		}

		return minId;
	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In("a.txt");
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length   = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}

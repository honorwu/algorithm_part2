import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;

public class BoggleSolver {
	private Node root;
	private static final int R = 26;
	private int x[] = {-1,1,0,0,-1,-1,1,1};
	private int y[] = {0,0,-1,1,-1,1,-1,1};

	private class MyStringBuilder {
		char[] value;
		private int count;

		MyStringBuilder(int capacity) {
			value = new char[capacity];
		}

		public int length() {
			return count;
		}

		public String toString() {
			// Create a copy, don't share the array
			return new String(value, 0, count);
		}

		public void append(char c) {
			value[count++] = c;
			if (c == 'Q') {
				value[count++] = 'U';
			}
		}

		public void deleteLast(int len) {
			count -= len;
		}
	}

	private class Node {
		private Node[] next;
		private boolean isString;

		public boolean isString() {
			return isString;
		}

		private Node() {
			this.next = new Node[R];
		}
	}

	private boolean contains(String key) {
		if (key == null) {
			throw new IllegalArgumentException("argument to contains() is null");
		} else {
			Node x = this.get(root, key, 0);
			return x == null ? false : x.isString;
		}
	}

	private Node get(Node x, String key, int d) {
		if (x == null) {
			return null;
		} else if (d == key.length()) {
			return x;
		} else {
			int c = key.charAt(d) - 'A';
			return this.get(x.next[c], key, d + 1);
		}
	}

	private void add(String key) {
		if (key == null) {
			throw new IllegalArgumentException("argument to add() is null");
		} else {
			this.root = this.add(this.root, key, 0);
		}
	}

	private Node add(Node x, String key, int d) {
		if (x == null) {
			x = new Node();
		}

		if (d == key.length()) {
			x.isString = true;
		} else {
			int c = key.charAt(d) - 'A';
			x.next[c] = this.add(x.next[c], key, d + 1);
		}

		return x;
	}

	// Initializes the data structure using the given array of strings as the dictionary.
	// (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		for (int i=0; i<dictionary.length; i++) {
			add(dictionary[i]);
		}
	}

	// Returns the set of all valid words in the given Boggle board, as an Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		int row = board.rows();
		int col = board.cols();
		boolean [][] visited = new boolean[row][col];
		char [] b = new char[row*col];

		int id=0;
		for (int i=0; i<row; i++) {
			for (int j=0; j<col; j++) {
				b[id++] = board.getLetter(i,j);
			}
		}

		HashSet<String> s = new HashSet<>();
		MyStringBuilder word = new MyStringBuilder(row * col * 2);

		for (int i=0; i<row; i++) {
			for (int j=0; j<col; j++) {
				visited[i][j] = true;
				dfs(i,j, board, s, visited, word, root);
				visited[i][j] = false;
			}
		}

		return s;
	}

	private boolean check(int i,int j, boolean [][] visited) {
		if (i<0 || i >= visited.length) {
			return false;
		}

		if (j<0 || j >= visited[0].length) {
			return false;
		}

		return !visited[i][j];
	}

	private Node cut(int append, Node node, char c) {
		if (append == 1 && node != null) {
			return node.next[c-'A'];
		}

		if (append == 2 && node != null) {
			node = node.next['Q'-'A'];
			if (node != null) {
				return node.next['U'-'A'];
			}
		}

		return null;
	}

	private void dfs(int i, int j, BoggleBoard board, HashSet<String> s, boolean [][] visited, MyStringBuilder word, Node node) {
		char c = board.getLetter(i,j);
		int append = 1;
		if (c == 'Q') {
			append = 2;
		}

		Node n = cut(append, node,c);

		if (n == null) {
			return;
		}

		word.append(c);
		if (word.length() >= 3 && n.isString()) {
			s.add(word.toString());
		}

		for (int k=0; k<8; k++) {
			int ni=i+x[k];
			int nj=j+y[k];
			if (check(ni, nj, visited)) {
				visited[ni][nj] = true;
				dfs(ni, nj, board, s, visited, word, n);
				visited[ni][nj] = false;
			}
		}
		word.deleteLast(append);
	}

	// Returns the score of the given word if it is in the dictionary, zero otherwise.
	// (You can assume the word contains only the uppercase letters A through Z.)
	public int scoreOf(String word) {
		if (!contains(word)) {
			return 0;
		}

		if (word.length() <= 2) {
			return 0;
		} else if (word.length() <=4) {
			return 1;
		} else if (word.length() <=5) {
			return 2;
		} else if (word.length() <=6) {
			return 3;
		} else if (word.length() <=7) {
			return 5;
		} else {
			return 11;
		}
	}

	public static void main(String[] args) {
		In in = new In("dictionary-algs4.txt");
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard("board-q.txt");
		int score = 0;
		for (String word : solver.getAllValidWords(board)) {
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);
	}
}

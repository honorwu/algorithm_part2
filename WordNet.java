import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class WordNet {
	private Map<String, Bag<Integer>> nouns = new TreeMap<>();
	private Map<Integer, String> synsets = new TreeMap<>();
	private Set<Integer> rootSet = new TreeSet<>();
	private Digraph digraph;
	private SAP sap;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new java.lang.IllegalArgumentException();
		}

		int v = 0;

		In synsetsIn = new In(synsets);
		String line = synsetsIn.readLine();
		while (line != null) {
			v++;

			String data[] = line.split(",");
			String word[] = data[1].split(" ");
			Integer id = Integer.parseInt(data[0]);

			for (int i=0; i<word.length; i++) {
				if (!nouns.containsKey(word[i])) {
					nouns.put(word[i], new Bag<>());
				}
				nouns.get(word[i]).add(id);
			}

			this.synsets.put(id, data[1]);
			this.rootSet.add(id);

			line = synsetsIn.readLine();
		}

		digraph = new Digraph(v);

		In hypernymsIn = new In(hypernyms);
		line = hypernymsIn.readLine();
		while (line != null) {
			String e[] = line.split(",");
			rootSet.remove(Integer.parseInt(e[0]));

			for (int i=1; i<e.length; i++) {
				digraph.addEdge(Integer.parseInt(e[0]), Integer.parseInt(e[i]));

			}

			line = hypernymsIn.readLine();
		}

		if (rootSet.size() > 1) {
			throw new java.lang.IllegalArgumentException();
		}

		sap = new SAP(digraph);
		DirectedCycle directedCycle = new DirectedCycle(digraph);
		if (directedCycle.hasCycle()) {
			throw new java.lang.IllegalArgumentException();
		}
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return nouns.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null) {
			throw new java.lang.IllegalArgumentException();
		}

		return nouns.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (!isNoun(nounA)) {
			throw new java.lang.IllegalArgumentException();
		}

		if (!isNoun(nounB)) {
			throw new java.lang.IllegalArgumentException();
		}

		return sap.length(nouns.get(nounA), nouns.get(nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (!isNoun(nounA)) {
			throw new java.lang.IllegalArgumentException();
		}

		if (!isNoun(nounB)) {
			throw new java.lang.IllegalArgumentException();
		}

		int id = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
		return synsets.get(id);
	}

	// do unit testing of this class
	public static void main(String[] args) {
		WordNet wordNet = new WordNet("synsets15.txt", "hypernyms15Tree.txt");
		int d = wordNet.distance("a", "a");
		String s = wordNet.sap("a", "a");
		System.out.println(d);
		System.out.println(s);
	}
}

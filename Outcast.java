public class Outcast {
	// constructor takes a WordNet object
	private WordNet wordNet;
	public Outcast(WordNet wordnet) {
		this.wordNet = wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		int max = 0;
		int maxId = 0;
		for (int i=0; i<nouns.length; i++) {
			int sum = 0;
			for (int j=0; j<nouns.length; j++) {
				if (i!=j) {
					sum += wordNet.distance(nouns[i], nouns[j]);
				}
			}
			if (sum > max) {
				max = sum;
				maxId = i;
			}
		}
		return nouns[maxId];
	}

	// see test client below
	public static void main(String[] args) {

	}
}

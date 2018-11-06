import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
	private char array[];
	private Integer id[];
	private int length;
	// circular suffix array of s
	public CircularSuffixArray(String s) {
		if (s==null) {
			throw new java.lang.IllegalArgumentException();
		}

		length = s.length();
		array = new char [length];
		id = new Integer [length];

		for (int i=0; i<s.length(); i++) {
			array[i] = s.charAt(i);
			id[i] = i;
		}

		Arrays.sort(id, new SuffixComp());
	}

	// length of s
	public int length() {
		return length;
	}

	// returns index of ith sorted suffix
	public int index(int i) {
		if (i<0 || i >= length()) {
			throw new java.lang.IllegalArgumentException();
		}

		return id[i];
	}

	private class SuffixComp implements Comparator<Integer> {
		@Override
		public int compare(Integer o1, Integer o2) {
			for (int i=0; i<length(); i++) {
				int c1=array[(o1+i)%length];
				int c2=array[(o2+i)%length];

				if (c1 > c2) {
					return 1;
				} else if (c1 < c2) {
					return -1;
				} else {
					continue;
				}
			}
			return 0;
		}
	}

	// unit testing (required)
	public static void main(String[] args) {
		CircularSuffixArray a = new CircularSuffixArray("ABRACADABRA!");
		for (int i=0; i<a.length(); i++) {
			System.out.println(a.index(i));
		}
	}
}
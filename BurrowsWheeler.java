import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {
	// apply Burrows-Wheeler transform, reading from standard input and writing to standard output
	public static void transform() {
		String input = BinaryStdIn.readString();
		CircularSuffixArray circularSuffixArray = new CircularSuffixArray(input);
		int length = circularSuffixArray.length();

		StringBuilder transformString = new StringBuilder();

		int first=0;
		for (int i=0; i<circularSuffixArray.length(); i++) {
			int index = circularSuffixArray.index(i);
			if (circularSuffixArray.index(i) == 0) {
				first = i;
			}

			transformString.append(input.charAt((index-1 + length)%length));
		}
		BinaryStdOut.write(first);
		BinaryStdOut.write(transformString.toString());
		BinaryStdOut.close();
	}

	// apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
	public static void inverseTransform() {
		int first = BinaryStdIn.readInt();
		String message = BinaryStdIn.readString();

		int length = message.length();

		int next[] = new int[length];
		char prefix[] = message.toCharArray();
		int last[] = new int[256];

		Arrays.sort(prefix);

		// construct next
		for (int i=0; i<length; i++) {
			int index = message.indexOf(prefix[i], last[prefix[i]]);
			next[i] = index;
			last[prefix[i]] = index+1;
		}

		// inverse
		StringBuilder origin = new StringBuilder();
		for (int i=0; i<length; i++) {
			origin.append(prefix[first]);
			first = next[first];
		}

		BinaryStdOut.write(origin.toString());
		BinaryStdOut.close();
	}

	// if args[0] is '-', apply Burrows-Wheeler transform
	// if args[0] is '+', apply Burrows-Wheeler inverse transform
	public static void main(String[] args) {
		if (args[0].compareTo("-") == 0) {
			transform();
		}
		if (args[0].compareTo("+") == 0) {
			inverseTransform();
		}
	}
}
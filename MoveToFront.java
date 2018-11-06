import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
	// apply move-to-front encoding, reading from standard input and writing to standard output
	private static int R=256;
	private static char[] init() {
		char alphabet[] = new char[R];
		for (int i=0; i<R; i++) {
			alphabet[i] = (char)i;
		}
		return alphabet;
	}

	private static int find(char [] alphabet, char input) {
		for (int i=0; i<R; i++) {
			if (alphabet[i] == input) {
				return i;
			}
		}
		return 0;
	}

	private static void move(char [] alphabet, int id) {
		char t=alphabet[id];
		for (int i=id; i>0; i--) {
			alphabet[i] = alphabet[i-1];
		}
		alphabet[0]=t;
	}

	public static void encode() {
		char alphabet[] = init();
		String input = BinaryStdIn.readString();

		for (int j=0; j<input.length(); j++) {
			int i=find(alphabet, input.charAt(j));

			BinaryStdOut.write((byte) i);

			move(alphabet, i);
		}

		BinaryStdOut.close();
	}

	// apply move-to-front decoding, reading from standard input and writing to standard output
	public static void decode() {
		char alphabet[] = init();

		while (!BinaryStdIn.isEmpty()) {
			int input = BinaryStdIn.readInt(8);

			BinaryStdOut.write(alphabet[input]);

			move(alphabet, input);
		}

		BinaryStdOut.close();
	}

	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {
		if (args[0].compareTo("-") == 0) {
			encode();
		} else if (args[0].compareTo("+") == 0) {
			decode();
		}
	}
}
import java.io.IOException;

public class test2 {

	public static void main(String[] args) throws IOException {
		int[] wordSize = { 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6 };
		String[] firstRow = { "at", "ko", "ace", "box", "", "", "blot", "axis",
				"kept", "", "strip", "tents", "wacky", "", "satire", "" };
		WordSquare square;
		System.out.println("Starting " + WordSquare.myName() + "'s program.");
		System.out.println();
		for (int i = 0; i < wordSize.length; i++) {
			square = new WordSquare(wordSize[i], "words.txt");
			if (square.buildSquare(firstRow[i])) {
				System.out.println(square);
			} else
				System.out.println("not found.  ");
		}
		System.out.println(WordSquare.myName() + "'s program has completed.");
	}
}


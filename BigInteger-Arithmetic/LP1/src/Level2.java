import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author divyanshu
 *
 */
public class Level2 {

	public static void main(String[] args) throws FileNotFoundException {
		String number1 = "999";
		System.out.println("level 2");
		LargeIntegerArithmetics input = new LargeIntegerArithmetics(number1);

		Scanner sc;
		if (args.length == 1) {
			File f = new File(args[0]);
			sc = new Scanner(f);

		} else {

			sc = new Scanner(System.in);
		}

		input.level2Parser(sc);

	}

}

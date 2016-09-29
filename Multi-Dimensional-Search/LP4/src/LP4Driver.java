
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * 
 * @author divyanshu,radhika,aditya,shweta
 */
public class LP4Driver {
	static long[] description;
	static final int DLENGTH = 100000;

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		int counter = 0;
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		if (args.length > 0) {
			in = new Scanner(new File(args[0]));
		} else {
			in = new Scanner(System.in);
		}
		String s;
		double rv = 0;
		description = new long[DLENGTH];
		double value = 0;
		Timer timer = new Timer();
		MDS mds = new MDS();

		while (in.hasNext()) {
			s = in.next();
			if (s.charAt(0) == '#') {
				s = in.nextLine();
				continue;
			}
			if (s.equals("Insert")) {
				long id = in.nextLong();
				double price = in.nextDouble();
				long des = in.nextLong();
				int index = 0;
				while (des != 0) {
					description[index++] = des;
					des = in.nextInt();
				}
				description[index] = 0;
				value = mds.insert(id, price, description, index);
				rv += value;
				counter++;
				// System.out.println(counter + ": Insert: " + (int) value);
			} else if (s.equals("Find")) {
				long id = in.nextLong();
				value = mds.find(id);
				rv += value;
				counter++;
				// System.out.println(counter + ": Find: " +
				// decimalFormat.format(value));
			} else if (s.equals("Delete")) {
				long id = in.nextLong();
				value = mds.delete(id);
				rv += value;
				counter++;
				// System.out.println(counter + ": Delete: " +
				// decimalFormat.format(value));
			} else if (s.equals("FindMinPrice")) {
				long des = in.nextLong();
				value = mds.findMinPrice(des);
				rv += value;
				counter++;
				// System.out.println(counter + ": FindMinPrice: " +
				// decimalFormat.format(value));
			} else if (s.equals("FindMaxPrice")) {
				long des = in.nextLong();
				value = mds.findMaxPrice(des);
				rv += value;
				counter++;
				// System.out.println(counter + ": FindMaxPrice: " +
				// decimalFormat.format(value));
			} else if (s.equals("FindPriceRange")) {
				long des = in.nextLong();
				double lowPrice = in.nextDouble();
				double highPrice = in.nextDouble();
				value = mds.findPriceRange(des, lowPrice, highPrice);
				rv += value;
				counter++;
				// System.out.println(counter + ": FindPriceRange: " + (int)
				// value);
			} else if (s.equals("PriceHike")) {
				long minid = in.nextLong();
				long maxid = in.nextLong();
				double rate = in.nextDouble();
				value = mds.priceHike(minid, maxid, rate);
				rv += value;
				counter++;
				// System.out.println(counter + ": PriceHike: " +
				// decimalFormat.format(value));
			} else if (s.equals("Range")) {
				double lowPrice = in.nextDouble();
				double highPrice = in.nextDouble();
				value = mds.range(lowPrice, highPrice);
				rv += value;
				counter++;
				// System.out.println(counter + ": Range: " + value);
			} else if (s.equals("SameSame")) {
				value = mds.samesame();
				rv += value;
				counter++;
				// System.out.println(counter + ": SameSame: " + (int) value);
			} else if (s.equals("End")) {
				break;
			} else {
				System.out.println("Houston, we have a problem.\nUnexpected line in input: " + s);
				System.exit(0);
			}
		}
		System.out.println(decimalFormat.format(rv));
		System.out.println(timer.end());
	}
}
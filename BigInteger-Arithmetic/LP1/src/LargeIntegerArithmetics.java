
/*
 * Level - 1 specifications
 * 
 * The class implements large integer arithmetics 
 * Initially a large number which is entered as a string is added to list in a way such that
 * the least significant digit is added to the list first 
 * Methods to implement all arithmetic operations on the large numbers are included in this class
 * namely : 1) addition
 *          2) subtraction
 *          3) product
 *          4) power
 *  
 * */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author Level1 - Radhika
 * @author Level2 - divyanshu, shweta
 * @author Level3 - divyanshu
 *
 */
public class LargeIntegerArithmetics {

	final static int BASE = 10;

	private long number;
	private ArrayList<Integer> digitList = new ArrayList<Integer>();

	public LargeIntegerArithmetics() {

	}

	/**
	 * @param number
	 * 
	 *            Constructor for the class where a large number which comes as
	 *            a string is stored in the list such that the least significant
	 *            digit is added first Eg : If the number is 1234 Then it is
	 *            stored in the list as 4->3->2->1
	 * 
	 */
	public LargeIntegerArithmetics(String number) {

		int no_of_digits = number.length();
		int flag = 0;
		// add signed bit if input number is negative
		if (number.charAt(0) == '-') {

			digitList.add(1);
			flag = 1;
		} else {

			digitList.add(0);
			flag = 0;
		}
		// if negative add the digits except '-' to the arraylist
		if (flag == 1) {
			for (int i = no_of_digits - 1; i > 0; i--) {
				int digit = Integer.valueOf(String.valueOf(number.charAt(i)));
				digitList.add(digit);
			}
			// if not negative all the digits to the arraylist
		} else {
			for (int i = no_of_digits - 1; i >= 0; i--) {
				int digit = Integer.valueOf(String.valueOf(number.charAt(i)));
				digitList.add(digit);
			}
		}

	}

	/**
	 * @param number
	 */
	public LargeIntegerArithmetics(long number) {
		this.number = number;

	}

	/**
	 * @param number
	 * 
	 *            Constructor for passing Arraylist to the class object
	 */
	public LargeIntegerArithmetics(ArrayList<Integer> number) {
		this.digitList = number;
	}

	/**
	 * 
	 * @param num1
	 *            - signed bit number
	 * @param num2
	 *            - signed bit number
	 * 
	 * @return - sum(num1+num2)
	 */
	public static LargeIntegerArithmetics addition(LargeIntegerArithmetics num1, LargeIntegerArithmetics num2) {
		// iterator to iterate through the lists
		Iterator<Integer> it1 = num1.digitList.iterator();
		Iterator<Integer> it2 = num2.digitList.iterator();

		Integer x1 = next(it1);
		Integer x2 = next(it2);
		// initialize carry to zero
		int carry = 0;

		LargeIntegerArithmetics result = new LargeIntegerArithmetics();
		ArrayList<Integer> temp = new ArrayList<>();

		if (num1.digitList.get(0) != num2.digitList.get(0)) {
			// result = null;
			// here subtraction should be performed.
			if (Compare(num1.digitList, num2.digitList)) {

				// num1.digitList.set(0, 0);
				temp = sub(num1.digitList, num2.digitList);
				if (num1.digitList.get(0) == 0) {
					temp.add(0, 0);
				} else {
					temp.add(0, 1);
				}
				result = new LargeIntegerArithmetics(temp);

			} else {
				temp = sub(num2.digitList, num1.digitList);
				if (num2.digitList.get(0) == 0) {
					temp.add(0, 0);
					// result.digitList.set(0, 0);
				} else {
					temp.add(0, 1);
					// result.digitList.set(0, 1);
				}
				result = new LargeIntegerArithmetics(temp);
			}
			return result;
		}
		int sum = 0;
		// add the digits in both the lists unless one of the list ends
		while (x1 != null && x2 != null) {

			sum = x1 + x2 + carry;
			result.digitList.add(sum % BASE);
			carry = sum / BASE;
			x1 = next(it1);
			x2 = next(it2);
		}
		while (x1 != null) {
			sum = x1 + carry;
			result.digitList.add(sum % BASE);
			carry = sum / BASE;
			x1 = next(it1);
		}
		while (x2 != null) {
			sum = x2 + carry;
			result.digitList.add(sum % BASE);
			carry = sum / BASE;
			x2 = next(it2);
		}
		if (carry > 0) {
			result.digitList.add(carry);
		}

		return result;

	}

	/**
	 * For doing addition of two numbers by ignoring the sign of the two numbers
	 * 
	 * @param a
	 *            - signed bit arraylist
	 * @param b
	 *            - signed bit arraylist
	 * 
	 * @return - unsigned add(a,b)
	 */
	public static ArrayList<Integer> add(ArrayList<Integer> a, ArrayList<Integer> b) {
		// iterator to iterate through the lists
		Iterator<Integer> it1 = a.iterator();
		Iterator<Integer> it2 = b.iterator();

		Integer x1 = next(it1);
		x1 = next(it1);
		Integer x2 = next(it2);
		x2 = next(it2);
		// initialize carry to zero
		int carry = 0;

		LargeIntegerArithmetics result = new LargeIntegerArithmetics();
		int sum = 0;
		// add the digits in both the lists unless one of the list ends
		while (x1 != null && x2 != null) {

			sum = x1 + x2 + carry;
			result.digitList.add(sum % BASE);
			carry = sum / BASE;
			x1 = next(it1);
			x2 = next(it2);
		}
		while (x1 != null) {
			sum = x1 + carry;
			result.digitList.add(sum % BASE);
			carry = sum / BASE;
			x1 = next(it1);
		}
		while (x2 != null) {
			sum = x2 + carry;
			result.digitList.add(sum % BASE);
			carry = sum / BASE;
			x2 = next(it2);
		}
		if (carry > 0) {
			result.digitList.add(carry);
		}

		return result.digitList;

	}

	/**
	 * 
	 * For doing subtraction of two numbers by ignoring the signed bit of two
	 * numbers
	 * 
	 * @param a
	 *            - signed arraylist
	 * @param b
	 *            - signed arraylist
	 * 
	 * @return - unsigned subtract(input1, input2)
	 */
	public static ArrayList<Integer> sub(ArrayList<Integer> a, ArrayList<Integer> b) {

		int borrow = 0;
		int value1 = 0, value2 = 0, value = 0;
		ArrayList<Integer> l = new ArrayList<>();
		int len1 = a.size();
		int len2 = b.size();

		for (int i = 1; i < len2; i++) {
			value1 = a.get(i);
			value2 = b.get(i);
			if (value1 >= value2 + borrow) {

				value = value1 - value2 - borrow;
				borrow = 0;
			} else {

				value = value1 + BASE - value2 - borrow;
				borrow = 1;

			}
			l.add(value);

		}

		for (int j = len2; j < len1; j++) {

			value = a.get(j) - borrow;

			if (value < 0) {

				value += BASE;
				borrow = 1;
			} else {

				borrow = 0;
			}
			l.add(value);
		}
		while (l.get(l.size() - 1) == 0) {
			l.remove(l.size() - 1);
		}
		return l;
	}

	/**
	 * @param num1
	 *            - signed bit number
	 * @param num2
	 *            - signed bit number
	 * 
	 * @return - subtraction(num1 - num2)
	 */
	public static LargeIntegerArithmetics subtract(LargeIntegerArithmetics num1, LargeIntegerArithmetics num2) {
		LargeIntegerArithmetics result = new LargeIntegerArithmetics();
		// set the iterators on the two lists
		Iterator<Integer> it1 = num1.digitList.iterator();
		Iterator<Integer> it2 = num2.digitList.iterator();

		Integer x1 = next(it1);
		Integer x2 = next(it2);

		int borrow = 0;
		ArrayList<Integer> temp = new ArrayList<>();
		// When sign of two numbers is different
		if (num1.digitList.get(0) != num2.digitList.get(0)) {
			// if 1st number is positive -> a-(-b)
			// do simple addition of a+b and add signed bit as positive
			if (num1.digitList.get(0) == 0) {
				temp = add(num1.digitList, num2.digitList);
				temp.add(0, 0);
				// if 1st number is negative -> -a-b
				// do simple addition of a+b and negative signed bit to the
				// result
			} else {
				temp = add(num1.digitList, num2.digitList);
				temp.add(0, 1);
			}
			result = new LargeIntegerArithmetics(temp);
			// when sign of two numbers is the same, check for the larger number
			// and do subtraction of (larger-smaller)
		} else if (!(Compare(num1.digitList, num2.digitList))) {
			temp = sub(num2.digitList, num1.digitList);
			temp.add(0, 1);
			result = new LargeIntegerArithmetics(temp);
		} else {
			// till we do not reach the end o both lists , we do the subtraction
			// arithmetics
			while (x1 != null && x2 != null) {

				x1 = x1 - borrow;
				borrow = 0;
				if (x1 < x2) {
					borrow = 1;
					x1 = x1 + BASE;
				}
				result.digitList.add(x1 - x2);
				x1 = next(it1);
				x2 = next(it2);
			}

			while (x1 != null) {
				x1 = x1 - borrow;
				borrow = 0;
				result.digitList.add(x1);
				x1 = next(it1);
			}
		}
		return result;
	}

	/**
	 * @param input1
	 *            - signed input
	 * @param input2
	 *            - signed input
	 * 
	 * @return - product(input1*input2)
	 */
	public static LargeIntegerArithmetics product(LargeIntegerArithmetics num1, LargeIntegerArithmetics num2) {

		// This is the main ArrayList
		ArrayList<Integer> arr = new ArrayList<>();

		// Initialize the final array to 0.
		for (int i = 1; i < num1.digitList.size() + num2.digitList.size(); i++) {

			arr.add(0);
		}
		ArrayList<Integer> temp = new ArrayList<>(); // for holding the output
														// of each iteration.
		int carryOuter = 0, outerIndex = 0, carryInner = 0;
		int value = 0, i, j;
		// iterate over both the loops.
		for (int i1 = 1; i1 < num1.digitList.size(); i1++) {
			i = num1.digitList.get(i1);
			for (int j1 = 1; j1 < num2.digitList.size(); j1++) {
				j = num2.digitList.get(j1);
				value = (i * j + carryInner) % BASE;
				carryInner = (i * j + carryInner) / BASE;
				temp.add(value);
			}
			if (carryInner != 0) {

				temp.add(carryInner);
			}

			carryOuter = 0;
			int position = outerIndex;
			while (temp.size() > 0) {

				value = arr.get(position) + carryOuter + temp.remove(0);
				arr.set(position, value % BASE);
				carryOuter = value / BASE;
				position++;
			}
			while (carryOuter != 0) {

				value = arr.get(position) + carryOuter;
				arr.set(position, value % BASE);
				carryOuter = value / BASE;
				position++;
			}

			// resetting carryInner and carryOuter.
			carryInner = 0;
			carryOuter = 0;
			outerIndex++;
		}

		// String for returning the value of inputs.
		String s = "";
		// conditions for handling negative numbers as well.
		if (num1.digitList.get(0) != num2.digitList.get(0)) {

			s += '-';
		}

		int counter = 0;
		for (int i2 = arr.size() - 1; i2 >= 0; i2--) {

			if (arr.get(i2) != 0)
				counter = 1;

			if (counter == 1) {
				s += arr.get(i2);
			}
		}
		LargeIntegerArithmetics b3 = new LargeIntegerArithmetics(s);
		return b3;
	}

	/**
	 * Comparing two numbers
	 * 
	 * 
	 * @param a
	 * @param b
	 * 
	 * @return - true if a > b, false if b > a
	 */
	public static Boolean Compare(ArrayList<Integer> a, ArrayList<Integer> b) {
		if (a.size() > b.size()) {
			return true;
		} else if (b.size() > a.size()) {
			return false;
		} else {
			for (int i = a.size() - 1; i >= 0; i--) {

				if (a.get(i) > b.get(i))
					return true;
				else if (b.get(i) > a.get(i)) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Division of two numbers Takes as input only positive number
	 * 
	 * @param dividend
	 *            - signed bit number
	 * @param divisor
	 *            - signed bit number
	 * 
	 * @return - quotient(unsigned number)
	 */
	public static LargeIntegerArithmetics division(LargeIntegerArithmetics dividend, LargeIntegerArithmetics divisor) {
		LargeIntegerArithmetics result;
		// if dividend less than divisor return quotient as 0
		if (!(Compare(dividend.digitList, divisor.digitList))) {

			result = new LargeIntegerArithmetics("0");
			return result;
		} else {

			int len1 = dividend.digitList.size();
			int len2 = divisor.digitList.size();
			int index = len2;
			int j = 0;
			LargeIntegerArithmetics number;
			ArrayList<Integer> arr = new ArrayList<>();
			ArrayList<Integer> quotient = new ArrayList<>();
			// add a 0 initially to the list of arr.
			arr.add(0);
			// add 0 the quotient as well.
			quotient.add(0);
			for (int i = len2 - 1; i > 0; i--) {

				arr.add(dividend.digitList.get(len1 - i));
			}

			int counter = len2;
			// iterate till the end of the list
			while (counter <= len1) {
				// when subset of dividend greater than divisor
				// do binary search on the quotient
				// store quotient as arraylist
				// and change subset of dividend to the remainder
				if (Compare(arr, divisor.digitList)) {
					j = 0;
					number = new LargeIntegerArithmetics(arr);

					QuotientRemainder sol = binarySearch(number, divisor);
					quotient.add(1, sol.in1);
					arr = sol.in2;
					while (arr.size() >= 2 && arr.get(1) == 0) {

						arr.remove(1);
					}
					// increment arraylist till the point
					// subset of dividend greater than equals the divisor
				} else {

					if (!(arr.size() == 1 && dividend.digitList.get(len1 - index) == 0)) {
						arr.add(1, dividend.digitList.get(len1 - index));
						j++;
					} else {

						if (index != len1)
							quotient.add(1, 0);
					}
					index++;
					counter++;
					if (j > 1) {
						quotient.add(1, 0);
					}
				}

			}
			result = new LargeIntegerArithmetics(quotient);
		}

		return result;

	}

	/**
	 * For doing a binary search on the quotient
	 * 
	 * @param n1
	 *            - digits from dividend
	 * @param n2
	 *            - divisor
	 * 
	 * @return - Class type containing digits of quotient and temporary
	 *         remainder arraylist
	 */
	private static QuotientRemainder binarySearch(LargeIntegerArithmetics n1, LargeIntegerArithmetics n2) {

		int low = 1;
		int high = BASE;
		int mid = (high + low) / 2;
		LargeIntegerArithmetics middleElement;
		QuotientRemainder sol = null;
		// while mid*divisor <= subset of dividend
		// and (mid+1)*divisor > subset of dividend
		while (true) {
			middleElement = new LargeIntegerArithmetics(String.valueOf(mid));

			if (Compare(n1.digitList, product(n2, new LargeIntegerArithmetics(String.valueOf(mid))).digitList)
					&& !(Compare(n1.digitList,
							product(n2, new LargeIntegerArithmetics(String.valueOf(mid + 1))).digitList))) {

				break;
			}
			LargeIntegerArithmetics bb = product(n2, middleElement);
			if (Compare(n1.digitList, bb.digitList)) {

				low = mid;
				mid = (low + high) / 2;

			}
			if (!(Compare(n1.digitList, bb.digitList))) {
				high = mid;
				mid = (low + high) / 2;

			}
		}
		LargeIntegerArithmetics remainder = (subtract(n1, product(n2, middleElement)));
		sol = new QuotientRemainder(mid, remainder.digitList);
		return sol;
	}

	/**
	 * @param base
	 *            - signed bit number arraylist
	 * @param power
	 *            - long value
	 * 
	 * @return - arraylist of base^value
	 */
	public static LargeIntegerArithmetics power(LargeIntegerArithmetics base, long power) {

		LargeIntegerArithmetics result = new LargeIntegerArithmetics();
		LargeIntegerArithmetics temp = new LargeIntegerArithmetics();
		// if the power is zero then directly return 1
		if (power == 0) {
			result.digitList.add(1);
			return result;

		}
		// if the power is 1 , return the base
		if (power == 1) {
			return base;
		}
		// recursively call power function
		result = power(base, power / 2);
		// if the power is a multiple of 2 then we can divide the number and by
		// DAG we compute the power
		if (power % 2 == 0) {
			return product(result, result);
		}
		// if the number is odd then we have to multiply base with the result as
		// one power will be missing if we divide by 2
		else {
			temp = product(result, result);
			return product(base, temp);
		}

	}

	/**
	 * @param x
	 *            - arraylist
	 * @param n
	 *            - arraylist
	 * 
	 * @return - arraylist of x^n
	 */
	public static LargeIntegerArithmetics bigPower(LargeIntegerArithmetics x, LargeIntegerArithmetics n) {
		long a0 = n.digitList.get(1);
		LargeIntegerArithmetics x_s;
		if (n.digitList.size() == 2) {
			return x.power(x, a0);
		} else {
			LargeIntegerArithmetics s = shift(n);
			x_s = bigPower(x, s);
			return x.product(x.power(x_s, BASE), n.power(x, a0));
		}
	}

	/**
	 * @param n
	 *            - arraylist to the base b - a0 + a1.x + a2.x^2+...+an.x^n+1
	 * 
	 * @return - a1+a2.x+...+an.x^n
	 */
	public static LargeIntegerArithmetics shift(LargeIntegerArithmetics n) {
		n.digitList.remove(0);
		return n;
	}

	/**
	 * @param dividend
	 *            - signed bit arraylist
	 * @param divisor
	 *            - signed bit arraylist
	 * 
	 * @return - arraylist of remainder
	 */
	LargeIntegerArithmetics modulus(LargeIntegerArithmetics dividend, LargeIntegerArithmetics divisor) {
		if (!(Compare(dividend.digitList, divisor.digitList))) {
			return dividend;
		}
		LargeIntegerArithmetics quotient = division(dividend, dividend);

		LargeIntegerArithmetics remainder = subtract(dividend, product(quotient, divisor));

		return remainder;
	}

	/**
	 * @param number
	 *            - signed bit arraylist taken unsigned
	 * 
	 * @return - arraylist - ~(number)
	 */
	LargeIntegerArithmetics squareRoot(LargeIntegerArithmetics number) {

		LargeIntegerArithmetics right = number;
		LargeIntegerArithmetics left = new LargeIntegerArithmetics("0");
		LargeIntegerArithmetics mid = null;
		LargeIntegerArithmetics divisor = new LargeIntegerArithmetics("2");
		// till low+1 < high
		while (!(Compare(addition(left, new LargeIntegerArithmetics("1")).digitList, right.digitList))) {

			mid = division(addition(left, right), divisor);
			if (Compare(product(mid, mid).digitList, number.digitList)) {

				right = mid;

			} else if (!(Compare(product(mid, mid).digitList, number.digitList))) {

				left = mid;

			} else {
				return mid;
			}

		}
		return left;
	}

	/**
	 * @param b
	 *            - number with base 10
	 * @param base
	 *            - desired base value
	 * 
	 * @return - arraylist - number
	 */
	LargeIntegerArithmetics convertToBase(LargeIntegerArithmetics number, int base) {

		ArrayList<Integer> arrtemp = new ArrayList<>();
		arrtemp.add(0, 0);
		LargeIntegerArithmetics Base = new LargeIntegerArithmetics(String.valueOf(base));
		LargeIntegerArithmetics temp;
		String s = "";
		while (Compare(number.digitList, Base.digitList)) {

			temp = modulus(number, Base);
			// convert the value above to number.
			for (int i = temp.digitList.size() - 1; i > 0; i--) {
				s += temp.digitList.get(i);
			}
			arrtemp.add(Integer.valueOf(s));
			// re change s to empty after usage.
			s = "";
			number = division(number, Base);
		}
		arrtemp.add(number.digitList.get(1));

		return new LargeIntegerArithmetics(arrtemp);

	}

	/**
	 * @param var
	 *            - number
	 * 
	 * @return - var!
	 */
	LargeIntegerArithmetics factorial(LargeIntegerArithmetics number) {
		LargeIntegerArithmetics temp = new LargeIntegerArithmetics(number.digitList);

		while (!(temp.digitList.size() == 2 && temp.digitList.get(1) == 1)) {

			temp = subtract(temp, new LargeIntegerArithmetics("1"));
			number = product(number, temp);
		}
		return number;
	}

	LargeIntegerArithmetics operations(LargeIntegerArithmetics a, LargeIntegerArithmetics b, String temp) {
		if (temp.equals("/")) {
			a = division(a, b);
		} else if (temp.equals("*")) {
			a = product(a, b);
		} else if (temp.equals("+")) {
			a = addition(a, b);
		} else if (temp.equals("-")) {
			a = subtract(a, b);
		} else if (temp.equals("^")) {
			a = bigPower(a, b);
		} else if (temp.equals("!")) {
			a = factorial(a);
		} else if (temp.equals("~")) {
			a = squareRoot(a);
		}
		return a;
	}

	LargeIntegerArithmetics operations(LargeIntegerArithmetics a, String temp) {
		if (temp.equals("!")) {
			a = factorial(a);
		} else if (temp.equals("~")) {
			a = squareRoot(a);
		}
		return a;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public List<Integer> getDigitList() {
		return digitList;
	}

	public void setDigitList(ArrayList<Integer> digitList) {
		this.digitList = digitList;
	}

	@Override
	public String toString() {

		int digits = digitList.size();
		String num = "";
		if (digitList.get(0) != 0) {
			num = num + '-';
		}

		for (int i = digits - 1; i > 0; --i) {

			num = num + digitList.get(i);
		}
		return num;
	}

	/*
	 * print the list in the actual ordering of the digits in the number Eg : if
	 * number is 1234 The printList functions prints : [1,2,3,4]
	 */
	public void printList() {
		System.out.print(BASE + ": ");
		for (int i = 1; i < digitList.size(); i++) {

			System.out.print(digitList.get(i) + " ");
		}

	}

	/*
	 * Helper function to iterate through the elements in the data structure
	 */
	public static Integer next(Iterator<Integer> it) {
		if (it.hasNext())
			return it.next();
		else
			return null;
	}

	void level3Parser(Scanner sc) {

		HashMap<String, LargeIntegerArithmetics> h = new HashMap<>();
		ArrayList<ReadValue> arr = new ArrayList<>();

		while (sc.hasNextLine()) {

			String s = sc.nextLine();

			if (s.equals(""))
				break;

			String temp[] = s.split(" ");

			arr.add(new ReadValue(Integer.valueOf(temp[0]), temp[1]));

		}
		HashMap<String, Integer> h1 = new HashMap<>();
		h1.put("!", 4);
		h1.put("~", 4);
		h1.put("^", 3);
		h1.put("*", 2);
		h1.put("/", 2);
		h1.put("%", 2);
		h1.put("+", 1);
		h1.put("-", 1);
		Stack<String> s1 = new Stack<>();
		Stack<String> s2 = new Stack<>();
		String result;
		String input;
		int lineNo = 0;
		while (lineNo < arr.size()) {

			ReadValue temp = arr.get(lineNo);

			String tok[] = temp.value.split("=");
			if (tok[1].length() == 1) {
				h.put(tok[0], new LargeIntegerArithmetics(tok[1]));
				lineNo++;
			} else {
				result = tok[0];
				input = tok[1];
				// System.out.println(input);
				String c[] = input.split("");
				int i = 0;
				while (i < c.length) {
					if (h1.containsKey(c[i])) {
						String temp1 = null;
						if (!(s2.isEmpty())) {
							temp1 = s2.peek();
							if (h1.get(temp1) > h1.get(c[i])) {
								temp1 = s2.pop();
								s1.push(temp1);

							}
						}
						s2.push(c[i]);
					} else {
						s1.push(c[i]);
					}
					// System.out.println(c[i]);
					i++;
				}
				while (!(s2.isEmpty())) {
					s1.push(s2.pop());
				}
				lineNo++;
			}
		}
		ArrayList<String> postfix = new ArrayList<>();
		while (!(s1.empty())) {
			postfix.add(0, s1.pop());
		}
		postfix.remove(0);
		// System.out.println(postfix);
		s1.clear();
		s2.clear();
		while (!(postfix.isEmpty())) {
			String temp = postfix.remove(0);
			if (h1.containsKey(temp)) {
				if (h1.get(temp) == 4) {
					String elem1 = s1.pop();
					if (h.containsKey(elem1)) {
						LargeIntegerArithmetics n1 = h.get(elem1);
						elem1 = String.valueOf(n1);
					}
					LargeIntegerArithmetics l1 = new LargeIntegerArithmetics(elem1);
					l1 = operations(l1, temp);
					s1.push(String.valueOf(l1));
				} else {
					String elem1 = s1.pop();
					if (h.containsKey(elem1)) {
						LargeIntegerArithmetics n1 = h.get(elem1);
						elem1 = String.valueOf(n1);
					}
					LargeIntegerArithmetics l1 = new LargeIntegerArithmetics(elem1);
					String elem2 = s1.pop();
					if (h.containsKey(elem2)) {
						LargeIntegerArithmetics n1 = h.get(elem2);
						elem2 = String.valueOf(n1);
					}
					LargeIntegerArithmetics l2 = new LargeIntegerArithmetics(elem2);
					l2 = operations(l2, l1, temp);
					s1.push(String.valueOf(l2));
				}
			} else {
				s1.push(temp);
			}

		}
		result = s1.pop();
		System.out.println(result);

	}

	void level2Parser(Scanner sc) {

		HashMap<String, LargeIntegerArithmetics> hash = new HashMap<>();
		ArrayList<ReadValue> arr = new ArrayList<>();

		while (sc.hasNextLine()) {

			String s = sc.nextLine();

			if (s.equals(""))
				break;

			String temp[] = s.split(" ");

			arr.add(new ReadValue(Integer.valueOf(temp[0]), temp[1]));

		}
		sc.close();

		int lineNo = 0;
		while (lineNo < arr.size()) {
			ReadValue temp = arr.get(lineNo);
			String token[] = temp.value.split("[-+*/=!^)?~!%]");

			if (token.length == 1 && temp.value.length() == 2) {

				if (temp.value.charAt(1) == '~') {

					LargeIntegerArithmetics temp1 = hash.get(String.valueOf(temp.value.charAt(0)));
					hash.put(String.valueOf(temp.value.charAt(0)), squareRoot(temp1));
				} else if (temp.value.charAt(1) == '!') {
					LargeIntegerArithmetics temp1 = hash.get(String.valueOf(temp.value.charAt(0)));
					hash.put(String.valueOf(temp.value.charAt(0)), factorial(temp1));

				} else if (temp.value.charAt(1) == ')') {

					hash.get(String.valueOf(temp.value.charAt(0))).printList();

				}

			} else if (token.length == 2 && temp.value.length() == 4
					&& (temp.value.charAt(3) == '~' || temp.value.charAt(3) == '!')) {

				if (temp.value.charAt(3) == '~') {

					LargeIntegerArithmetics temp1 = hash.get(String.valueOf(temp.value.charAt(2)));
					hash.put(String.valueOf(temp.value.charAt(0)), squareRoot(temp1));
				} else if (temp.value.charAt(3) == '!') {
					LargeIntegerArithmetics temp1 = hash.get(String.valueOf(temp.value.charAt(2)));
					hash.put(String.valueOf(temp.value.charAt(0)), factorial(temp1));

				}

			} else if (token.length == 2) {

				if (temp.value.charAt(1) == '?') {

					LargeIntegerArithmetics temp1 = hash.get(String.valueOf(temp.value.charAt(0)));
					if (temp1.digitList.size() == 3 && temp1.digitList.get(1) == 0 && temp1.digitList.get(1) == 0) {

					} else if (temp1.digitList.size() == 2 && temp1.digitList.get(1) == 0) {

					} else {

						int s = Integer.valueOf(token[1]);

						int counter = 0;
						for (ReadValue temp2 : arr) {

							if (temp2.lineNumber == s) {
								break;

							}
							counter++;
						}
						lineNo = counter;
						continue;
					}

				} else {

					hash.put(token[0], new LargeIntegerArithmetics(token[1]));
				}
			}

			else if (token.length == 3) {

				LargeIntegerArithmetics num1 = hash.get(token[1]);
				LargeIntegerArithmetics num2 = hash.get(token[2]);

				LargeIntegerArithmetics output;
				if (temp.value.charAt(3) == '+') {

					output = addition(num1, num2);
					hash.put(token[0], output);

				} else if (temp.value.charAt(3) == '-') {
					output = subtract(num1, num2);
					hash.put(token[0], output);

				} else if (temp.value.charAt(3) == '*') {
					output = product(num1, num2);
					hash.put(token[0], output);

				} else if (temp.value.charAt(3) == '/') {
					output = division(num1, num2);
					hash.put(token[0], output);

				} else if (temp.value.charAt(3) == '%') {
					output = modulus(num1, num2);
					hash.put(token[0], output);

				} else if (temp.value.charAt(3) == '^') {
					output = bigPower(num1, num2);
					hash.put(token[0], output);

				}

			} else {

				System.out.println(hash.get(token[0]));
			}

			lineNo++;

		}

	}

	public static void main(String[] args) {

		int ch;
		System.out.println("result");
		Scanner scan = new Scanner(System.in);

		do {
			System.out.println("Select your Choice");
			System.out.println("1.Addition");
			System.out.println("2.Subtraction");
			System.out.println("3.Product");
			System.out.println("4.Power");
			System.out.println("Level 2");
			System.out.println("5.Power using BigInteger");
			System.out.println("6. Division");
			System.out.println("7.Exit");
			ch = scan.nextInt();
			switch (ch) {
			case 1:
				System.out.println("Enter first number : ");
				String a = scan.next();
				LargeIntegerArithmetics num1 = new LargeIntegerArithmetics(a);
				System.out.println("Enter second number : ");
				String b = scan.next();
				LargeIntegerArithmetics num2 = new LargeIntegerArithmetics(b);
				LargeIntegerArithmetics sum = new LargeIntegerArithmetics();
				sum = addition(num1, num2);

				System.out.println("The addition of the given two numbers is : " + sum);
				break;
			case 2:
				System.out.println("Enter first number : ");
				String c = scan.next();
				LargeIntegerArithmetics num3 = new LargeIntegerArithmetics(c);
				System.out.println("Enter second number : ");
				String d = scan.next();
				LargeIntegerArithmetics num4 = new LargeIntegerArithmetics(d);
				LargeIntegerArithmetics result = new LargeIntegerArithmetics();
				result = subtract(num3, num4);
				System.out.println("The subtraction of the given numbers is : " + result);
				break;
			case 3:
				System.out.println("Enter first number : ");
				String e = scan.next();
				LargeIntegerArithmetics num5 = new LargeIntegerArithmetics(e);
				System.out.println("Enter second number : ");
				String f = scan.next();
				LargeIntegerArithmetics num6 = new LargeIntegerArithmetics(f);
				LargeIntegerArithmetics product = new LargeIntegerArithmetics();
				product = product(num5, num6);
				System.out.println("The product of the given numbers is : " + product);
				break;
			case 4:
				System.out.println("Enter base number : ");
				String g = scan.next();
				LargeIntegerArithmetics num7 = new LargeIntegerArithmetics(g);
				System.out.println("Enter power number : ");
				long h = scan.nextLong();
				LargeIntegerArithmetics power = new LargeIntegerArithmetics();
				power = power(num7, h);
				System.out.println("The " + h + "th power of " + g + " is : " + power);
				break;
			case 5:
				System.out.println("Enter base number:");
				String s1 = scan.next();
				LargeIntegerArithmetics number1 = new LargeIntegerArithmetics(s1);
				System.out.println("Enter power number:");
				String s2 = scan.next();
				LargeIntegerArithmetics number2 = new LargeIntegerArithmetics(s2);
				power = bigPower(number1, number2);
				System.out.println("Result is " + power);
			case 6:
				System.out.println("Enter Dividend:");
				String d1 = scan.next();
				LargeIntegerArithmetics dividend = new LargeIntegerArithmetics(d1);
				System.out.println("Enter Divisor:");
				String d2 = scan.next();
				LargeIntegerArithmetics divisor = new LargeIntegerArithmetics(d2);
				LargeIntegerArithmetics quotient = new LargeIntegerArithmetics();
				quotient = division(dividend, divisor);
				System.out.println(quotient);

			case 7:
				System.out.println("Exit");
				break;
			default:
				System.out.println("Entered Wrong Choice");
				break;
			}
		} while (ch != 7);
		scan.close();
		System.exit(0);

	}

}

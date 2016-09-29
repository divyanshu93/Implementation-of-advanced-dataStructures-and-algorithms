/**
 *
 * @author aditya,divyanshu,radhika,shweta
 */
public class Item {
	long id;
	long[] descriptors;
	double Price;

	Item(long id, long[] descriptors, Double Price) {
		this.id = id;
		this.descriptors = descriptors;
		this.Price = Price;
	}
}

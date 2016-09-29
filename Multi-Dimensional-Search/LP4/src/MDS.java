import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author divyanshu, aditya, radhika, shweta
 *
 */
public class MDS {
	// tree map with id as key and item as the value
	TreeMap<Long, Item> outer;
	// hash map with descriptor as key and list of ids as value
	HashMap<Long, TreeSet<Long>> inner;
	HashMap<Equal, Integer> same;// Hashmap for doing SameSame
	Integer sameCounter;// Add all instances of SameSame

	public MDS() {
		outer = new TreeMap<>();
		inner = new HashMap<>();
		same = new HashMap<>();
		sameCounter = 0;
	}

	// list of description of type long
	public long[] createList(long[] a, int size) {
		long[] ans = new long[size];
		for (int i = 0; i < size; i++) {
			ans[i] = a[i];
		}
		return ans;
	}

	public int doHash(long[] inputs) {
		long hash = 5381;
		for (int i = 0; i < inputs.length; i++) {
			// hash += inputs[i];
			hash = ((hash << 5) + hash) + inputs[i];
		}
		return (int) hash;
	}

	/**
	 * 
	 * Inserting into the TreeMap if already exists then overwrites the previous
	 * entry if already exists and size = 0 then copies the previous description
	 * list
	 * 
	 * @param id
	 * @param price
	 * @param description
	 * @param size
	 * @return
	 */
	int insert(long id, double price, long[] description, int size) {
		int returnValue = 0;
		long[] descriptionList = null;
		long[] copyArray = createList(description, size);

		int hashValue = doHash(copyArray);
		Integer count;
		Item item = outer.get(id);
		// calculate hash value.
		if (item != null && size == 0) {
			// when id exists and size of descriptors is 0, update only the
			// price
			descriptionList = item.descriptors;
			outer.put(id, new Item(id, descriptionList, price));
			return 0;
		} else if (item != null && size != 0) {
			// when id already exists and its size is not 0.
			// update both price and descriptors
			// remove prev entry from the same hash map
			count = same.get(new Equal(doHash(item.descriptors), item.descriptors.length));
			if (count != null) {
				if (count == 1) {
					same.remove(new Equal(doHash(item.descriptors), item.descriptors.length));
				} else {
					same.put(new Equal(doHash(item.descriptors), item.descriptors.length), count - 1);
					sameCounter--;
				}
			}
			if (size >= 8) {
				// getting the value of object new Equal(size,hashValue)
				// checking if the value exists and incrementing accordingly
				Equal e = new Equal(hashValue, size);
				count = same.get(e);
				if (count == null) {
					same.put(e, 1);
				} else {
					same.put(e, count + 1);
					if (count == 1)
						sameCounter++;
					sameCounter++;
				}
			}
			// remove id from the previosu list of descriptors
			descriptionList = item.descriptors;
			outer.put(id, new Item(id, copyArray, price));
			returnValue = 0;
			for (long i : descriptionList) {
				inner.get(i).remove(id);
			}
		} else {
			outer.put(id, new Item(id, copyArray, price));
			returnValue = 1;
			if (size >= 8) {
				Equal e = new Equal(hashValue, size);
				count = same.get(e);
				if (count == null) {
					same.put(e, 1);
				} else {
					same.put(e, count + 1);
					if (count == 1)
						sameCounter++;
					sameCounter++;
				}
			}
		}
		// add descriptors and their values to the hashmap
		for (long i : copyArray) {
			if (inner.get(i) == null)
				inner.put(i, new TreeSet<Long>());
			inner.get(i).add(id);
		}
		return returnValue;
	}

	/**
	 * Find id of the item
	 * 
	 * @param id
	 * @return - price of the id if found else 0
	 */
	double find(long id) {
		Item item = outer.get(id);
		if (item != null) {
			// System.out.println(temp.Price);
			return Math.floor((item.Price + 0.000001) * 100) / 100;
			// return item.Price;
		}
		return 0;
	}

	/**
	 * Delete the item
	 * 
	 * @param id
	 * @return - sum of descriptors of the item
	 */
	long delete(long id) {
		// long[] item = outer.get(id).descriptors;
		// remove the entry from the outer list
		Item item = outer.remove(id);

		// remove the entry from hashmap same if it exists there
		Integer count;
		count = same.get(new Equal(doHash(item.descriptors), item.descriptors.length));
		if (count != null) {
			// when count is 1. deleting the entire element of the hashmap.
			if (count == 1)
				same.remove(new Equal(doHash(item.descriptors), item.descriptors.length));
			else {
				// when counter is greater than 1, reduce the count and put the
				// new value of count to hashmap
				count--;
				same.put(new Equal(doHash(item.descriptors), item.descriptors.length), count);
				// if value of count is 1, this means the item now does not have
				// any other item with same descriptors.
				// So we reduce the global return value of SameSame by 2, else
				// we reduce it by 1.
				if (count == 1)
					sameCounter--;
				sameCounter--;

			}
		}

		// remove the entry of the ids from the inner map
		long sum = 0;
		for (int i = 0; i < item.descriptors.length; i++) {
			inner.get(item.descriptors[i]).remove(id);
			sum += item.descriptors[i];
		}
		return sum;
	}

	/**
	 * Find Minimum price among id's that contain descriptor
	 * 
	 * @param des
	 *            - value of descriptor
	 * @return - price of the item
	 */
	double findMinPrice(long des) {
		TreeSet<Long> listOfIds = inner.get(des);
		double itemPrice;
		if (listOfIds == null) {
			return 0;
		}
		double minPrice = Double.MAX_VALUE;
		boolean change = false;
		for (long i : listOfIds) {
			itemPrice = outer.get(i).Price;
			// System.out.println("here: " + itemPrice + " " + outer.get(i).id +
			// " " + des);
			if (itemPrice <= minPrice) {
				minPrice = itemPrice;
				change = true;
			}
		}
		return change == true ? minPrice : 0;
	}

	/**
	 * Find Maximum price among id's that contain descriptor
	 * 
	 * @param des
	 *            - value of descriptor
	 * @return - price of the item
	 */
	double findMaxPrice(long des) {
		TreeSet<Long> list = inner.get(des);
		Double itemPrice;
		if (list == null) {
			return 0;
		}
		Double maxPrice = Double.MIN_VALUE;
		boolean change = false;
		for (long i : list) {
			// System.out.println("List is: " + i);
			itemPrice = outer.get(i).Price;
			if (itemPrice >= maxPrice) {
				maxPrice = itemPrice;
				change = true;
			}
		}
		return change == true ? maxPrice : 0;
	}

	/**
	 * @param des
	 * @param lowPrice
	 * @param highPrice
	 * @return - count of number of id's within the range
	 */
	int findPriceRange(long des, double lowPrice, double highPrice) {
		int count = 0;
		TreeSet<Long> list = inner.get(des);
		for (long i : list) {
			Double itemPrice = outer.get(i).Price;
			if (itemPrice <= highPrice && itemPrice >= lowPrice)
				count++;
		}
		return count;
	}

	double priceHike(long minid, long maxid, double rate) {

		double sum = 0;
		double inc_price = 0;
		for (long id : outer.keySet()) {

			if (id >= minid && id <= maxid) {
				inc_price = outer.get(id).Price * (rate / 100);
				inc_price = Math.floor((inc_price + 0.000001) * 100) / 100;
				// System.out.println("Hike: " + id + " " + inc_price);
				Item item = outer.get(id);
				item.Price = item.Price + inc_price;
				outer.put(id, item);
				sum = sum + inc_price;
			}
		}

		return sum;
	}

	int range(double lowPrice, double highPrice) {

		TreeSet<ItemPrice> itemPrice = new TreeSet<ItemPrice>(new ValueComparator());
		// double price;
		int no_of_items = 0;
		// for (long id : outer.keySet()) {
		// Item i = outer.get(id);
		// price = i.Price;
		// price = Math.floor((price + 0.000001) * 100) / 100;
		// if (price >= lowPrice && price <= highPrice) {
		// no_of_items = no_of_items + 1;
		// }
		// }
		for (long id : outer.keySet()) {
			ItemPrice ip = new ItemPrice(outer.get(id).id, outer.get(id).Price);
			itemPrice.add(ip);
		}

		if (itemPrice.first().price > highPrice || itemPrice.last().price < lowPrice) {
			return 0;
		}
		for (ItemPrice itpri : itemPrice) {
			itpri.price = Math.floor((itpri.price + 0.000001) * 100) / 100;
			if (itpri.price >= lowPrice && itpri.price <= highPrice) {
				no_of_items = no_of_items + 1;
			}
		}

		return no_of_items;
	}

	int samesame() {

		return sameCounter;
	}

	class ValueComparator implements Comparator<ItemPrice> {

		@Override
		public int compare(ItemPrice o1, ItemPrice o2) {
			if (o1.price >= o2.price)
				return 1;
			return -1;
		}

	}

}
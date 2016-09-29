
/**
 * @author divyanshu,aditya,radhika,shweta
 *
 */
public class Equal {

	int hashValue; // hash of descriptors
	int size; // number of descriptors

	public Equal(int hashValue, int size) {
		this.hashValue = hashValue;
		this.size = size;
	}

	@Override
	public boolean equals(Object arg0) {

		Equal e = (Equal) arg0;

		if (e.hashValue == hashValue && e.size == size) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return hashValue;
	}

}

package hw4;

/**
 * 
 * An IntegerPair is an ordered pair of ints.  We might have just used an array of length two,
 * but wrapping into an object allows easy comparison and hashing.
 * A constraint takes a pair of variables (indexed by integers) and gives pairs of
 * legal values (which are also integers).  
 * @author djb
 *
 */
public class IntegerPair {
	public int first, second;
	
	public IntegerPair(int f, int s) {
		first = f;
		second = s;
	}

	public boolean equals(Object o) {
		IntegerPair other = (IntegerPair) o;
		return first==other.first && second==other.second;
		
	}
	
	public int hashCode() {
		return first + second * 1000;
	}

	
	public String toString() {
		String s = "(" + first + ", " + second + ")";
		return s;
	}
	
	public void sort() {
		if(first > second) {
			int temp = first;
			first = second;
			second = temp;
		}
		 
		
	}
}

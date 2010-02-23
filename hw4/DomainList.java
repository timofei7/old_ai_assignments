package hw4;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Each variable has a domain.  Unary constraints are enforced by shrinking domains, and in a forward-checking
 * or MAC algorithm, the domain is shrunk as a path in the backtrackign algorithm is explored.  We want to be able to add variables to the
 * set in constant time, or (maybe more importantly, remove them), but we might also want to iterate over 
 * list.  (Why?  We'll need to iterate to do a backchaining search, since the next value in the domain 
 * needs to be selected).  So a LinkedHashSet is promising.  
 * Notice that since Integers are immutable, we can freely clone domains
 * @author tim
 *
 */
public class DomainList implements Cloneable{

	private ArrayList<LinkedHashSet<Integer>> domains;
	private int numVariables;
	
	
	/**
	 * domainlist constructor
	 * @param n number of variables
	 * @param maxValue 
	 */
	public DomainList(int n, int maxValue) {
		numVariables = n; 
		domains = new ArrayList<LinkedHashSet<Integer>>(n);
		
		for(int i = 0; i < n; i++ ) {
			LinkedHashSet<Integer> dom = new LinkedHashSet<Integer>();
			
			for(int j = 0; j < maxValue; j++) {
				dom.add(j);
			}
			
			domains.add(dom);
		}
	}

	
	/**
	 * delete a value from a domain given a variable
	 * @param variable
	 * @param value
	 */
	public void deleteValue(int variable, int value) {
		LinkedHashSet<Integer> dom = domains.get(variable);
		dom.remove(value);
		
	}
	
	
	/**
	 * print out the domainlist
	 */
	public String toString() {
		String s = "";
		for(int i = 0; i < numVariables; i++) {
			s = s + domains.get(i).toString() + "\n";
		}
		
		return s;
	}

	/**
	 * clone deep clone
	 */
	public Object clone() {
		DomainList copy;
		try {
			copy = (DomainList) super.clone();
		}
		catch( CloneNotSupportedException e )
		{
			return null;
		}

		copy.domains = new ArrayList<LinkedHashSet<Integer>>(numVariables);
		
		//  We've done the shallow copy of the DomainList, and it's stored in "copy"
		//  But the domains themselves need to be copied.  Since integers are immutable,
		//  it's sufficient to clone the linkedhashset corresponding to each domain
		for(int i = 0; i < numVariables; i++ ) {
			copy.domains.add((LinkedHashSet<Integer>)(domains.get(i).clone()));
		}
			
		return copy;
	} 

	
}

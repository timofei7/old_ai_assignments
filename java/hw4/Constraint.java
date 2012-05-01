package hw4;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * Contraint class
 * @author djb
 *
 */
public class Constraint {

	public HashSet<IntegerPair> allowedPairs = new HashSet<IntegerPair>();

	public Constraint(String valuePairList, Hashtable<String, Integer> valueHash) 
	{
	
		String []vpl = valuePairList.split(":");
		for(int i = 0; i < vpl.length; i++) 
		{
			String cpair = vpl[i].trim();
			String [] valuePairString = cpair.split("\\.");
			
			int value1 = valueHash.get(valuePairString[0]);
			int value2 = valueHash.get(valuePairString[1]);
			IntegerPair values = new IntegerPair(value1, value2);
			allowedPairs.add(values);
	
		}
		
	}
	
	boolean satisfied(int value1, int value2) 
	{
		IntegerPair pair = new IntegerPair(value1, value2);
		return allowedPairs.contains(pair);
	}
	
	public String toString()
	{
		return allowedPairs.toString();
	}
		
}
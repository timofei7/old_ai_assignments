package hw5;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Step 1: collect a count of all bigrams in the file. (A bigram is a pair of words, in sequence. So "international business" is a different bigram than "business international".) I'd recommend a hashtable.
 * Step 2: collect a count of all trigrams in the file. Another hashtable.
 * Step 3: Write a short method that, given each of these hashtables, and a triplet of words, gives the probability. 
 * @author tim
 *
 */
public class P1
{	
	public Map<String, Integer> bigrams;
	public Map<String, Integer> trigrams;
	
	public int bigramTotal;
	public int trigramTotal;
	
	public P1(String training)
	{
		bigrams = new HashMap<String, Integer>();
		trigrams = new HashMap<String, Integer>();
		bigramTotal = 0;
		trigramTotal = 0;
		
		countGrams(training);
	}
	
	
	/**
	 * counts all the bigrams and trigrams and files two hashtables with teh counts
	 * @param training
	 */
	private void countGrams(String training)
	{
		StringTokenizer sparser = new StringTokenizer(training); 		
		
		int count = 0; //counter for 
		LinkedList<String> temp2 = new LinkedList<String>();
		LinkedList<String> temp3 = new LinkedList<String>();
		
		while (true)
		{
			count++;
			
			if (sparser.hasMoreTokens())
			{
				String one = sparser.nextToken();
				temp2.add(one);
				temp3.add(one);
			}else if (temp2.size() < 2 && temp3.size() < 3)
			{
				break; //we're done here. 
			}
			
			if (count % 2 == 0 && temp2.size() >=2)
			{
				bigramTotal++;
				String two = temp2.get(0) + " " + temp2.get(1);

				if (!bigrams.containsKey(two))
				{
					bigrams.put(two, 1);
				}
				else
				{
					int c = bigrams.get(two);
					bigrams.put(two, ++c);
				}
				temp2.removeFirst();
			}
			
			if (count % 3 == 0 && temp3.size() >=3)
			{
				trigramTotal++;
				String three = temp3.get(0) + " " + temp3.get(1) + " " + temp3.get(2);

				if (!trigrams.containsKey(three))
				{
					trigrams.put(three, 1);
				}
				else
				{
					int c = trigrams.get(three);
					trigrams.put(three, ++c);
				}
				temp3.removeFirst();
			}
			
		} 
		
		System.out.println(bigramTotal);
		System.out.println(bigrams);
		System.out.println(trigramTotal);
		System.out.println(trigrams);
	}
	
	
	public double getProb(String s)
	{
		return 0;
	}
	
	
}

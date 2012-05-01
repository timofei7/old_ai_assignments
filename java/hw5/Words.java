package hw5;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Step 1: collect a count of all bigrams in the file. (A bigram is a pair of words, in sequence. So "international business" is a different bigram than "business international".) I'd recommend a hashtable.
 * Step 2: collect a count of all trigrams in the file. Another hashtable.
 * Step 3: Write a short method that, given each of these hashtables, and a triplet of words, gives the probability. 
 * @author tim
 *
 */
public class Words
{	
	public Map<String, Integer> bigrams;
	public Map<String, Integer> trigrams;
	public SortedSet<sortString> sortedDict;
	public HashMap<String, Integer> dict;
	
	public int bigramTotal;
	public int trigramTotal;
	
	
	/**
	 * constructor 
	 * @param training a string of the file for training
	 */
	public Words(String training)
	{
		bigrams = new HashMap<String, Integer>();
		trigrams = new HashMap<String, Integer>();
		sortedDict = new TreeSet<sortString>();
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
		HashMap<String, Integer> dict = new HashMap<String, Integer>();

		
		while (true)
		{
			count++;
			
			if (sparser.hasMoreTokens())
			{
				String one = sparser.nextToken().toLowerCase(); //lowercase it all
				addToDict(dict, one);  //add it to the temporary dict
				temp2.add(one);
				temp3.add(one);
			}else if (temp2.size() < 2 && temp3.size() < 3)
			{
				break; //we're done here. 
			}
			
			if (count % 2 == 0 && temp2.size() >=2) //bigrams
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
			
			if (count % 3 == 0 && temp3.size() >=3) //trigrams
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
		
		sortDict(dict); //sort the dictionary
		//System.out.println(sortedDict);
		//System.out.println(trigrams);
		//System.out.println(bigrams);
		
	}
	
	/**
	 * given the first two words of a sentence
	 * randomly selects the next word from a distribution
	 * that is consistent with the probablistic model.
	 * @param bigram
	 * @return
	 */
	public String nextWord(String bigram)
	{
		Random rand = new Random();
		double dart = 0;
		while (dart < .2)
			dart = rand.nextDouble();
		
		double accum = 0;
		//System.out.println("dart " + dart);
		for (sortString s : sortedDict)
		{
			accum = accum + bigramCondProb(s.string, bigram);
			//System.out.println(bigramCondProb(s.string, bigram));
			//System.out.println("accum: " + accum + " s: " + s.string);
			if (accum >= dart)
			{
				//System.out.println(bigramCondProb(s.string, bigram));
				return s.string;
			}
		}
		
		return "";
	}
	
	
	/**
	 * builds a 15 word sentence
	 * @param bigram
	 * @return
	 */
	public String buildSentence(String bigram)
	{
		String out = "";
		String s  = bigram.split(" ")[1];
		String ss = nextWord(bigram);
		out = bigram;
		for (int i = 0; i< 13; i++)
		{
			String t = "";
			out = out + " " + ss;
			t = nextWord(s + " " +ss);
			s = ss;
			ss = t;
		}
		return out;
	}
	
	
	/**
	 * get probability of triplet or a bigram
	 * @param s
	 * @return
	 */
	public double sP(String s)
	{
		double ret = 0d;
		//System.out.println(s);
		if (s.split(" ").length == 2)
		{
			//System.out.println(bigrams.get(s).doubleValue() + " / " + (double)bigramTotal);
			try{ ret = bigrams.get(s).doubleValue() / (double)bigramTotal;}
			catch(Exception e){ret = 0d;}
		}
		else if (s.split(" ").length == 3)
		{
			//System.out.println(trigrams.get(s).doubleValue() + " / " + (double)trigramTotal);
			try{ ret = trigrams.get(s).doubleValue() / (double)trigramTotal;}
			catch(Exception e){ret = 0d;}
		}

		return ret;
	}
	
	/**
	 * p(A | B C) = P (B C A) / P (B C).
	 * compute P( B C A) and P( B C) 
	 * frequency count for BCA and for BC, from the hashtables, and a count
	 * for the total number of bigrams and trigrams.
	 * P(B C A) should be frequence (B C A) / number of trigrams.
	 * @return p(A | B C) = P (B C A) / P (B C)
	 */
	public double bigramCondProb(String A, String BC)
	{
		double bca = sP(BC+" "+A);
		double bc = sP(BC);
		if (bc==0)
			return 0;
		else
			return bca / bc;
	}
	
	
	/**
	 * sorts the dictionary
	 * @param dict
	 */
	private void sortDict(HashMap<String, Integer> dict)
	{	
		for (String s: dict.keySet())
		{
			sortedDict.add(new sortString(s, dict.get(s)));
		}
	}
	
	/**
	 * adds the string to the dictionary of words
	 * @param one
	 */
	private void addToDict(HashMap<String, Integer> dict,String one)
	{
		if (dict.containsKey(one)) //update dict with new count
		{	
			int nc = dict.get(one);
			nc++;
			dict.put(one, nc);
		}
		else
		{
			dict.put(one, 1);
		}

	}
	
	/**
	 *class to allow sorting of strings according to the count
	 */
	private class sortString implements Comparable<sortString>
	{
		public String string;
		public int count;

		sortString(String string, int count)
		{
			this.string = string;
			this.count = count;
		}
		@Override
		public int compareTo(sortString o)
		{
			int diff = o.count - this.count; //gross hack but works, reverse order
			if (diff == 0)					 // breaks ties so that we don't lose fake dups by count
				return 1;
			else
				return diff;
		}
		public String toString()
		{
			return string + "=" + count;
		}
		@Override
		public boolean equals(Object o)
		{
			sortString s = (sortString) o;
			return this.string == s.string;
		}
		
	}
	
}

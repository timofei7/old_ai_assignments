package hw5;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Step 1: collect a count of all bigrams in the file. (A bigram is a pair of words, in sequence. So "international business" is a different bigram than "business international".) I'd recommend a hashtable.
 * Step 2: collect a count of all trigrams in the file. Another hashtable.
 * Step 3: Write a short method that, given each of these hashtables, and a triplet of words, gives the probability. 
 * @author tim
 *
 */
public class SpellCharacters
{	
	public Map<String, Integer> bigrams;
	public Map<String, Integer> trigrams;
	public final char[] dict = " abcdefghijklmnopqrstuvwxyz".toCharArray();
	
	public int bigramTotal;
	public int trigramTotal;
	
	public String string;
	
	
	/**
	 * constructor 
	 * @param training a string of the file for training
	 */
	public SpellCharacters(String training)
	{
		bigrams = new HashMap<String, Integer>();
		trigrams = new HashMap<String, Integer>();
		bigramTotal = 0;
		trigramTotal = 0;
		
		string = "";
		
		countGrams(training);
	}
	
	
	/**
	 * counts all the bigrams and trigrams and files two hashtables with teh counts
	 * @param training
	 */
	private void countGrams(String training)
	{
		StringTokenizer sparser = new StringTokenizer(training); 	
		String tokenized = " ";
		
		while (sparser.hasMoreTokens())
		{			
				tokenized = tokenized + sparser.nextToken().toLowerCase() + " "; //lowercase it all add spaces
		}
		
		string = tokenized;
				
		int count = 0; //counter for 
		LinkedList<Character> temp2 = new LinkedList<Character>();
		LinkedList<Character> temp3 = new LinkedList<Character>();
		
		for (char one : tokenized.toCharArray())
		{
			temp2.add(one);
			temp3.add(one);
		}

		
		while (true)
		{
			count++;
			if (temp2.size() < 2 && temp3.size() < 3)
			{
				break; //we're done here. 
			}
			
			if (count % 2 == 0 && temp2.size() >=2) //bigrams
			{
				bigramTotal++;
				String two = "" + temp2.get(0) + temp2.get(1);

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
				String three = "" + temp3.get(0) + temp3.get(1) + temp3.get(2);

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
		
		System.out.println(trigrams);
		System.out.println(bigrams);
		
	}
	
	/**
	 * given the first two words of a sentence
	 * randomly selects the next word from a distribution
	 * that is consistent with the probablistic model.
	 * @param bigram
	 * @return
	 */
	public Character nextChar(String bigram)
	{
		Random rand = new Random();
		double dart = 0;
		while (dart < .5)  //TODO: play with this value to increase correctness
			dart = rand.nextDouble();
		
		double accum = 0;
		//System.out.println("dart " + dart);
		for (Character s : dict)
		{
			accum = accum + bigramCondProb(s, bigram);
			//System.out.println("accum: " + accum + " s: " + s);
			if (accum >= dart)
			{
				//System.out.println(bigramCondProb(s, bigram));
				return s;
			}
		}
		
		return null;
	}
	
	
	/**
	 * builds a 15 word sentence
	 * @param bigram
	 * @return
	 */
	public String build(String bigram)
	{
		String out = "";
		Character s  = bigram.charAt(1);
		Character ss = nextChar(bigram);
		out = bigram;
		for (int i = 0; i< 13; i++)
		{
			out = out + ss;
			Character t = nextChar(""+s + ss);
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
		if (s.length() ==2)
		{
			//System.out.println(bigrams.get(s).doubleValue() + " / " + (double)bigramTotal);
			try{ ret = bigrams.get(s).doubleValue() / (double)bigramTotal;}
			catch(Exception e){ret = 0d;}
		}
		else if (s.length() == 3)
		{
			double t = 0d;
			try
			{
				t = trigrams.get(s).doubleValue(); //assume minimum count is one
				if (t == 0) {t = 1d;}
			}catch(Exception e) {ret = 1d;}
			
			//System.out.println(trigrams.get(s).doubleValue() + " / " + (double)trigramTotal);
			try{ ret = t / (double)trigramTotal;}
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
	public double bigramCondProb(Character A, String BC)
	{
		double bca = sP(BC+A);
		double bc = sP(BC);
		if (bc==0)
			return 0;
		else
			return bca / bc;
	}
	
}

package hw5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

public class P3Main
{

	private static final String trainingFile = "hw5/gclean.txt";  //gulliverstravels
	//private static final String trainingFile = "hw5/hfclean.txt"; //huckfin
	//private static final String trainingFile = "hw5/uclean.txt"; //ulysses
	//private static final String trainingFile = "hw5/chartest.txt"; //test

	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		

//		System.out.println("reading in training data...may take a little while");
//		Scanner sc = null;
//		try
//		{
//			sc = new Scanner(new File(trainingFile));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		
//		String input = "";
//		while (sc.hasNextLine())
//		{
//			input = input+"\n"+sc.nextLine();
//		}
//						
//		String errors = makeErrors(input, trainingFile + "_"+(int)Math.floor(0.10d * 100) + "percent_errors.txt", 0.10d);
//		
//		System.out.println("eerors: " + compareErrors(input, errors));
		
		Umbrellas U = new Umbrellas();
		U.viterbi();
		
		
		
//		Characters p = new Characters(input);
//		
//		Scanner txtinput = new Scanner(System.in);
//
//		while (true)
//		{
//			System.out.print("Type in two characters to start some sentences with: ");
//			String bigram = txtinput.next();
//			
//			for (int i=0;i<5; i++)
//				System.out.println(p.build(bigram));		
//		}

	}
	
	
	/**
	 * takes an input string and a filename introduces a bunch of errors
	 * returns the string and also saves the file
	 * @param input
	 * @param filename
	 * @return
	 */
	private static String makeErrors(String input, String filename, double probability)
	{
		final char[] dict = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		Random rand = new Random();
		double dart;
		String forstring=""; //for returning
		String forfile = ""; //for writing to file
		for (char c : input.toCharArray())
		{
			dart = rand.nextDouble();
			if (dart < probability)
			{
				if ("abcdefghijklmnopqrstuvwxyz".contains(""+c))
				{
					int ra = rand.nextInt(25);
					while (dict[ra] == c)
						ra = rand.nextInt(25);
					c = dict[ra];
				}
			}
			forfile = forfile + c;
			if (c == '\n')   // clean out carriage returns for string version
				c = ' ';
			forstring = forstring + c;
		}
			
		//TODO: do I need to be writing this out to file?
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			out.write(forfile);
			out.close();
		}
		catch (Exception e)
		{
			System.out.println("Error writing file " + e);
		}

		
		return forstring;
	}
	
	
	/**
	 * compares two strings and returns the percentage that differes
	 * ie for us the percentage of errors
	 * @param original
	 * @param errorfilled
	 * @return
	 */
	private static double compareErrors(String original, String errorfilled)
	{
		double diffcount = 0d;
		for (int i =0; i< original.length();i++)
		{
			if (original.charAt(i) != errorfilled.charAt(i))
			{
				diffcount++;
				System.out.println(diffcount);
			}
		}
		
		return diffcount / original.length();
	}

}

package hw5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

/**
 * this is CS44 W10 HW5 part 3b. 
 * 
 * @author tim
 *
 */
public class P3bMain
{

	//private static final String trainingFile = "hw5/spellcheck_first.txt";  //gulliverstravels
	//private static final String errorFile = "hw5/spellcheck_second_10perror.txt";  //gulliverstravels
	private static final String trainingFile = "hw5/sptrain.txt"; 
	private static final String errorFile = "hw5/sptrain_10perror.txt"; 

	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		Scanner txtinput = new Scanner(System.in);
		
		while (true)
		{
			System.out.print("Pick option: prepare test files=1, run spellcheck on default file set=2:");
			int option = txtinput.nextInt();
			
			switch(option)
			{
				case 1:
					makefiles(txtinput);
					break;
				case 2:
					spellcheck(txtinput);
					break;
				default:
					System.out.println("I don't understand.. try again");
			}
			
		}

	}
	
	
	/**
	 * preps a file with errors
	 * @param input
	 */
	private static void makefiles(Scanner input)
	{
		String file = "";
		int errors = 0;
		while (file=="")
		{
			System.out.print("percentage of errors:");
			errors = input.nextInt();
			System.out.print("original filename:");
			file = input.next();
		}
		
		
		System.out.println("reading in file...may take a little while");
		Scanner sc = null;
		try
		{
			sc = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String tinput = "";
		while (sc.hasNextLine())
		{
			tinput = tinput+"\n"+sc.nextLine();
		}
		
		String nfile = file.split("\\.")[0];
		
		String with_errors = makeErrors(tinput, nfile + "_"+errors + "perror.txt", errors*0.01d);
		
		System.out.println("error rate check:");
		System.out.println("errors: " + compareErrors(tinput, with_errors));

		
		
			
	}
	
	
	/**
	 * does a spell check
	 * @param input
	 */
	private static void spellcheck(Scanner input)
	{
		
		String tfile = trainingFile;
		String efile = errorFile;
		
		System.out.println("enter file (1) or defaults (2): ");
		int choice = input.nextInt();
		
		if (choice == 1)
		{
			System.out.print("file with errors:");
			efile = input.next();
			System.out.print("training file without errors:");
			tfile = input.next();
		}

		
		System.out.println("reading in training data...may take a little while");
		Scanner sc = null;
		try
		{
			sc = new Scanner(new File(tfile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String tinput = "";
		while (sc.hasNextLine())
		{
			tinput = tinput+"\n"+sc.nextLine();
		}
		
		
		System.out.println("reading in error text...may take a little while");
		try
		{
			sc = new Scanner(new File(efile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String einput = "";
		while (sc.hasNextLine())
		{
			einput = einput+"\n"+sc.nextLine();
		}

		
		SpellCheck sp = new SpellCheck(tinput, einput);
		sp.viterbi();
		

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
			}
		}
		
		return diffcount / original.length();
	}

}

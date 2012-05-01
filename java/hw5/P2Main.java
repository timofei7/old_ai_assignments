package hw5;

import java.io.File;
import java.util.Scanner;

/**
 * CS44 W10 HW5 p2 
 * @author tim
 *
 */
public class P2Main
{

	//private static final String trainingFile = "hw5/gclean.txt";  //gulliverstravels
	//private static final String trainingFile = "hw5/hfclean.txt"; //huckfin
	//private static final String trainingFile = "hw5/uclean.txt"; //ulysses
	private static final String trainingFile = "hw5/chartest.txt"; //test

	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		

		System.out.println("reading in training data..this may take some time...");
		Scanner sc = null;
		try
		{
			sc = new Scanner(new File(trainingFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String input = "";
		while (sc.hasNextLine())
		{
			input = input+" "+sc.nextLine();
		}
				
		Characters p = new Characters(input);
		
		Scanner txtinput = new Scanner(System.in);

		while (true)
		{
			System.out.print("Type in two characters to start some sentences with (try 'th' or 'my': ");
			String bigram = txtinput.next();
			
			for (int i=0;i<5; i++)
				System.out.println(p.build(bigram));		
		}

	}

}

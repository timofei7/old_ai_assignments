package hw5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class P1Main
{

	//private static final String trainingFile = "hw5/hfclean.txt";
	private static final String trainingFile = "hw5/hw5test.txt";

	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		

		
		Scanner sc = null;
		try {
			sc = new Scanner(new File(trainingFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String input = "";
		while (sc.hasNextLine())
		{
			input = input+" "+sc.nextLine();
		}
				
		P1 p = new P1(input);
		
		


	}

}

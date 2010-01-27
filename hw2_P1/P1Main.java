package hw2_P1;

import java.util.Scanner;


/**
 * 
 * @author tim tregubov
 * cs44 w10 hw2 p1
 */
public class P1Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		Path p = new Path();
		Scanner input = new Scanner(System.in);

		while (true)
		{
			System.out.print("Choose some paths to find (1,2) or 0 to quit: ");
			int which = input.nextInt();
			
			
			switch (which)
			{
				case 1:
					p.findURLS("http://www.cs.dartmouth.edu/~tim", "http://www.zingweb.com/tim/link.html");
					break;
				case 2: 
					p.findURLS("http://www.cs.dartmouth.edu", "http://www.cs.dartmouth.edu/~robotics");
					break;
				default:
					System.out.println("bailing!");
					System.exit(1);
			}
		}
					    
	}
}

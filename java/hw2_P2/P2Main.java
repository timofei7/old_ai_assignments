package hw2_P2;

import java.util.Scanner;


/**
 * text only driver for the robot solver
 * @author tim
 *
 */
public class P2Main
{
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		RobotSolver rs = new RobotSolver();
		
		
		Scanner input = new Scanner(System.in);

		while (true)
		{
			System.out.print("\nChoose some maps to find (1,2,3,4,5,6) or 0 to quit: ");
			int which = input.nextInt();
			
			
			switch (which)
			{
				case 1:
					rs.Solve(MMap.MapSet.MAP1);
					break;
				case 2: 
					rs.Solve(MMap.MapSet.MAP2);
					break;
				case 3:
					rs.Solve(MMap.MapSet.MAP3);
					break;
				case 4:
					rs.Solve(MMap.MapSet.MAP4);
					break;
				case 5:
					rs.Solve(MMap.MapSet.MAP5);
					break;
				case 6:
					rs.Solve(MMap.MapSet.MAP6);
					break;
				default:
					System.out.println("bailing!");
					System.exit(1);
			}
		}

	}

}

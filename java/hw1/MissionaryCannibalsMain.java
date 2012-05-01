package hw1;
import java.util.LinkedList;

/**
 * Drives the Missionary vs Cannibals solver and prints out some pretty output
 * @author tim tregubov
 * for cs44w10
 */

public class MissionaryCannibalsMain
{

	public static void main(String[] args)
	{
		MCSolver solver = new MCSolver();  //get us a solver

		LinkedList<MCSolver.State> output = solver.bfs(); //run the breadth-first search

		if (output != null)
		{
			MCSolver.State previous = output.getFirst(); // get the first state which is the start

			System.out.println("Found a solution to your Cannibal Vs Missionaries problems!");
			System.out.println("Total Moves: " + (output.size() - 1));
			System.out.println("Here they are:");
			
			for (int i = 0; i < output.size(); i++) //prep some output
			{
				MCSolver.State s = output.get(i);
				//System.out.println(s.toString());

				int p = previous.get();  //int representation
				int c = s.get(); 
				int d =  Math.abs((p - c) / 10); //get the first two digits
				String bdir_singular = " goes across.";
				String bdir_plural = " go across.";
				
				if (s.boat == 1){  //which direction are we going
					bdir_singular = " comes back.";
					bdir_plural = " come back.";
				}
				
				switch (d) { //reverse engineer the moves
					case 00: ;break;
					case 01: System.out.println("One cannibal"+bdir_singular); break;
					case 10: System.out.println("One missionary"+bdir_singular); break;
					case 11: System.out.println("One missionary and one cannibal"+bdir_plural); break;
					case 20: System.out.println("Two missionaries"+bdir_plural); break;
					case 02: System.out.println("Two cannibals"+bdir_plural); break;
					default: System.out.println("didn't know what to say: " + Integer.toString(d)); break;
				}
				previous = s;
			}
			
			System.out.println("and we are done!");
			System.out.println("\nin short notation the moves (in mcb) are: ");
			
			for (int i = 0; i < output.size(); i++) // short notation
			{
				MCSolver.State s = output.get(i);
				System.out.print(s.toString() + " ");
			}
			
			
		} else
		{
			System.out.println("no solution found! :-(");
		}

	}

}

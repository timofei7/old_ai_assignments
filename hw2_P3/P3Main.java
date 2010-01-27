package hw2_P3;

import java.util.Scanner;



public class P3Main
{
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		Scanner input = new Scanner(System.in);
		
		while (true)
		{
			System.out.print("Choose a map (1,2,3) or 0 to quit (4 at your own risk): ");
			int which = input.nextInt();
			
			
			MMap.MapSet map = MMap.MapSet.MAP1;
			switch (which)
			{
				case 1:
					map = MMap.MapSet.MAP1;
					break;
				case 2: 
					map = MMap.MapSet.MAP2;
					break;
				case 3:
					map = MMap.MapSet.MAP3;
					break;
				case 4:
					map = MMap.MapSet.MAP4;
					break;
				default:
					System.out.println("bailing!");
					System.exit(1);
					
			}
			
			System.out.println("solving this map:");
			
			SenselessSolver rs = new SenselessSolver(new MMap(map));
			
			for (int i =0; i< rs.map.gridSize; i++)
			{
				System.out.println(rs.map.current_map[i]);
			}
			rs.Solve();
			
		}
		
		
		
	}

}

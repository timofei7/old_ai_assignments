package hw2_P3;



public class P3Main
{
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("solving this map:");
		RobotSolver rs = new RobotSolver(new MMap(MMap.MapSet.MAP1));
		for (int i =0; i< rs.map.gridSize; i++)
		{
			System.out.println(rs.map.current_map[i]);
		}
		rs.Solve();
		
	}

}

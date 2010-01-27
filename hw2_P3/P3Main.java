package hw2_P3;

import java.util.ArrayList;

import hw2_P2.Loc;



public class P3Main
{
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("solving this map:");
		RobotSolver rs = new RobotSolver(new MMap(MMap.MapSet.MAP1));
		
//		Loc[] l = new Loc[3];
//		l[0] = new Loc(2,0);
//		l[1] = new Loc(2,1);
//		l[2] = new Loc(2,2);
//		
//		MLoc n = new MLoc(l);
//		
//		
//		ArrayList<MLoc> r = rs.getMovesCorrectlyPlease(n);
//		
//		for (int i = 0; i < r.size(); i++)
//		{
//			System.out.println(r.get(i));
//		}
//		
//		System.out.println(r.size());
//		System.out.println(r.toString());
		
		
		
		for (int i =0; i< rs.map.gridSize; i++)
		{
			System.out.println(rs.map.current_map[i]);
		}
		rs.Solve();
		
	}

}

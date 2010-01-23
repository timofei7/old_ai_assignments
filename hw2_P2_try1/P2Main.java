package hw2_P2_try1;


import java.util.LinkedList;

public class P2Main
{
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		RobotSolver rs = new RobotSolver(new MMap(MMap.MapSet.MAP1));
		//LinkedList<RobotState> solution = rs.uniAstar(0);
		LinkedList<MultiRobotState> foo = rs.astar();
		foo.toString();
	}

}

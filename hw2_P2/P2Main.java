package hw2_P2;

import java.util.LinkedList;

public class P2Main
{
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		RobotSolver rs = new RobotSolver();
		LinkedList<RobotState> solution = rs.astar();
	}

}

package hw2_P2;

import java.util.ArrayList;
import java.util.LinkedList;

public class P2Main
{
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		RobotSolver rs = new RobotSolver(new MMap(MMap.MapSet.MAP2));
		//LinkedList<RobotState> solution = rs.uniAstar(0);
		ArrayList<LinkedList<RobotState>> foo = rs.multipathAstar();
		foo.toString();
	}

}

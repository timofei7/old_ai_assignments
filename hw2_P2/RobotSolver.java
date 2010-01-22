package hw2_P2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class RobotSolver
{
	

	public MMap mazes;
	
	/**
	 * constructor, initializes the robots and some maps. 
	 */
	public RobotSolver(MMap m)
	{
		mazes = m;
	}
	
	
	/**
	 * search positions!
	 */
	public LinkedList<RobotState> astar()
	{
		PriorityHashQueue frontier = new PriorityHashQueue();
		Set<RobotState> explored = new HashSet<RobotState>();
		
		LinkedList<RobotState> solution = new LinkedList<RobotState>();
		
		frontier.add(new Node(mazes.starts[0], null, 0 + heuristic(mazes.starts[0], mazes.finishes[0]))); //add the first robot state
		explored = new HashSet<RobotState>();
		
		while (!frontier.isEmpty())
		{
			Node current = frontier.poll(); //chooses the lowest cost node in frontier
			
			
			if (goalTest(current.state))
			{
				for (Node node = current; node != null; node = node.parent)
				{
					solution.addFirst(node.state);  //add path nodes to solution
				}
				System.out.println("FOUND SOLUTION:" + solution.toString());
				return solution;  //yay!
			}
			
			explored.add(current.state);
			
			ArrayList<RobotState> possibles = getMoves(current.state);
			
			for (int i=0; i < possibles.size(); i++)
			{
				if (! explored.contains(possibles.get(i)))
				{
					frontier.add(new Node(possibles.get(i), current, current.distance + 1  + heuristic(mazes.starts[0], mazes.finishes[0])));
				}
				else
				{
					Node n = frontier.get(current.state);
					if (n != null && n.distance > current.distance + 1)
					{
					frontier.update(current, new Node(possibles.get(i), current, current.distance + 1  + heuristic(mazes.starts[0],  mazes.finishes[0])));
					}
				}
			}
		}
		return null;  //if the frontier is empty return failure
	}
	
	
	/**
	 * gets all the *possible* moves for a robot on the map
	 * @param r
	 * @return
	 * TODO: add checking for other robots!!!
	 */
	private ArrayList<RobotState> getMoves(RobotState r)
	{
		ArrayList<RobotState> sts = new ArrayList<RobotState>();
		
		
		RobotState up = new RobotState(r.x, r.y - 1);
		RobotState down = new RobotState(r.x, r.y + 1);
		RobotState left = new RobotState(r.x - 1, r.y);
		RobotState right = new RobotState(r.x + 1, r.y);
		
		if (r.y > 0  && !mazes.isCollision(up))
		{
			sts.add(up);
		}
		
		if (r.y < 6 && !mazes.isCollision(down))
		{
			sts.add(down);
		}
		
		if (r.x > 0 && !mazes.isCollision(left))
		{
			sts.add(left);
		}
		
		if (r.x < 6 && !mazes.isCollision(right))
		{
			sts.add(right);
		}
		
		return sts;

		
	}
	
	
	/**
	 * tests if we're at goal state
	 * @param r
	 * @return
	 */
	private boolean goalTest(RobotState r)
	{
		//System.out.println("GOALTEST: " +r.toString() +"with"+mazes.map1AF.toString()+ "and get: "+r.equals(mazes.map1AF));
		//return r.equals(RtoF.get(r));
		//System.out.println("Checking coords: " + r.x + "," + r.y);
		return r.equals(mazes.finishes[0]); //TODO: fix this hack
		//TODO make real test for all robots
	}
	
	
	/**
	 * distance heuristic in squares from the goal
	 * 
     * It is optimistic because it is the shortest distance (it doesn't consider obstacles, non-diagonal moves, or other robots).
     * Thus it is either exactly correct or the actual path was longer and so it is guaranteed to be optimistic.
	 * @param r the robot position
	 * @param t the target position (robot object holding coordinates)
	 * @return
	 */
	public int heuristic(RobotState r, RobotState t)
	{
		return Math.abs((t.x - r.x) + (t.y - r.y));
	}


}

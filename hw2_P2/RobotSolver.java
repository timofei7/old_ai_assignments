package hw2_P2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

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
	 * search positions for multiple robots
	 * @return solutions
	 */
	public ArrayList<LinkedList<RobotState>> multipathAstar()
	{
		ArrayList<LinkedList<RobotState>> allSols = new ArrayList<LinkedList<RobotState>>();
		ArrayList<PriorityHashQueue> allFrontiers = new ArrayList<PriorityHashQueue>();
		ArrayList<HashSet<RobotState>> allExplored = new ArrayList<HashSet<RobotState>>();
		
		//create the appropriate number of data structures
		for (int i=0; i< mazes.numRobots; i++)
		{
			allFrontiers.add(new PriorityHashQueue());
			allExplored.add(new HashSet<RobotState>());
			allSols.add(new LinkedList<RobotState>());
		}
		
		//insert the start position of each robot into each frontier
		for (int i=0; i<mazes.numRobots; i++)
		{
			allFrontiers.get(i).add(new Node(mazes.starts[i], null, 0 + heuristic(mazes.starts[i], mazes.finishes[i]))); //add the first robot state
		}

		
		while (!checkFrontsNSols(allFrontiers, allSols))
		{
			for (int i = 0; i < mazes.numRobots; i ++)
			{
				//get references to the right versions of these
				PriorityHashQueue frontier = allFrontiers.get(i);
				HashSet<RobotState> explored = allExplored.get(i);
				LinkedList<RobotState> solution = allSols.get(i);
				
				if (solution.isEmpty() && !frontier.isEmpty())
				{
					// the rest is almost the same as in a single case
					Node current = frontier.poll(); //chooses the lowest cost node in frontier
					
					if (goalTest(current.state, i))
					{
						for (Node node = current; node != null; node = node.parent)
						{
							solution.addFirst(node.state);  //add path nodes to solution
						}
						System.out.println("FOUND SOLUTION FOR " + i + " :" + solution.toString());
					}
					
					explored.add(current.state);
					
					ArrayList<RobotState> possibles = getMoves(current.state, i, allFrontiers);
					//ArrayList<RobotState> possibles = getMovesOLD(current.state);
					
					for (int j=0; j < possibles.size(); j++)
					{
						if (! explored.contains(possibles.get(j))) //TODO: check for other frontiers?
						{
							frontier.add(new Node(possibles.get(j), current, current.distance + 1  + heuristic(mazes.starts[i], mazes.finishes[i])));
						}
						else
						{
							Node n = frontier.get(current.state);
							if (n != null && n.distance > current.distance + 1)
							{
								frontier.update(current, new Node(possibles.get(j), current, current.distance + 1  + heuristic(mazes.starts[i],  mazes.finishes[i])));
							}
						}
					}
				}
				else
				{
					// just spin our wheels waiting for the others
				}
			}
		}
		return allSols; //return whatever solutions we gots
	}
	
	
	/**
	 * checks all of the frontiers for empty only returns true when all frontiers are empty
	 * or when all the solutions are full
	 * @param af
	 * @return
	 */
	private boolean checkFrontsNSols(ArrayList<PriorityHashQueue> af, ArrayList<LinkedList<RobotState>> sls)
	{
		boolean a = true;
		boolean s = true;
		
		for (int i = 0; i < mazes.numRobots; i ++)
		{
			a = a && af.get(i).isEmpty(); //make sure all frontiers are empty
			s = s && !sls.get(i).isEmpty();//make sure all solutions are not empty
		}
		return s || a;  //return either all empty frontiers (failure) or all solutions are full (success)
	}
	
	
	private ArrayList<RobotState> getMovesOLD(RobotState r)
	{
		ArrayList<RobotState> sts = new ArrayList<RobotState>();
		
		
		RobotState up = new RobotState(r.x, r.y - 1);
		RobotState down = new RobotState(r.x, r.y + 1);
		RobotState left = new RobotState(r.x - 1, r.y);
		RobotState right = new RobotState(r.x + 1, r.y);
		
		if (r.y > 0  && !mazes.isCollision(up) )
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
		
		//add a do nothing state //TODO: test if this is ever needed
		sts.add(r);
		
		return sts;
	}
	
	/**
	 * gets all the *possible* moves for a robot on the map
	 * @param r
	 * @return
	 * TODO: add checking for other robots!!!
	 */
	private ArrayList<RobotState> getMoves(RobotState r, int curr_i, ArrayList<PriorityHashQueue> frs)
	{
		ArrayList<RobotState> sts = new ArrayList<RobotState>();
		
		
		RobotState up = new RobotState(r.x, r.y - 1);
		RobotState down = new RobotState(r.x, r.y + 1);
		RobotState left = new RobotState(r.x - 1, r.y);
		RobotState right = new RobotState(r.x + 1, r.y);
		
		if (r.y > 0  && !mazes.isCollision(up) && !getMovesHelper(up, curr_i, frs))
		{
			sts.add(up);
		}
		
		if (r.y < 6 && !mazes.isCollision(down) && !getMovesHelper(down, curr_i, frs))
		{
			sts.add(down);
		}
		
		if (r.x > 0 && !mazes.isCollision(left) && !getMovesHelper(down, curr_i, frs))
		{
			sts.add(left);
		}
		
		if (r.x < 6 && !mazes.isCollision(right) && !getMovesHelper(down, curr_i, frs))
		{
			sts.add(right);
		}
		
		//add a do nothing state //TODO: test if this is ever needed
		sts.add(r);
		
		return sts;
	}
	
	
	/**
	 * checks for collisions with the first priority items of all the other frontiers
	 * TODO:  make just do contains rather than peek?  does this even work?
	 * @param r
	 * @param curr_i
	 * @param frs
	 * @return
	 */
	private boolean getMovesHelper(RobotState r, int curr_i, ArrayList<PriorityHashQueue> frs)
	{
		for (int i = 0; i < mazes.numRobots; i++)
		{
			if (i != curr_i)
			{
				RobotState nr;
				try
				{
					nr = frs.get(i).peek().state;
				}catch(Exception e)
				{
					nr = null;
				}
				if (nr != null && r.equals(nr))
				{
					return true; //if the next node of any of the other frontiers is in the same place return true.
				}
			}
		}
		return false;
		
	}
	
	/**
	 * tests if this particular robot state is at its goal state
	 * @param r the state we're comparing
	 * @param i the index of the robot we're checking for
	 * @return yep if yep
	 */
	private boolean goalTest(RobotState r, int i)
	{
		return r.equals(mazes.finishes[i]);
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

package hw2_P2_multinotwork;

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
    public LinkedList<MultiRobotState> astar()
    {
        PriorityHashQueue frontier = new PriorityHashQueue();
        Set<MultiRobotState> explored = new HashSet<MultiRobotState>();

        LinkedList<MultiRobotState> solution = new LinkedList<MultiRobotState>();

        frontier.add(new Node(new MultiRobotState(mazes.starts), null, 0 + heuristic(new MultiRobotState(mazes.starts),new MultiRobotState(mazes.finishes)))); //add the first robot state
        explored = new HashSet<MultiRobotState>();

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

            for (int i = 0; i < mazes.numRobots; i++)
            {
	            ArrayList<MultiRobotState> possibles = getMoves(current.state, i); //TODO do this for all robots
	
	            for (int j=0; j < possibles.size(); j++)
	            {
	                if (! explored.contains(possibles.get(j)))
	                {
	                    frontier.add(new Node(possibles.get(j), current, current.distance + 1  + heuristic(mazes.starts[0], mazes.finishes[0])));
	                }
	                else
	                {
	                    Node n = frontier.get(current.state);
	                    if (n != null && n.distance > current.distance + 1)
	                    {
	                    frontier.update(current, new Node(possibles.get(j), current, current.distance + 1  + heuristic(mazes.starts[0],  mazes.finishes[0])));
	                    }
	                }
	            }
            }
        }
        return null;  //if the frontier is empty return failure
    }

	
		

	private ArrayList<MultiRobotState> getMoves(MultiRobotState rs, int curr_i)
	{
		ArrayList<RobotState> sts = new ArrayList<RobotState>();
		ArrayList<MultiRobotState> msts = new ArrayList<MultiRobotState>();
		
		RobotState r = rs.rs[curr_i];
			
		RobotState up = new RobotState(r.x, r.y - 1);
		RobotState down = new RobotState(r.x, r.y + 1);
		RobotState left = new RobotState(r.x - 1, r.y);
		RobotState right = new RobotState(r.x + 1, r.y);
		
		if (r.y > 0  && !mazes.isCollision(up) && !getMovesHelper(up, curr_i, rs))
		{
			sts.add(up);
		}
		
		if (r.y < 6 && !mazes.isCollision(down) && !getMovesHelper(down, curr_i, rs))
		{
			sts.add(down);
		}
		
		if (r.x > 0 && !mazes.isCollision(left) && !getMovesHelper(down, curr_i, rs))
		{
			sts.add(left);
		}
		
		if (r.x < 6 && !mazes.isCollision(right) && !getMovesHelper(down, curr_i, rs))
		{
			sts.add(right);
		}
		
		//add a do nothing state //TODO: test if this is ever needed
		sts.add(r);
		
		for (int i=0; i < sts.size(); i++) //build states
		{
			
			msts.add(new MultiRobotState(sts.get(i), curr_i, rs));
		}
		
		return msts;
	}
	
	
	/**
	 * checks for collisions
	 * TODO: does this even work?
	 * @param r
	 * @param curr_i
	 * @param frs
	 * @return
	 */
	private boolean getMovesHelper(RobotState r, int curr_i, MultiRobotState rs)
	{
		for (int i = 0; i < mazes.numRobots; i++)
		{
			if (i != curr_i)
			{
				RobotState nr = rs.rs[i];
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
	private boolean goalTest(MultiRobotState r)
	{
		return r.equals(new MultiRobotState(mazes.finishes));
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
	
	
	public int heuristic(MultiRobotState r, MultiRobotState t)
	{
		int n = 0;
		for (int i = 0; i < r.size; i++)
		{
			n = n + heuristic(r.rs[i], t.rs[i]);
		}
		return n;
	}


}

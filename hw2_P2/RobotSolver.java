package hw2_P2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class RobotSolver
{
	

	public MMap mazes;
	
	public ArrayList<Map<Loc,Double>> waveFronts;
	
	public ArrayList<LinkedList<Loc>> solutions;
	public ArrayList<LinkedList<MultiLoc>> msolutions;
	
	/**
	 * constructor, initializes the robots and some maps. 
	 */
	public RobotSolver(MMap m)
	{
		waveFronts = new ArrayList<Map<Loc,Double>>();
		solutions = new ArrayList<LinkedList<Loc>>();
		msolutions = new ArrayList<LinkedList<MultiLoc>>();
		
		mazes = m;
		
		for (int i = 0; i < mazes.numRobots; i++)
		{
			waveFronts.add(astar2d(mazes.starts[i], mazes.finishes[i]));
		}
		
		astar3d(new MultiLoc(mazes.starts), new MultiLoc(mazes.finishes)); //TODO does this work?
	}
	
	
	/**
	 * search positions!
	 * @param s is start
	 * @param f is finish
	 */
	public void astar3d(MultiLoc s, MultiLoc f)
	{
		
		MPriorityHashQueue frontier = new MPriorityHashQueue();
		Set<MultiLoc> explored = new HashSet<MultiLoc>();
		
		LinkedList<MultiLoc> solution = new LinkedList<MultiLoc>();
		
		frontier.add(new MultiNode(s, null, 0 + heuristic(s))); //add the first robot state
		
		while (!frontier.isEmpty())
		{
			MultiNode current = frontier.poll(); //chooses the lowest cost node in frontier
			
			if (goalTest(current.state, f))
			{
				for (MultiNode node = current; node != null; node = node.parent)
				{
					solution.addFirst(node.state);  //add path nodes to solution
				}
				System.out.println("FOUND SOLUTION:" + solution.toString());
				msolutions.add(solution);
				return; // we stop here cause we're done!
			}
			
			explored.add(current.state);
			
			ArrayList<MultiLoc> possibles = getMoves(current.state);
			System.out.println("possible: " + possibles.toString());
			
			for (int i=0; i < possibles.size(); i++)
			{
				if (! explored.contains(possibles.get(i)))
				{
					frontier.add(new MultiNode(possibles.get(i), current, current.distance + 1  + heuristic(current.state)));
				}
				else
				{
					MultiNode n = frontier.get(current.state);
					if (n != null && n.distance > current.distance + 1)
					{
						frontier.update(current, new MultiNode(possibles.get(i), current, current.distance + 1  + heuristic(current.state)));
					}
				}
			}
		}
		//System.out.println(explored.toString());
		//System.out.println(explored.size());
	}
	
	
	
	
	
	
	/**
	 * search positions!
	 * instead of returning the solution here though, we return the explored graph
	 * which is now going to be the best heuristic
	 * remember to goes backwards from finish to start
	 * @param s is start
	 * @param f is finish
	 * @return a hashset of Nodes of all locations (hashed by location state)
	 */
	public Map<Loc,Double> astar2d(Loc s, Loc f)
	{
		boolean solSeen = false; //mark that we've seen a solution but don't quit quite yet
		
		PriorityHashQueue frontier = new PriorityHashQueue();
		Map<Loc,Double> explored = new HashMap<Loc,Double>();
		
		LinkedList<Loc> solution = new LinkedList<Loc>();
		
		frontier.add(new Node(f, null, 0 + manhattan(f, s))); //add the first robot state
		
		while (!frontier.isEmpty())
		{
			Node current = frontier.poll(); //chooses the lowest cost node in frontier
			
			if (goalTest(current.state, s) && !solSeen)
			{
				solSeen = true;
				for (Node node = current; node != null; node = node.parent)
				{
					solution.addFirst(node.state);  //add path nodes to solution
				}
				System.out.println("FOUND INITIAL PLANNING SOLUTION:" + solution.toString());
				solutions.add(reverse(solution));
				//NOTE: we do not return here but we wait and fully populate the explored graph
			}
			
			explored.put(current.state, current.distance);
			
			ArrayList<Loc> possibles = getMoves(current.state);
			
			for (int i=0; i < possibles.size(); i++)
			{
				if (! explored.containsKey(possibles.get(i)))
				{
					frontier.add(new Node(possibles.get(i), current, current.distance + 1  + manhattan(current.state, s)));
				}
				else
				{
					Node n = frontier.get(current.state);
					if (n != null && n.distance > current.distance + 1)
					{
						frontier.update(current, new Node(possibles.get(i), current, current.distance + 1  + manhattan(current.state, s)));
					}
				}
			}
		}
		//System.out.println(explored.toString());
		//System.out.println(explored.size());
		return explored;  //if the frontier is empty return failure
	}
	
	
	/**
	 * gets all the *possible* moves for a robot on the map
	 * @param r
	 * @return
	 * TODO: add checking for other robots!!!
	 */
	private ArrayList<Loc> getMoves(Loc r)
	{
		ArrayList<Loc> sts = new ArrayList<Loc>();
		
		
		Loc up = new Loc(r.x, r.y - 1);
		Loc down = new Loc(r.x, r.y + 1);
		Loc left = new Loc(r.x - 1, r.y);
		Loc right = new Loc(r.x + 1, r.y);
		
		if (r.y > 0  && !mazes.isCollision(up))
		{
			sts.add(up);
		}
		if (r.y < mazes.gridSize -1 && !mazes.isCollision(down))
		{
			sts.add(down);
		}
		
		if (r.x > 0 && !mazes.isCollision(left))
		{
			sts.add(left);
		}
		
		if (r.x < mazes.gridSize -1  && !mazes.isCollision(right))
		{
			sts.add(right);
		}
	
		//System.out.println("input: " + r.toString() + "out: " + sts.toString());
		return sts;

		
	}
	
	
	/**
	 * gets all the *possible* moves for all the sets of robots (but only moving one at a time for now)
	 * @param r
	 * @return
	 * TODO: add checking for other robots!!!
	 */
	private ArrayList<MultiLoc> getMoves(MultiLoc r)
	{
		ArrayList<MultiLoc> msts = new ArrayList<MultiLoc>();
		
		for (int i = 0; i <  mazes.numRobots; i++)
		{
			ArrayList<Loc> nlocs = getMoves(r.rs.get(i));
			
			for (int j=0; j < nlocs.size(); j++)
			{
				MultiLoc ml = new MultiLoc(nlocs.get(j), i, r);
				msts.add(ml);
			}
		}
		return msts;
	}
	
	
	
	/**
	 * tests if we're at goal state
	 * @param r
	 * @return
	 */
	private boolean goalTest(Loc r, Loc g)
	{
		return r.equals(g);
	}
	
	
	/**
	 * tests if we're at goal state
	 * @param r
	 * @return
	 */
	private boolean goalTest(MultiLoc r, MultiLoc g)
	{
		return r.equals(g);
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
	public int manhattan(Loc r, Loc t)
	{
		return Math.abs((t.x - r.x) + (t.y - r.y));
	}
	
	
	/**
	 * distance heuristic from the wavefront plan given
	 * this is sum of distances
	 * @param r the location to test
	 * @param s the precomputed map of distances
	 * @return the distance value recorded
	 */
	public double heuristic(MultiLoc r)
	{
		double dl = 0; 

		for (int j = 0; j < mazes.numRobots; j++)
		{
			dl = dl + waveFronts.get(j).get(r.rs.get(j));
		}

		return dl;
	}
	
	
	/**
	 * for testing purposes just reverses a linked list
	 * @param ll
	 * @return
	 */
	private LinkedList<Loc> reverse(LinkedList<Loc> ll)
	{
		LinkedList<Loc> nll = new LinkedList<Loc>();
		for (int i = 0; i < ll.size(); i++)
		{
			nll.addFirst(ll.get(i));
		}
		return nll;
	}


}

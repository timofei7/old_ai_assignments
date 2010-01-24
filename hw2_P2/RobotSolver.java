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
	
	public ArrayList<Map<Loc,Double>> waveFronts;  //generated complete distance maps to use as true heuristic
	
	public ArrayList<LinkedList<Loc>> solutions2d; //simple solutions
	public ArrayList<LinkedList<Loc>> solutions3d; //simple solutions
		
	public ArrayList<Set<TimeLoc>> blocked;  //this holds the paths that are currently blocked
	
	
	/**
	 * constructor, initializes the robots and some maps. 
	 */
	public RobotSolver(MMap m)
	{
		waveFronts = new ArrayList<Map<Loc,Double>>();
		solutions2d = new ArrayList<LinkedList<Loc>>();
		solutions3d = new ArrayList<LinkedList<Loc>>();
		blocked = new ArrayList<Set<TimeLoc>>();
		
		mazes = m;
		
		for (int i = 0; i < mazes.numRobots; i++)
		{
			waveFronts.add(astar2d(mazes.starts[i], mazes.finishes[i]));
		}
		
		for (int i = 0; i < mazes.numRobots; i++)
		{
			astar3d(mazes.starts[i], mazes.finishes[i], i);
		}
		
	}
	
	

	
	
	public void astar3d(Loc strt, Loc f, int robotindex)
	{
		boolean solSeen = false; //mark that we've seen a solution but don't quit quite yet

		TimeLoc s = new TimeLoc(strt.x, strt.y, 0); //make the start at 0 time
		
		TPriorityHashQueue frontier = new TPriorityHashQueue();
		Map<Loc,Double> explored = new HashMap<Loc,Double>();
		
		LinkedList<Loc> solution = new LinkedList<Loc>(); //my solution
		Set<TimeLoc> myblocks = new HashSet<TimeLoc>(); //where you shouldn't go
		
		frontier.add(new TimeNode(s, null, 0 + heuristic(s, robotindex))); //add the first robot state
		
		while (!frontier.isEmpty())
		{
			TimeNode current = frontier.poll(); //chooses the lowest cost node in frontier
			
			if (goalTest(new Loc(current.state), f) && !solSeen)
			{
				solSeen = true;
				for (TimeNode node = current; node != null; node = node.parent)
				{
					solution.addFirst(new Loc(node.state));  //add path nodes to solution
					myblocks.add(node.state); //add nodes so others don't try to go here
					myblocks.add(new TimeLoc(node.state.x, node.state.y, node.state.t+1));  //to prevent swaps 
				}
				System.out.println("FOUND *THE* SOLUTION:" + solution.toString());
				solutions3d.add(solution);
				blocked.add(myblocks);
				//NOTE: we do not return here but we wait and fully populate the explored graph
				//TODO: make it loop and wait at destination so it can move if necessary
			}
			
			explored.put(new Loc(current.state), current.distance);
			
			ArrayList<TimeLoc> possibles = getMoves(current.state);
			
			for (int i=0; i < possibles.size(); i++)
			{
				TimeLoc possib = possibles.get(i);
				if (! explored.containsKey(new Loc(possib)) && !checkBlocks(possib, robotindex))
				{
					frontier.add(new TimeNode(possib, current, current.distance + 1  + heuristic(current.state, robotindex)));
				}
				else
				{
					TimeNode n = frontier.get(current.state);
					if (n != null && n.distance > current.distance + 1)
					{
						frontier.update(current, new TimeNode(possib, current, current.distance + 1  + heuristic(current.state, robotindex)));
					}
				}
			}
		}
		System.out.println(explored.toString());
		System.out.println(explored.size());
		//return explored;  //if the frontier is empty return failure
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
				solutions2d.add(reverse(solution));
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
		return explored;  //if the frontier is empty return failure
	}
	
	
	/**
	 * checks the blocked list for blocks
	 * @param l
	 * @param r
	 * @return
	 */
	private boolean checkBlocks(TimeLoc l, int r)
	{
		boolean b = false;
		for (int i=0; i< blocked.size(); i++)
		{
			if (i!=r)
			{
				if (blocked.get(i).contains(l))
				{
					b = true;  // return true for any collision
				}
			}
		}return b;
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
		return sts;		
	}
	
	
	private ArrayList<TimeLoc> getMoves(TimeLoc r)
	{
		ArrayList<Loc> sts = getMoves(new Loc(r));
		
		ArrayList<TimeLoc> tsts = new ArrayList<TimeLoc>();
		
		for (int i = 0; i < sts.size(); i++)
		{
			tsts.add(new TimeLoc(sts.get(i), r.t + 1));
		}
		tsts.add(new TimeLoc(r.x, r.y, r.t +1 )); //also have a pause action!
		
		return tsts;
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
	 * @param i the index of the robot
	 * @return the distance value recorded
	 */
	public double heuristic(TimeLoc r, int i)
	{
		return waveFronts.get(i).get(new Loc(r.x, r.y));
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

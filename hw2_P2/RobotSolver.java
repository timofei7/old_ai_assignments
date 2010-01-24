package hw2_P2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * this is an a* based cooperative multi robot motion solver
 * nice
 * @author tim tregubov
 * for: CS44 W10
 *
 */
public class RobotSolver
{
	public final int searchDepth = 10; //we only search this far per iteration

	public MMap map; //the map object
	
	private ArrayList<Map<Loc,Double>> waveFronts;  //generated complete distance maps to use as true heuristic
	
	public ArrayList<LinkedList<Loc>> solutions; //the final cooperative paths for display
		
	private Set<TimeLoc> blocked;  //this holds the paths that are currently blocked
	
	private Random random;
	
	private Boolean[] terminalStops; // records finishes per robot index
	
	/**
	 * constructor, initializes the robots and some maps. 
	 */
	public RobotSolver(MMap m)
	{
		map = m;
		
		waveFronts = new ArrayList<Map<Loc,Double>>();
		solutions = new ArrayList<LinkedList<Loc>>();
		blocked = new HashSet<TimeLoc>();
		terminalStops = new Boolean[map.numRobots];
		random = new Random();
							
		// build our wavefront plans!
		for (int i = 0; i < map.numRobots; i++)
		{
			waveFronts.add(astar2d(map.starts[i], map.finishes[i]));
		}
		
		//run a search for each bot
		for (int i = 0; i < map.numRobots; i++)
		{
			solutions.add(new LinkedList<Loc>());  //fill in empty
			astar3d(map.starts[i], map.finishes[i], i);
		}
		
//		//then run some more if we must, alternate randomly to increase likelihood of blockage clearage
//		int last=0;
//		while (!terminalStopCheck())
//		{
//			int i = random.nextInt(map.numRobots);
//
//			if (last==i)
//			{
//				i = random.nextInt(map.numRobots);
//			}
//			
//			astar3d(solutions.get(i).getLast(), map.finishes[i], i); //restart from last position
//			last = i;
//		}
		
	}
	

	
	/**
	 * does an a* search, using the true heuristic and the builds/looks at a
	 * blocked list of other robots to prevent run ins
	 * @param strt is start
	 * @param f is finish
	 * @param robotindex the index of the current robot
	 * @return 
	 */
	private void astar3d(Loc strt, Loc f, int robotindex)
	{
		TimeLoc s = new TimeLoc(strt.x, strt.y, 0); //make the start at 0 time
		int depth = 0;   //depth level counter
		
		TPriorityHashQueue frontier = new TPriorityHashQueue(); //states to look at
		Map<Loc,Double> explored = new HashMap<Loc,Double>();   //explored locations
		
		LinkedList<Loc> solution = new LinkedList<Loc>(); //this robots solution
		
		//TODO: do I need a parent if I'm resuming from a partial solution?
		frontier.add(new TimeNode(s, null, 0 + heuristic(s, robotindex))); //add the first robot state
		
		//run the search
		while (!frontier.isEmpty())
		{
			TimeNode current = frontier.poll(); //chooses the lowest cost node in frontier
			
			//this only happens if we reach the reaaaal goal
			if (goalTest(new Loc(current.state), map.finishes[robotindex]))
			{
				terminalStops[robotindex] = true; //note that we've finished
			}
			
			if (terminalStops[robotindex] !=null && terminalStops[robotindex])
			{	//make sure no matter what that our current location if we're done is on the blocked list
				blocked.add(current.state);
				blocked.add(new TimeLoc(current.state.x, current.state.y, current.state.t+1));
			}

			// goal test
			if (goalTest(new Loc(current.state), f) || (terminalStops[robotindex] !=null && terminalStops[robotindex] && depth >= searchDepth ))
			{
				for (TimeNode node = current; node != null; node = node.parent)
				{
					solution.addFirst(new Loc(node.state));  //add path nodes to solution
					blocked.add(node.state); //add nodes so others don't try to go here
					blocked.add(new TimeLoc(node.state.x, node.state.y, node.state.t+1));  //to prevent swaps
				}
				System.out.println("FOUND SOLUTION:" + solution.toString());
				solutions.get(robotindex).addAll(solution);
				return;
			}
			
			
			explored.put(new Loc(current.state), current.distance);
			
			ArrayList<TimeLoc> possibles = getMoves(current.state, robotindex);
			
			for (int i=0; i < possibles.size(); i++) // for all the children
			{
				TimeLoc possib = possibles.get(i); 
				
				// if we haven't explored it yet
				// add it to frontier with a distance
				if (! explored.containsKey(new Loc(possib)))
				{
					frontier.add(new TimeNode(possib, current, current.distance + 1  + heuristic(current.state, robotindex)));
				}
				else // if we've explored it check that its not at a lower cost now
				{
					TimeNode n = frontier.get(current.state);
					if (n != null && n.distance > current.distance + 1)
					{
						if (terminalStops[robotindex] && current.state == new TimeLoc(map.finishes[robotindex], current.state.t))
						{   //if we are paused on the goal.. make cost 0 but keep planning in case we need to move
							frontier.update(current, new TimeNode(possib, current, current.distance + 0)); 
						}else
						{
							frontier.update(current, new TimeNode(possib, current, current.distance + 1  + heuristic(current.state, robotindex)));
						}
					}
				}
			}
			depth++;
		}
	}
	
	
	
	/**
	 * build wavefront heuristic
	 * instead of returning the solution here though, we return the explored graph
	 * which is now going to be the best heuristic
	 * remember to goes backwards from finish to start
	 * @param s is start
	 * @param f is finish
	 * @return a hashset of Nodes of all locations (hashed by location state)
	 */
	private Map<Loc,Double> astar2d(Loc s, Loc f)
	{
		
		PriorityHashQueue frontier = new PriorityHashQueue();
		Map<Loc,Double> explored = new HashMap<Loc,Double>();		
		
		frontier.add(new Node(f, null, 0 + manhattan(f, s))); //add the first robot state
		
		while (!frontier.isEmpty())
		{
			Node current = frontier.poll(); //chooses the lowest cost node in frontier
			
			//NOTE we do not backtrack and make a solution here
			//NOTE we also don't return once we've found the solution
			//NOTE instead we consider all options exhaustively
			//NOTE to create a wavefront planned heuristic
			
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
		return explored;  
	}
	
	
	
	private boolean onBlocked(TimeLoc tl, int ri)
	{
		boolean b = blocked.contains(tl);
		System.out.println("checking: " + tl +" for: " + ri + " = " + b);

		return b;
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
		
		if (r.y > 0  && !map.isCollision(up))
		{
			sts.add(up);
		}
		if (r.y < map.gridSize -1 && !map.isCollision(down))
		{
			sts.add(down);
		}
		
		if (r.x > 0 && !map.isCollision(left))
		{
			sts.add(left);
		}
		
		if (r.x < map.gridSize -1  && !map.isCollision(right))
		{
			sts.add(right);
		}
		return sts;		
	}
	
	
	/**
	 * get all the *possible* moves with time in mind
	 * available to populate the graph
	 * this includes a pause action
	 * @param r
	 * @return
	 */
	private ArrayList<TimeLoc> getMoves(TimeLoc r, int ri)
	{
		ArrayList<Loc> sts = getMoves(new Loc(r));
		
		ArrayList<TimeLoc> tsts = new ArrayList<TimeLoc>();
		
		for (int i = 0; i < sts.size(); i++)
		{
			TimeLoc n = new TimeLoc(sts.get(i), r.t + 1);
			if (!onBlocked(n, ri))  //checks for time block
			{
				tsts.add(n);
			}
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
	 * manhattan distance heuristic in squares from the goal
	 * 
     * It is optimistic because it is the shortest distance (it doesn't consider obstacles, non-diagonal moves, or other robots).
     * Thus it is either exactly correct or the actual path was longer and so it is guaranteed to be optimistic.
	 * @param r the robot position
	 * @param t the target position (robot object holding coordinates)
	 * @return
	 */
	private int manhattan(Loc r, Loc t)
	{
		return Math.abs((t.x - r.x) + (t.y - r.y));
	}
	
	
	/**
	 * distance heuristic from the wavefront plan given
	 * @param r the location to test
	 * @param i the index of the robot
	 * @return the distance value recorded
	 */
	private double heuristic(TimeLoc r, int i)
	{
		return waveFronts.get(i).get(new Loc(r.x, r.y));
	}
	
	
	/**
	 * checks if all robots have finished
	 * @return
	 */
	private boolean terminalStopCheck()
	{
		int c = 0;
		for (int i = 0; i < terminalStops.length; i++)
		{
			if (terminalStops[i] != null && terminalStops[i])
			{
				c = c + 1;
			}

		}
		if (c == map.numRobots)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}

package hw2_P2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * this is an a* based cooperative multi robot motion solver
 * nice
 * @author tim tregubov
 * for: CS44 W10
 *
 */
public class RobotSolver
{
	public MMap map; //the map object
	
	private ArrayList<Map<Loc,Double>> waveFronts;  //generated complete distance maps to use as true heuristic
	
	public ArrayList<LinkedList<Loc>> solutions; // the final cooperative paths for display
	public LinkedList<MLoc> mSolutions; // the final cooperative paths for display

		
	/**
	 * basic constructor
	 */
	public RobotSolver()
	{		
		waveFronts = new ArrayList<Map<Loc,Double>>();
		solutions = new ArrayList<LinkedList<Loc>>();
		mSolutions = new LinkedList<MLoc>();
	}
	
	/**
	 * run the solver on some map
	 * @param mapset
	 */
	public void Solve(MMap.MapSet mapset)
	{
		map = new MMap(mapset);
	
		waveFronts = new ArrayList<Map<Loc,Double>>();
		solutions = new ArrayList<LinkedList<Loc>>();
		mSolutions = new LinkedList<MLoc>();
							
		// build our wavefront plans!
		for (int i = 0; i < map.numRobots; i++)
		{
			waveFronts.add(astar2d(map.starts[i], map.finishes[i]));
			solutions.add(new LinkedList<Loc>()); //add empty lists
		}
		
		astarMulti(map.starts, map.finishes);
		
		// separate out the result states into separate robot moves
		for (int i = 0; i < mSolutions.size(); i++)
		{
			for (int j = 0; j < map.numRobots; j++)
			{
				solutions.get(j).add(mSolutions.get(i).locs.get(j));
			}
		}
		
		//print em out
		for (int i=0; i< map.numRobots; i++)
		{
			System.out.println("robot: " + i + " moves: " + solutions.get(i).toString());
		}	
	}
	
	
	/**
	 * run astar over the state of all robots!
	 * @param fs start states
	 * @param ff finish states
	 */
	private void astarMulti(Loc[] fs, Loc[] ff)
	{
		MLoc s = new MLoc(fs);
		MLoc f = new MLoc(ff);
		
		int turnKeeper = 0;
		int counter = 0;
		
		MPriorityHashQueue frontier = new MPriorityHashQueue(); //states to look at
		Map<MLoc,Double> explored = new HashMap<MLoc,Double>();   //explored locations
		
		LinkedList<MLoc> solution = new LinkedList<MLoc>(); //the solutions
		
		frontier.add(new MNode(s, null, 0, heuristic(s))); //add the first robot state
		
		//run the search
		while (!frontier.isEmpty())
		{

			//progress printer
			if (counter % 100 == 0){System.out.print("\n");}; //these can be adjusted
			if (counter % 1 == 0) {System.out.print(".");}; 
			
			MNode current = frontier.poll(); //chooses the lowest cost node in frontier
									
			// build plan if goal and we've planned enough steps to cover longest running bot
			if (goalTest(current.state, f))
			{
				for (MNode node = current; node != null; node = node.parent)
				{
					solution.addFirst(node.state);  //add path nodes to solution
				}
				System.out.println("found goal!");
				mSolutions.addAll(solution);
				return;
			}
			
			explored.put(current.state, current.distance());  //add current state and the F cost
			
			ArrayList<MLoc> possibles = getMoves(current.state, turnKeeper);
						
			for (int i=0; i < possibles.size(); i++) // for all the children
			{
				MLoc possib = possibles.get(i); 
								
				// if we haven't explored it yet
				// add it to frontier with a distance
				if (! explored.containsKey(possib))
				{
					frontier.add(new MNode(possib, current, current.G + 1, heuristic(current.state)));
				}
				else if (current.state.equals(possib)) //allow for a turn skip if we're stuck
				{
					frontier.add(new MNode(possib, current, current.G + 1, heuristic(current.state)));
				}
				// if we've explored it check that its not at a lower cost now
				else
				{
					MNode n = frontier.get(current.state);
					if (n != null && n.distance() > current.distance())//
					{
						frontier.update(current, new MNode(possib, current, current.G + 1, heuristic(current.state)));
					}
				}
			}
			turnKeeper = (turnKeeper + 1) % map.numRobots; //next robot
			counter++;
		}
		if (solution.isEmpty())
		{
			System.out.println("FAILED TO FIND SOLUTION");
		}
	}	
		
	
	/**
	 * build wavefront heuristic
	 * instead of returning the solution here though, we return the explored graph
	 * which is now going to be the best heuristic
	 * if we had problems running this we could implement a continuable a* to
	 * continue searching if we asked for a value not in expolored
	 * but this is the least of our worries at this point
	 * remember to goes backwards from finish to start
	 * @param s is start
	 * @param f is finish
	 * @return a hashset of Nodes of all locations (hashed by location state)
	 */
	private Map<Loc,Double> astar2d(Loc s, Loc f)
	{
		
		PriorityHashQueue frontier = new PriorityHashQueue();
		Map<Loc,Double> explored = new HashMap<Loc,Double>();		
		
		frontier.add(new Node(f, null, 0, manhattan(f, s))); //add the first robot state
		
		while (!frontier.isEmpty())
		{
			Node current = frontier.poll(); //chooses the lowest cost node in frontier
			
			//NOTE we do not backtrack and make a solution here
			//NOTE we also don't return once we've found the solution
			//NOTE instead we consider all options exhaustively
			//NOTE to create a wavefront planned heuristic
			
			explored.put(current.state, current.G);
			
			ArrayList<Loc> possibles = getMoves(current.state);
			
			for (int i=0; i < possibles.size(); i++)
			{
				if (! explored.containsKey(possibles.get(i)))
				{
					frontier.add(new Node(possibles.get(i), current, current.G + 1, manhattan(current.state, s)));
				}
				else
				{
					Node n = frontier.get(current.state);
					if (n != null && n.distance() > current.distance())
					{
						frontier.update(current, new Node(possibles.get(i), current, current.G + 1, manhattan(current.state, s)));
					}
				}
			}
		}
		return explored;  
	}	
	
	/**
	 * gets all the *possible* moves for a robot on the map
	 * @param r
	 * @return
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
	 * adds a pause state to the possible moves
	 * @param r
	 * @return
	 */
	private ArrayList<Loc> getMovesWithPause(Loc r)
	{
		ArrayList<Loc> sts = getMoves(r);
		
		sts.add(r); //include pause
		
		return sts;		
	}
	
	
	/**
	 * get all the *possible* moves
	 * available to populate the graph
	 * this includes a pause action
	 * @param r
	 * @return
	 */
	public ArrayList<MLoc> getMoves(MLoc ml, int r)
	{
		ArrayList<Loc> ls = getMovesWithPause(ml.locs.get(r));
				
		ArrayList<MLoc> mls = new ArrayList<MLoc>();
		for (int i = 0; i < ls.size(); i++)
		{
			if (!colTest(ls.get(i), ml, r))
			{
				MLoc nml = ml.clone();
				nml.locs.set(r, ls.get(i));
				mls.add(nml);
			}
		}
		return mls;
	}
	
	
	//TODO:  make simultaneous moves
	
	/**
	 * checks collisions between a robot and its neighbors
	 */
	private boolean colTest(Loc l, MLoc ml, int r)
	{
		boolean b = false;
		for (int i = 0; i < map.numRobots; i++)
		{
			if (r != i)
			{
				b = b || l.equals(ml.locs.get(i));
			}
		}
		return b;
	}
	
		
	/**
	 * tests if we're at goal state
	 * @param r
	 * @return
	 */
	private boolean goalTest(MLoc r, MLoc g)
	{
		return r.equals(g);
	}
	
	
	/**
	 * manhattan distance heuristic in squares from the goal
	 * 
     * It is optimistic because it is the shortest distance (it doesn't consider obstacles, non-diagonal moves, or other robots).
     * Thus it is either the shortest grid distance or the actual path was longer and so it is guaranteed to be optimistic.
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
	private double heuristic(MLoc r)
	{
		double cost = 0;
		for (int i =0; i< map.numRobots; i++)
		{
			cost = cost + waveFronts.get(i).get(r.locs.get(i)); //sum of distances from wavefront
		}
		return cost;
	}

}

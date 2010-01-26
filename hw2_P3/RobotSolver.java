package hw2_P3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import hw2_P2.*;

/**
 * this is an a* based cooperative multi robot motion solver
 * nice
 * @author tim tregubov
 * for: CS44 W10
 *
 */
public class RobotSolver
{

	public static enum ActionSet { NORTH, SOUTH, EAST, WEST};
	
	public MMap map; //the map object
	
	private Map<Loc,Double> waveFront;  //generated complete distance map
	
	public ArrayList<LinkedList<Loc>> solutions; // the final cooperative paths for display
	public LinkedList<MLoc> mSolutions; // the final cooperative paths for display

		
	/**
	 * constructor, initializes the robots and some maps. 
	 */
	public RobotSolver(MMap m)
	{
		map = m;
		
		waveFront = new HashMap<Loc,Double>();
		solutions = new ArrayList<LinkedList<Loc>>();
		mSolutions = new LinkedList<MLoc>();
							
		waveFront = astar2d(map.start, map.finish);
		
		//astarMulti(getInitBeliefs());
		
//		MLoc test = new MLoc(3);
//		test.locs[0] = new Loc(3, 0);
//		test.locs[1] =new Loc(3, 1);
//		test.locs[2] =new Loc(3, 2);
//		System.out.println(getMoves(test));
		
		astarMulti(map.start, map.finish);
		
//		// separate out the result states into separate robot moves
//		for (int i = 0; i < mSolutions.size(); i++)
//		{
//			for (int j = 0; j < map.numRobots; j++)
//			{
//				solutions.get(j).add(mSolutions.get(i).locs[j]);
//			}
//		}
//		
//		//print em out
//		for (int i=0; i< map.numRobots; i++)
//		{
//			System.out.println("robot: " + i + " moves: " + solutions.get(i).toString());
//		}	
	}
	

	private void astarMulti(Loc[] is)
	{
		

		MLoc s = new MLoc(is);
		MLoc f = new MLoc(1);
		f.locs[0] = map.finish;
				
		MPriorityHashQueue frontier = new MPriorityHashQueue(); //states to look at
		Map<MLoc,Double> explored = new HashMap<MLoc,Double>();   //explored locations
		
		LinkedList<MLoc> solution = new LinkedList<MLoc>(); //the solutions
		
		frontier.add(new MNode(s, null, 0 + heuristic(s))); //add the first robot state
		
		//run the search
		while (!frontier.isEmpty())
		{
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
			
			explored.put(current.state, current.distance);
			
			ArrayList<MLoc> possibles = getMoves(current.state);
						
			for (int i=0; i < possibles.locs.length; i++) // for all the children
			{
				MLoc possib = possibles.locs[i]; 
								
				// if we haven't explored it yet
				// add it to frontier with a distance
				if (! explored.containsKey(possib))
				{
					frontier.add(new MNode(possib, current, current.distance + 1  + heuristic(current.state)));
				}
				else if (current.state.equals(possib)) //allow for a turn skip if we're stuck
				{
					frontier.add(new MNode(possib, current, current.distance + 1  + heuristic(current.state)));
				}
				// if we've explored it check that its not at a lower cost now
				else
				{
					MNode n = frontier.get(current.state);
					if (n != null && n.distance > current.distance)// + 1)
					{
						frontier.update(current, new MNode(possib, current, current.distance + 1  + heuristic(current.state)));
					}
				}
			}
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
	
	
	
	private Loc[] getInitBeliefs()
	{
		ArrayList<Loc> l = new ArrayList<Loc>();
		for (int i = 0; i < map.gridSize; i++)
		{
			for (int j = 0; j < map.gridSize; j++)
			{
				if (map.current_map[i].charAt(j) == '.')
				{
					l.add(new Loc(i,j));
				}
			}
		}
		Loc[] ll = new Loc[l.size()];
		l.toArray(ll);
		return ll;
	}
	
	
	/**
	 * gets all the *possible* moves for a robot on the map
	 * @param r
	 * @return
	 */
	private ArrayList<MLoc> getMoves(MLoc r)
	{
		ArrayList<MLoc> msts = new ArrayList<MLoc>();

		for (int dir = 0; dir < 4; dir++)
		{
			ArrayList<Loc> sts = new ArrayList<Loc>();

			for (int i = 0; i < r.locs.length; i++)
			{
				Loc nd;
				
				switch (dir)
				{
					case 0:  //north
						nd = new Loc(r.locs[i].x, r.locs[i].y - 1);
						if (r.locs[i].y > 0  && !map.isCollision(nd))
						{
							sts.add(nd);
						}
						break;
					case 1: //south
						nd = new Loc(r.locs[i].x, r.locs[i].y + 1);
						if (r.locs[i].y < map.gridSize -1 && !map.isCollision(nd))
						{
							sts.add(nd);
						}
						break;
					case 2: //east
						nd = new Loc(r.locs[i].x - 1, r.locs[i].y);
						if (r.locs[i].x > 0 && !map.isCollision(nd))
						{
							sts.add(nd);
						}
						break;
					case 3: //west
						nd = new Loc(r.locs[i].x + 1, r.locs[i].y);
						if (r.locs[i].x < map.gridSize -1  && !map.isCollision(nd))
						{
							sts.add(nd);
						}
						break;
				}
			}
			MLoc t = new MLoc(sts.size());
			sts.toArray(t.locs);
			msts.add(t);
		}		
		return msts;
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
	 * checks collisions between a location and the belief state
	 */
	private boolean colTest(Loc l, MLoc ml, int r)
	{
		boolean b = true;
		for (int i = 0; i < ml.locs.length; i++)
		{
			if (r != i)
			{
				b = b && l.equals(ml.locs[i]);
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
	private double heuristic(MLoc r)
	{
		double cost = 0;
		for (int i =0; i< r.locs.length; i++)
		{
			cost = cost + waveFront.get(r.locs[i]); //sum of distances from wavefront
		}
		return cost;
	}

}

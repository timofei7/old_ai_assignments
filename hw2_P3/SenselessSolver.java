package hw2_P3;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


/**
 * this is a tiny sensorless robot planning solver
 * 
 * @author tim tregubov
 * for: CS44 W10 HW2 P3
 */
public class SenselessSolver
{

	public static enum ActionSet { NORTH, SOUTH, EAST, WEST}; //why doesn't java let you index these by int and also print out strings from them like python?
	
	public MMap map; //the map object
		
	public LinkedList<State> mSolutions; // the final cooperative paths for display

		
	/**
	 * constructor, initializes the robots and some maps. 
	 */
	public SenselessSolver(MMap m)
	{
		map = m;
		mSolutions = new LinkedList<State>();
		
	}
	
	/**
	 * run the solver
	 */
	public void Solve()
	{
		bfs(getInitBeliefs());
		
		System.out.println("In order of moves: ");
		for (int i = 0; i < mSolutions.size(); i++)
		{
			System.out.println("Go " + getDir(mSolutions.get(i).id) + " to get: \t" + mSolutions.get(i).toString());
		}
	}
		
	/**
	 * runs a simple bfs on the state space
	 * @param is
	 */
	private void bfs(State s)
	{
		
		s.id = -1;
		Loc f = map.finish; //the finish
		
		int counter = 0;
				
		LinkedList<Node> frontier = new LinkedList<Node>(); //states to look at
		Set<State> explored = new HashSet<State>();   //explored locations
		
		LinkedList<State> solution = new LinkedList<State>(); //the solutions
		
		frontier.addLast(new Node(s, null, 0)); //add the first robot state
		
		//run the search
		while (!frontier.isEmpty())
		{
			Node current = frontier.removeFirst(); //chooses the first node in frontier
									
			// build plan if goal
			if (goalTest(current.state, f))
			{
				for (Node node = current; node != null; node = node.parent)
				{
					solution.addFirst(node.state);  //add path nodes to solution
				}
				System.out.println("found goal!");
				System.out.println("moves: " + solution.size());
				mSolutions.addAll(solution);
				return;
			}
			
			explored.add(current.state);
			
			if (counter % 5000 == 0){System.out.print("\n");};
			if (counter % 50 == 0) {System.out.print(".");}; 
						
			ArrayList<State> possibles =  getMovesCorrectlyPlease(current.state);

			for (int i=0; i < possibles.size(); i++) // for all the children
			{
				State possib = possibles.get(i); 
				
				// if we haven't explored it yet
				// add it to frontier with a distance
				if (! explored.contains(possib))
				{
					frontier.addLast(new Node(possib, current, current.distance + 1));
				}
			}
			counter++;
		}
		if (solution.isEmpty())
		{
			System.out.println("FAILED TO FIND SOLUTION");
		}
	}	
	
	
	

	/**
	 * get all the initial possible positions from a map
	 * @return
	 */
	private State getInitBeliefs()
	{
		State s = new State();

		for (int i = 0; i < map.gridSize; i++)
		{
			for (int j = 0; j < map.gridSize; j++)
			{
				if (map.current_map[i].charAt(j) == '.')
				{
					s.add(new Loc(j,i));
				}
			}
		}
		System.out.println("initial states: " + s.toString());
		return s;
	}
	
	
	/**
	 * gets all the *possible* moves for a robot on the map
	 * @param r
	 * @return
	 */
	public ArrayList<State> getMovesCorrectlyPlease(State rr)
	{
		ArrayList<State> msts = new ArrayList<State>();
		
		Loc[] r = new Loc[rr.size()];
		rr.toArray(r); //make it so we can index into the hash state

		for (int dir = 0; dir < 4; dir++)
		{
			State sts = new State();

			for (int i = 0; i < r.length; i++)
			{
				int x = r[i].x;
				int y = r[i].y;
				
				switch (dir)
				{   
					// how do we trim states?
					case 0:  //north
						if (canAdd(r[i], dir, rr))
						{
							Loc nl = new Loc(x, y - 1);
							sts.add(nl);
						}
						if (!mapConstraint(r[i], dir)  && !pieceConstraint(r[i], dir, rr))
						{
							sts.add(new Loc(x, y));
						} 
						break;
					case 1: //south
						if (canAdd(r[i], dir, rr))
						{
							Loc nl = new Loc(x, y + 1);
							sts.add(nl);
						}
						if (!mapConstraint(r[i], dir) && !pieceConstraint(r[i], dir, rr))
						{
							sts.add(new Loc(x, y));
						}
						break;
					case 2: //west
						if (canAdd(r[i], dir, rr))
						{
							Loc nl = new Loc(x - 1,y);
							sts.add(nl);
						}
						if (!mapConstraint(r[i], dir) && !pieceConstraint(r[i], dir, rr))
						{
							sts.add(new Loc(x, y));
						} 
						break;
					case 3: //east
						if (canAdd(r[i], dir, rr))
						{
							Loc nl = new Loc(x + 1, y);
							sts.add(nl);
						}
						if (!mapConstraint(r[i], dir) && !pieceConstraint(r[i], dir, rr))
						{
							sts.add(new Loc(x, y));
						} 
						break;
				}
			}
						
			if (sts.isEmpty())
			{
				sts = rr.clone();
			}
			
			sts.id = dir; //add direction for tracking
			msts.add(sts);
		}		
		return msts;
	}

		
	/**
	 * is this a case where the movement causes new states?
	 * @param l
	 * @param dir
	 * @param rr
	 * @return
	 */
	private boolean canAdd(Loc l, int dir, State rr)
	{
		int x = l.x;
		int y = l.y;
		
		switch (dir)
		{
			case 0:
				return  (y - 1 >= 0  && !map.isCollision(x, y - 1) && !rr.contains(new Loc(x, y - 1)));
			case 1:
				return  (y + 1 < map.gridSize && !map.isCollision(x, y + 1) && !rr.contains(new Loc(x, y + 1)));
			case 2:
				 //return (x - 1 >= 0 && !map.isCollision(x + 1, y) && !map.isCollision(x - 1, y) && !rr.contains(new Loc(x -1, y)));
				 return (x - 1 >= 0 && !map.isCollision(x - 1, y) && !rr.contains(new Loc(x -1, y)));
			case 3:
				return  (x + 1 < map.gridSize && !map.isCollision(x + 1, y) && !rr.contains(new Loc(x + 1, y)));
			default:
				return false;
		}		
	}
	
	
	/**
	 * are there missing pieces around that make us reduce state further?
	 * @param l
	 * @param dir
	 * @param rr
	 * @return
	 */
	private boolean pieceConstraint(Loc l, int dir, State rr)
	{
		int x = l.x;
		int y = l.y;
		
		switch (dir)
		{
			case 0:
				return  (y - 1 >= 0 && !map.isCollision(x, y - 1) && !rr.contains(new Loc(x, y + 1)));
			case 1:
				return  (y + 1 < map.gridSize && !map.isCollision(x, y + 1) && !rr.contains(new Loc(x, y - 1)));
			case 2:
				 return (x - 1 >= 0 && !map.isCollision(x - 1, y) && !rr.contains(new Loc(x + 1, y)));
			case 3:
				return  (x + 1 < map.gridSize && !map.isCollision(x + 1, y) && !rr.contains(new Loc(x - 1, y)));
			default:
				return false;
		}		
	}
	
	/**
	 * are we in a place where the movement shrinks our belief state?
	 * @param l
	 * @param dir
	 * @return
	 */	
	private boolean mapConstraint(Loc l, int dir)
	{
		int x = l.x;
		int y = l.y;
		
		switch (dir)
		{   
			
			case 0:  //north	
				return 
					(y + 1 >= map.gridSize && !map.isCollision(x, y - 1)) ||
					(map.isCollision(x, y + 1) && !(y - 1 < 0));
			case 1: //south
				return (y - 1 < 0 && !map.isCollision(x, y + 1)) ||
					(map.isCollision(x, y - 1) && !(y + 1 >= map.gridSize));
			case 2: //west
				return (x + 1 >= map.gridSize && !map.isCollision(x - 1, y)) ||
					(map.isCollision(x + 1, y) && !(x - 1 < 0));
			case 3: //east
				return (x - 1 < 0 && !map.isCollision(x + 1, y)) ||
					(map.isCollision(x - 1, y) && !(x + 1 >= map.gridSize));
			default:
				return false;
		}
	}
	
	
	
		
	/**
	 * tests if we're at goal state
	 * @param r
	 * @return
	 */
	private boolean goalTest(State r, Loc g)
	{
		return (r.size() ==1 && r.contains(g));
	}
	

	/**
	 * why doesn't java let you create dicts like so: {(k,v), (k,v)}?
	 * @param dir
	 * @return
	 */
	private String getDir(int dir)
	{
		switch (dir)
		{
			case 0:
				return "North";
			case 1:
				return "South";
			case 2:
				return "West";
			case 3:
				return "East";
			case -1:
				return "Start";
			default:
				return "not a direction";
		}
	}
	
	



}

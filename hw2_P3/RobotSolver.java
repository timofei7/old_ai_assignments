package hw2_P3;

import hw2_P2.Loc;
import hw2_P2.MLoc;
import hw2_P2.MNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
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

	public static enum ActionSet { NORTH, SOUTH, EAST, WEST};
	
	public MMap map; //the map object
		
	public LinkedList<MLoc> mSolutions; // the final cooperative paths for display

		
	/**
	 * constructor, initializes the robots and some maps. 
	 */
	public RobotSolver(MMap m)
	{
		map = m;
		mSolutions = new LinkedList<MLoc>();
		
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
	 * 
	 * @param is
	 */
	private void bfs(Loc[] is)
	{
		
		MLoc s = new MLoc(is); //the first state is the complete set of moves
		s.id = -1;
		Loc f = map.finish; //the finish
		
		int counter = 0;
				
		LinkedList<MNode> frontier = new LinkedList<MNode>(); //states to look at
		Set<MLoc> explored = new HashSet<MLoc>();   //explored locations
		
		LinkedList<MLoc> solution = new LinkedList<MLoc>(); //the solutions
		
		frontier.addLast(new MNode(s, null, 0, 0)); //add the first robot state
		
		//run the search
		while (!frontier.isEmpty())
		{
			MNode current = frontier.removeFirst(); //chooses the first node in frontier
									
			// build plan if goal
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
			
			explored.add(current.state);
			
			//if (counter % 5000 == 0){System.out.print("\n");};
			//if (counter % 50 == 0) {System.out.print(".");}; 
						
			ArrayList<MLoc> possibles =  getMovesCorrectlyPlease(current.state);

			for (int i=0; i < possibles.size(); i++) // for all the children
			{
				MLoc possib = possibles.get(i); 
				//System.out.println("consider: " + possib + " dir: " + possib.id);
								
				// if we haven't explored it yet
				// add it to frontier with a distance
				if (! explored.contains(possib))
				{
					frontier.addLast(new MNode(possib, current, current.G + 1, 0));
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
	private Loc[] getInitBeliefs()
	{
		ArrayList<Loc> l = new ArrayList<Loc>();
		for (int i = 0; i < map.gridSize; i++)
		{
			for (int j = 0; j < map.gridSize; j++)
			{
				if (map.current_map[i].charAt(j) == '.')
				{
					l.add(new Loc(j,i));
				}
			}
		}
		Loc[] ll = new Loc[l.size()];
		l.toArray(ll);
		System.out.println("initial states: " + l.toString());
		return ll;
	}
	
	
	/**
	 * gets all the *possible* moves for a robot on the map
	 * @param r
	 * @return
	 */
	public ArrayList<MLoc> getMovesCorrectlyPlease(MLoc r)
	{
		ArrayList<MLoc> msts = new ArrayList<MLoc>();

		for (int dir = 0; dir < 4; dir++)
		{
			ArrayList<Loc> sts = new ArrayList<Loc>();

			for (int i = 0; i < r.locs.size(); i++)
			{
				int x = r.locs.get(i).x;
				int y = r.locs.get(i).y;
				
				switch (dir)
				{   
					// how do we trim states?
					case 0:  //north	
						if (y + 1 >= map.gridSize && !map.isCollision(x, y - 1))
						{
							System.out.println("culling1 " + r.locs.get(i) + " for "+dir);
						}
						else if (map.isCollision(x, y + 1) && !(y - 1 < 0))
						{
							System.out.println("culling2 " + r.locs.get(i) + " for "+dir);
						}
						else if (y - 1 >= 0 && !map.isCollision(x, y + 1) && !map.isCollision(x, y - 1) && !r.locs.contains(new Loc(x, y - 1)))
						{
							Loc nl = new Loc(x, y - 1);
							sts.add(nl);
							System.out.println("adding " + nl + " for " + dir);
						}
						else
						{
							sts.add(new Loc(x, y));
						}
						break;
					case 1: //south
						if (y - 1 < 0 && !map.isCollision(x, y + 1))
						{
							System.out.println("culling1 " + r.locs.get(i) + " for "+dir);
						}
						else if (map.isCollision(x, y - 1) && !(y + 1 >= map.gridSize))
						{
							System.out.println("culling2 " + r.locs.get(i) + " for "+dir);
						}
						else if (y + 1 < map.gridSize && !map.isCollision(x, y - 1) && !map.isCollision(x, y + 1) && !r.locs.contains(new Loc(x, y + 1)))
						{
							Loc nl = new Loc(x, y + 1);
							sts.add(nl);
							System.out.println("adding " + nl + " for " + dir);
						}
						else
						{
							sts.add(new Loc(x, y));
						}
						break;
					case 2: //west
						if (x + 1 >= map.gridSize && !map.isCollision(x - 1, y))
						{
							System.out.println("culling1 " + r.locs.get(i) + " for "+dir);
						}
						else if (map.isCollision(x + 1, r.locs.get(i).y) && !(x - 1 < 0))
						{
							System.out.println("culling2 " + r.locs.get(i) + " for "+dir);
						}
						else if (x - 1 >= 0 && !map.isCollision(x + 1, y) && !map.isCollision(x - 1, y) && !r.locs.contains(new Loc(x -1, y)))
						{
							Loc nl = new Loc(x - 1,y);
							sts.add(nl);
							System.out.println("adding " + nl + " for " + dir);
						}
						else
						{
							sts.add(new Loc(x, y));
						}
						break;
					case 3: //east
						if (x - 1 < 0 && !map.isCollision(x + 1, y))
						{
							System.out.println("culling1 " + r.locs.get(i) + " for "+dir);
						}
						else if (map.isCollision(x - 1, y) && !(x + 1 >= map.gridSize))
						{
							System.out.println("culling2 " + r.locs.get(i) + " for "+dir);
						}
						else if (x + 1 < map.gridSize && !map.isCollision(x - 1, y) && !map.isCollision(x + 1, y) && !r.locs.contains(new Loc(x + 1, y)))
						{
							Loc nl = new Loc(x + 1, y);
							sts.add(nl);
							System.out.println("adding " + nl + " for " + dir);
						}
						else
							
						{
							sts.add(new Loc(x, y));
						}
						break;
				}
			}
			
			MLoc t;
			
			if (sts.isEmpty())
			{
				t = r.clone();
			}
			else
			{
				t = new MLoc(sts.size()); 
				t.locs = sts;
			}
			
			t.id = dir; //add direction for tracking
			System.out.println("dir: " + dir + " r: "+r+" new: " + t);
			msts.add(t);
		}		
		return msts;
	}

	
		
		
	/**
	 * tests if we're at goal state
	 * @param r
	 * @return
	 */
	private boolean goalTest(MLoc r, Loc g)
	{
		return (r.locs.size() ==1 && g.equals(r.locs.get(0)));
	}
	

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

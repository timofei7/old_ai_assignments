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
		Map<MLoc,Double> explored = new HashMap<MLoc,Double>();   //explored locations
		
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
			
			explored.put(current.state, current.G);
			
			if (counter % 5000 == 0){System.out.print("\n");};
			if (counter % 50 == 0) {System.out.print(".");}; 
						
			ArrayList<MLoc> possibles = getMoves(current.state);

			for (int i=0; i < possibles.size(); i++) // for all the children
			{
				MLoc possib = possibles.get(i); 
				//System.out.println("consider: " + possib + " dir: " + possib.id);
								
				// if we haven't explored it yet
				// add it to frontier with a distance
				if (! explored.containsKey(possib))
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
					l.add(new Loc(i,j));
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
				{   // if we hit a collision in that direction don't change but add in original
					case 0:  //north
						nd = new Loc(r.locs[i].x, r.locs[i].y - 1);
						if (r.locs[i].y > 0  && !map.isCollision(nd))
						{
							sts.add(nd);
						}
						else
						{
							sts.add(r.locs[i]);
						}
						break;
					case 1: //south
						nd = new Loc(r.locs[i].x, r.locs[i].y + 1);
						if (r.locs[i].y < map.gridSize -1 && !map.isCollision(nd))
						{
							sts.add(nd);
						}
						else
						{
							sts.add(r.locs[i]);
						}
						break;
					case 2: //west
						nd = new Loc(r.locs[i].x - 1, r.locs[i].y);
						if (r.locs[i].x > 0 && !map.isCollision(nd))
						{
							sts.add(nd);
						}
						else
						{
							sts.add(r.locs[i]);
						}
						break;
					case 3: //east
						nd = new Loc(r.locs[i].x + 1, r.locs[i].y);
						if (r.locs[i].x < map.gridSize -1  && !map.isCollision(nd))
						{
							sts.add(nd);
						}
						else
						{
							sts.add(r.locs[i]);
						}
						break;
				}
			}
			MLoc t = new MLoc(sts.size()); 
			t.id = dir; //add direction for backtracking solution
			sts.toArray(t.locs);
			msts.add(t);
		}		
		return msts;
	}
		
		
	/**
	 * tests if we're at goal state
	 * for this case we test each of the belief states against
	 * the goal. So if (0,0) is our goal then the belief state should be
	 * {(0,0), (0,0), ..}
	 * @param r
	 * @return
	 */
	private boolean goalTest(MLoc r, Loc g)
	{
		boolean b = true;
		for (int i = 0; i < r.locs.length; i++)
		{
			b = b && r.locs[i].equals(g);
		}
		return b;
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

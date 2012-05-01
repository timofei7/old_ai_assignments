package hw2_P3;


/**
 * the maps for P4
 * @author tim
 *cs44 w10 hw2 p3
 */
public class MMap
{
	public static enum MapSet { MAP1, MAP2, MAP3, MAP4};
	
	public Loc finish; //the finishes
	
	public String[] current_map; //how we store maps
	
	public MapSet currentMap;	//which is the current one
		
	public int gridSize;  //the gridsize, only supports squares right now 
	
	
	/**
	 * initialize the map
	 * @param map
	 */
	public MMap(MapSet map)
	{
		
		currentMap = map; 
		
		switch (currentMap)
		{
			case MAP1:
				buildMap1();
				break;
			case MAP2:
				buildMap2();
				break;
			case MAP3:
				buildMap3();
				break;
			case MAP4:
				buildMap4();
				break;
			default:
				buildMap1();
				break;
		}
	}
	
	
	
	/**
	 * checks the coordinates at the given location for collisions with the given map
	 * @param r is a RobotState object
	 * @return if collision is found returns true
	 */
	public boolean isCollision(Loc r)
	{
		return isCollision(r.x, r.y);
	}
	
	
	/**
	 * checks the coordinates at the given location for collisions with the given map
	 * @param x is x coord
	 * @param y is y coord
	 * @return if collision is found returns true
	 */
	public boolean isCollision(int x, int y)
	{
		if (x >= gridSize || y >= gridSize || x < 0 || y < 0)
		{
			return false;
		}
		if (current_map[y].charAt(x) == '#')
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * build map 1 // 
	 */
	private void buildMap1()
	{
				
		finish = new Loc(1,0);


		gridSize = 4;
		
		current_map = new String[gridSize];
		current_map[0] = "....";
		current_map[1] = ".##.";
		current_map[2] = "..##";
		current_map[3] = "....";
	}
	
	/**
	 * build map  // nice fast test case
	 */
	private void buildMap2()
	{
		
		finish = new Loc(0,0);

		
		gridSize = 2;
		
		current_map = new String[gridSize];
		current_map[0] = "..";
		current_map[1] = ".#";

	}	
	
	/**
	 * build map 3  //this one is a bit more complex change it around some and it can sometimes
	 * fail to solve even though the solution seems obvious. 
	 */
	private void buildMap3()
	{
		
		finish = new Loc(3,0);

		
		gridSize = 5;
		
		current_map = new String[gridSize];
		current_map[0] = ".....";
		current_map[1] = ".###.";
		current_map[2] = "..#..";
		current_map[3] = ". #..";
		current_map[4] = "..#..";
	}
	
	/**
	 * build map 3  //this one is a bit more complex change it around some and it can sometimes
	 * fail to solve even though the solution seems obvious. 
	 */
	private void buildMap4()
	{
		
		finish = new Loc(3,0);

		
		gridSize = 5;
		
		current_map = new String[gridSize];
		current_map[0] = ".....";
		current_map[1] = ".###.";
		current_map[2] = "..#..";
		current_map[3] = ". #..";
		current_map[4] = ".....";
	}

}

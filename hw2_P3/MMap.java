package hw2_P3;

import hw2_P2.*;

public class MMap
{
	public static enum MapSet { MAP1, MAP2, MAP3};
	
	public Loc start;  //the robot starts indexed by robot
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
	 * build map 1
	 */
	private void buildMap1()
	{
				
		start = new Loc(0,6);
		finish = new Loc(1,0);


		gridSize = 4;
		
		current_map = new String[gridSize];
		current_map[0] = "....";
		current_map[1] = ".##.";
		current_map[2] = "..##";
		current_map[3] = "....";
	}
	
	/**
	 * build map 2
	 */
	private void buildMap2()
	{
		
		start = new Loc(0,0);
		finish = new Loc(0,0);

		
		gridSize = 2;
		
		current_map = new String[gridSize];
		current_map[0] = "..";
		current_map[1] = ".#";

	}	
	
	/**
	 * build map 2
	 */
	private void buildMap3()
	{
		
		start = new Loc(0,6);
		finish = new Loc(3,2);

		
		gridSize = 5;
		
		current_map = new String[gridSize];
		current_map[0] = ".....";
		current_map[1] = ".##..";
		current_map[2] = ".###.";
		current_map[3] = "..##.";
		current_map[4] = ".....";
	}
}

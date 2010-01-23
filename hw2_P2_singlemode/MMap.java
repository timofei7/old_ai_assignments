package hw2_P2_singlemode;

import java.util.ArrayList;


public class MMap
{
	public static enum MapSet { MAP1, MAP2, MAP3, MAP4, MAP5 };
	
	public RobotState[] starts;
	public RobotState[] finishes;
	
	public String[] current_map;
	
	public MapSet currentMap;	
	
	
	public MMap(MapSet map)
	{
		
		currentMap = map;  // this one thing setting changes EVERYTHING
		
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
			case MAP5:
				buildMap5();
				break;
			default:
				buildMap1();
				break;
		}
	}
	
	public ArrayList<RobotState> getRobotStarts()
	{
		return null;
	}
	
	
	public ArrayList<RobotState> getRobotFinishes()
	{
		return null;
	}

	
	
	/**
	 * checks the coordinates at the given location for collisions with the given map
	 * @param r is a RobotState object
	 * @return if collision is found returns true
	 */
	public boolean isCollision(RobotState r)
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
	
	private void buildMap1()
	{
		starts = new RobotState[3];
		finishes = new RobotState[3];
		
		starts[0] = new RobotState(0,6);
		starts[1] = new RobotState(1,0);
		starts[2] = new RobotState(4,1);
		finishes[0] = new RobotState(6,5);
		finishes[1] = new RobotState(6,4);
		finishes[2] = new RobotState(6,6);

		
		current_map = new String[7];
		current_map[0] = ".......";
		current_map[1] = ".##....";
		current_map[2] = "..##...";
		current_map[3] = "....#..";
		current_map[4] = "..##...";
		current_map[5] = "..#....";
		current_map[6] = "....##.";
	}
	
	private void buildMap2()
	{
		starts = new RobotState[1];
		finishes = new RobotState[1];
		
		starts[0] = new RobotState(0,6);
		finishes[0] = new RobotState(3,2);
		
		current_map = new String[7];
		current_map[0] = ".......";
		current_map[1] = "..###..";
		current_map[2] = "..#.#..";
		current_map[3] = "..#.#..";
		current_map[4] = "..#.#..";
		current_map[5] = "..#.#..";
		current_map[6] = "..#....";
	}
	
	private void buildMap3()
	{
		current_map = new String[7];
		current_map[0]  = "...........";
		current_map[1]  = "...........";
		current_map[2]  = "...........";
		current_map[3]  = "...........";
		current_map[4]  = "...........";
		current_map[5]  = "...........";
		current_map[6]  = "...........";
		current_map[7]  = "...........";
		current_map[8]  = "...........";
		current_map[9]  = "...........";
		current_map[10] = "...........";
		current_map[11] = "...........";
	}
	
	private void buildMap4()
	{
		current_map = new String[7];
	}
	
	private void buildMap5()
	{
		current_map = new String[7];
	}
	
}

package hw2_P2;

import java.util.ArrayList;


public class Maps
{
	public static enum Map { MAP1, MAP2, MAP3, MAP4, MAP5 };
	
	//TODO maybe store these some other way?
	public final RobotState[] map1A = {new RobotState(0,6),new RobotState(6,5)};
	//public final RobotState map1AFinish = new RobotState(6,5);
	public final RobotState[] map1B = {new RobotState(1,0),new RobotState(6,4)};
	//public final RobotState map1BStart = new RobotState(1,0);
	//public final RobotState map1BFinish = new RobotState(6,4);
	public final RobotState[] map1C = {new RobotState(4,1),new RobotState(6,6)};
	//public final RobotState map1CStart = new RobotState(4,1); 
	//public final RobotState map1CFinish = new RobotState(6,6);

	
	public String[] map1;
	public String[] map2;
	public String[] map3;
	public String[] map4;
	public String[] map5;
	
	public Map currentMap;
	
	public Maps()
	{
		currentMap = Map.MAP1;  // this one thing setting changes EVERYTHING
		
		map1 = new String[7];
		map2 = new String[7];
		map3 = new String[11];
		map4 = new String[7];
		map5 = new String[7];
		
		map1[0] = ".......";
		map1[1] = ".##....";
		map1[2] = "..##...";
		map1[3] = "....#..";
		map1[4] = "..##...";
		map1[5] = "..#....";
		map1[6] = "....##.";
		
		map2[0] = ".......";
		map2[1] = "..###..";
		map2[2] = "..#.#..";
		map2[3] = "..#.#..";
		map2[4] = "..#.#..";
		map2[5] = "..#.#..";
		map2[6] = "..#....";
		
		map3[0]  = "...........";
		map3[1]  = "...........";
		map3[2]  = "...........";
		map3[3]  = "...........";
		map3[4]  = "...........";
		map3[5]  = "...........";
		map3[6]  = "...........";
		map3[7]  = "...........";
		map3[8]  = "...........";
		map3[9]  = "...........";
		map3[10] = "...........";
		map3[11] = "...........";
		
		
	}
	
	public ArrayList<RobotState> getRobots()
	{
		switch (currentMap)
		{
			case MAP1:
				break;
			case MAP2:
				break;
			case MAP3:
				break;
			case MAP4:
				break;
			case MAP5:
				break;
			default:
				break;
		}
		return null;
	}

	
	/**
	 * checks the coordinates at the given location for collisions with the given map
	 * @param x
	 * @param y
	 * @param m the map type
	 * @return if collision is found returns true
	 */
	public boolean isCollision(RobotState r)
	{
		
		switch (currentMap)
		{
			case MAP1:
				return isCollision(r.x, r.y, map1);
			case MAP2:
				return isCollision(r.x, r.y, map2);
			case MAP3:
				return isCollision(r.x, r.y, map3);
			case MAP4:
				return isCollision(r.x, r.y, map4);
			case MAP5:
				return isCollision(r.x, r.y, map5);
			default: return false;
		}
	}
	
	
	/**
	 * checking for collisions
	 */
	public boolean isCollision(int x, int y)
	{
		return isCollision(new RobotState(x, y));
	}
	
	
	/**
	 * checking for collisions
	 */
	public boolean isCollision(int x, int y, String[] m)
	{
		if (m[y].charAt(x) == '#')
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	
}

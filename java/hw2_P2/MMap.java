package hw2_P2;

/**
 * hardcoded maps object
 * see below javadocs for description of maps
 * @author tim
 *
 */
public class MMap
{
	public static enum MapSet { MAP1, MAP2, MAP3, MAP4, MAP5, MAP6};
	
	public Loc[] starts;  //the robot starts indexed by robot
	public Loc[] finishes; //the finishes
	
	public String[] current_map; //how we store maps
	
	public MapSet currentMap;	//which is the current one
	
	public int numRobots;  //the number of robots on the map
	
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
			case MAP5:
				buildMap5();
				break;
			case MAP6:
				buildMap6();
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
	 * the example map from the homework assignment
	 */
	private void buildMap1()
	{
		numRobots = 3;
		
		starts = new Loc[numRobots];
		finishes = new Loc[numRobots];
		
		starts[0] = new Loc(0,6);
		starts[1] = new Loc(1,0);
		starts[2] = new Loc(4,1);
		finishes[0] = new Loc(6,5);
		finishes[1] = new Loc(6,4);
		finishes[2] = new Loc(6,6);

		gridSize = 7;
		
		current_map = new String[gridSize];
		current_map[0] = ".......";
		current_map[1] = ".##....";
		current_map[2] = "..##...";
		current_map[3] = "....#..";
		current_map[4] = "..##...";
		current_map[5] = "..#....";
		current_map[6] = "....##.";
	}
	
	/**
	 * build map 2
	 * map where 2 robots need to swap places but have lots of time to do so
	 */
	private void buildMap2()
	{
		numRobots = 2;
		
		starts = new Loc[numRobots];
		finishes = new Loc[numRobots];
		
		starts[0] = new Loc(0,6);
		starts[1] = new Loc(0,5);
		finishes[0] = new Loc(3,2);
		finishes[1] = new Loc(3,3);
		
		gridSize = 7;
		
		current_map = new String[gridSize];
		current_map[0] = ".......";
		current_map[1] = "..###..";
		current_map[2] = "..#.#..";
		current_map[3] = "..#.#..";
		current_map[4] = "..#.#..";
		current_map[5] = "..#.#..";
		current_map[6] = "..#....";
	}
	
	/**
	 * build map 3
	 * map where robots need to swap places, not so much opportunity
	 * also larger map
	 */
	private void buildMap3()
	{
		numRobots = 2;
		
		starts = new Loc[numRobots];
		finishes = new Loc[numRobots];
		
		starts[0] = new Loc(0,3);
		starts[1] = new Loc(1,3);
		finishes[0] = new Loc(18,3);
		finishes[1] = new Loc(16,3);

		gridSize = 20;
		
		current_map = new String[gridSize];
		current_map[0]  = "....................";
		current_map[1]  = "....................";
		current_map[2]  = "####################";
		current_map[3]  = "....................";
		current_map[4]  = "######.#########.###";
		current_map[5]  = "....................";
		current_map[6]  = "....................";
		current_map[7]  = "....................";
		current_map[8]  = "....................";
		current_map[9]  = "....................";
		current_map[10] = "....................";
		current_map[11] = "....................";
		current_map[12] = "....................";
		current_map[13] = "....................";
		current_map[14] = "....................";
		current_map[15] = "....................";
		current_map[16] = "....................";
		current_map[17] = "....................";
		current_map[18] = "....................";
		current_map[19] = "....................";

		
	}
	
	/**
	 * build map 4
	 * one robot needs to get out of the way of the other
	 */
	private void buildMap4()
	{
		numRobots = 2;
		
		starts = new Loc[numRobots];
		finishes = new Loc[numRobots];
		
		starts[0] = new Loc(4,4);
		finishes[0] = new Loc(1,0);
		
		starts[1] = new Loc(3,4);
		finishes[1] = new Loc(1,1);
		
		gridSize = 5;
		
		current_map = new String[gridSize];
		current_map[0] = "#.###";
		current_map[1] = "#.###";
		current_map[2] = "#.###";
		current_map[3] = "#.###";
		current_map[4] = ".....";
	}
	
		
	/**
	 * build map 5
	 * biggest map i can handle. it won't handle with more than 2 robots...
	 */
	private void buildMap5()
	{
		numRobots = 2;  // more than 2 robots doesn't work in this big maze haha
		
		starts = new Loc[numRobots];
		finishes = new Loc[numRobots];
		
		starts[0] = new Loc(39,39);  //has to navigate past 1
		finishes[0] = new Loc(39,0);
		
		starts[1] = new Loc(38,39);
		finishes[1] = new Loc(0,2); //blocks 0
		
		
		gridSize=41;
		
		current_map = new String[gridSize];
		
		current_map[0] =  ".....................##...##.............";
		current_map[1] =  "...............#####....#....######......";
		current_map[2] =  ".########################################";
		current_map[3] =  ".........................................";
		current_map[4] =  "..############################...........";
		current_map[5] =  "..............#..........................";
		current_map[6] =  "..............#..........................";
		current_map[7] =  "..............#..#.......................";
		current_map[8] =  "..............#.....#####................";
		current_map[9] =  "..............#..#..#.#.#................";
		current_map[10] = "..............#..#..#.#.#................";
		current_map[11] = "..............#..#..#.#.#................";
		current_map[12] = "..............#..........................";
		current_map[13] = "..............#.......###................";
		current_map[14] = "..............#.......#..................";
		current_map[15] = "..............#....#..###................";
		current_map[16] = "..............#....#....#................";
		current_map[17] = "..............#....#..###................";
		current_map[18] = "..............#..........................";
		current_map[19] = "..............#..........................";
		current_map[20] = ".........................................";
		current_map[21] = "......##.................................";
		current_map[22] = ".....#..#..........###...................";
		current_map[23] = "....#........#...#.#.....................";
		current_map[24] = "...########..#.#.#.###.#################.";
		current_map[25] = ".. #......#..#.#.#.#.....................";
		current_map[26] = "...#......#..#####.###...................";
		current_map[27] = "..............................#...#......";
		current_map[28] = "...#####.............######...#...#......";
		current_map[29] = "...#......####.#####.#........#...#......";
		current_map[30] = "...#####..#..#.#.#.#.####.....#...#......";
		current_map[31] = ".......#..#..#.#.#.#.#........#...#......";
		current_map[32] = "...#####..####.#.#.#.######...#...#......";
		current_map[33] = "..............................#...#......";
		current_map[34] = "..............................#...#......";
		current_map[35] = ".........................................";
		current_map[36] = "..............................#...#......";
		current_map[37] = ".........................................";
		current_map[38] = ".########################################";
		current_map[39] = ".........................................";
		current_map[40] = "#########################################";

	}
	
	/**
	 * build map 6
	 * attempt at 8-puzzle
	 * I found an easy one online.
	 * the solver pauses a lot for this one
	 * and I would change the heuristic to make it better
	 */
	private void buildMap6()
	{
		numRobots = 8;
		
		starts = new Loc[numRobots];
		finishes = new Loc[numRobots];
		
		starts[0] = new Loc(0,0);
		finishes[0] = new Loc(0,0);
		
		starts[1] = new Loc(2,1);
		finishes[1] = new Loc(1,0);
		
		starts[2] = new Loc(1,0);
		finishes[2] = new Loc(2,0);
		
		starts[3] = new Loc(2,0);
		finishes[3] = new Loc(2,1);
		
		starts[4] = new Loc(2,2);
		finishes[4] = new Loc(2,2);
		
		starts[5] = new Loc(1,1);
		finishes[5] = new Loc(1,2);
		
		starts[6] = new Loc(0,2);
		finishes[6] = new Loc(0,2);
		
		starts[7] = new Loc(0,1);
		finishes[7] = new Loc(0,1);
		
		gridSize = 3;
		
		current_map = new String[gridSize];
		current_map[0] = "...";
		current_map[1] = "...";
		current_map[2] = "...";

	}
	
}

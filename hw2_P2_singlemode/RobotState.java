package hw2_P2_singlemode;


/**
 *  is the robot location
 */
public class RobotState
{
	public int x;
	public int y;
	
	/**
	 * make me a robot state plz
	 * @param x
	 * @param y
	 */
	public RobotState(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * a robot clone constructor
	 * @param r
	 */
	public RobotState(RobotState r)
	{
		this.x = r.x;
		this.y = r.y;
	}
	
	public boolean equals(RobotState r)
	{
		return r.x == this.x && r.y == this.y;
	}
	
	@Override
	public boolean equals(Object o)
	{
		RobotState r = (RobotState) o;
		return equals(r);
	}
	
	@Override
	public int hashCode()
	{
		return (x * 10) + y;
	}
	
	
	public String toString()
	{
		return "("+x+","+y+")";
	}
}

package hw2_P2;


/**
 *  is the robot location
 */
public class Loc
{
	public int x;
	public int y;
	
	/**
	 * make me a robot state plz
	 * @param x
	 * @param y
	 */
	public Loc(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * a robot clone constructor
	 * @param r
	 */
	public Loc(Loc r)
	{
		this.x = r.x;
		this.y = r.y;
	}
	
	public boolean equals(Loc r)
	{
		return r.x == this.x && r.y == this.y;
	}
	
	@Override
	public boolean equals(Object o)
	{
		Loc r = (Loc) o;
		return equals(r);
	}
	
	@Override
	public int hashCode()
	{
		//return (x * 10) + y;
		return Integer.parseInt(Integer.toString(x) + Integer.toString(y));
	}
	
	
	public String toString()
	{
		return "("+x+","+y+")";
	}
}

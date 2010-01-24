package hw2_P2;


/**
 *  is the robot location with time 
 */
public class TimeLoc
{
	public int x;
	public int y;
	public int t;
	
	/**
	 * make me a robot state plz
	 * @param x
	 * @param y
	 * @param t
	 */
	public TimeLoc(int x, int y, int t)
	{
		this.x = x;
		this.y = y;
		this.t = t;
	}
	
	/**
	 * a robot clone constructor
	 * @param r
	 */
	public TimeLoc(TimeLoc r)
	{
		this.x = r.x;
		this.y = r.y;
		this.t = r.t;
	}
	
	/**
	 * different constructor
	 * @param r
	 * @param t
	 */
	public TimeLoc(Loc r, int t)
	{
		this.x = r.x;
		this.y = r.y;
		this.t = t;
	}
	
	
	public boolean equals(TimeLoc r)
	{
		return r.x == this.x && r.y == this.y && r.t == this.t;
	}
	
	@Override
	public boolean equals(Object o)
	{
		TimeLoc r = (TimeLoc) o;
		return equals(r);
	}
	
	/**
	 * the hashcode is simply an integer of all 3 state items cat'd together
	 */
	@Override
	public int hashCode()
	{
		return Integer.parseInt(Integer.toString(x) + Integer.toString(y) + Integer.toString(t));
	}
	
	
	public String toString()
	{
		return "("+x+","+y+"," + t +")";
	}
}

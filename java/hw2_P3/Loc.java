package hw2_P3;

/**
 * very basic class to hold a tuple
 * @author tim
 *
 */
public class Loc
{
	public int x;
	public int y;
	
	public Loc(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Loc(Loc r)
	{
		this.x = r.x;
		this.y = r.y;
	}

	
	@Override
	public Loc clone()
	{
		Loc l = new Loc(x, y);
		return l;
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
	
	/**
	 * the hashcode is simply an int of the 2 states cat'd together
	 */
	@Override
	public int hashCode()
	{
		return Integer.parseInt(Integer.toString(x) + Integer.toString(y));
	}
	
	
	public String toString()
	{
		return "("+x+","+y+")";
	}
}

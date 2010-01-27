package hw2_P3;


public class State
{
	public int x;
	public int y;
	
	public State(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public State(State r)
	{
		this.x = r.x;
		this.y = r.y;
	}

	
	@Override
	public State clone()
	{
		State l = new State(x, y);
		return l;
	}
	
	public boolean equals(State r)
	{
		return r.x == this.x && r.y == this.y;
	}
	
	@Override
	public boolean equals(Object o)
	{
		State r = (State) o;
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

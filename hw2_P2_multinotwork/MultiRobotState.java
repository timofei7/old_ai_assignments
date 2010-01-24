package hw2_P2_multinotwork;

public class MultiRobotState
{
	public RobotState[] rs;
	public int size;
	
	public MultiRobotState(int c)
	{
		rs = new RobotState[c];
		size = c;
	}
	
	
	public MultiRobotState(RobotState r, int i, MultiRobotState mrs)
	{
		rs = mrs.rs;
		rs[i] = r;
	}
	
	public MultiRobotState(RobotState[] r)
	{
		rs = r;
		size = r.length;
	}
	
	
	public boolean equals(MultiRobotState r)
	{
		boolean t = true;
		
		for (int i =0; i < size; i++)
		{
			t = t && r.rs[i].equals(rs[i]);
		}
		return t;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		MultiRobotState r = (MultiRobotState) o;
		return equals(r);
	}
	
	
	@Override
	public int hashCode()
	{
		String s = "0";
		for (int i = 0; i< size; i++)
		{
			s = s + Integer.toString(rs[i].hashCode());
		}
		return Integer.parseInt(s);
	}
	
	
	public String toString()
	{
		String s = "";
		for (int i = 0; i< size; i++)
		{
			s = s + rs[i].toString() + ",";
		}
		return s;
	}
}

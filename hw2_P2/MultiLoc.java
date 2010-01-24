package hw2_P2;

import java.util.ArrayList;

public class MultiLoc
{
	public ArrayList<Loc> rs;
	
	public MultiLoc(int c)
	{
		rs = new ArrayList<Loc>();
	}
	
	public MultiLoc(Loc r, int i, MultiLoc mrs)
	{
		rs = mrs.deepClone();
		rs.set(i, r);
	}
	
	public MultiLoc(Loc[] r)
	{
		rs = new ArrayList<Loc>();
		
		for (int i = 0; i < r.length; i ++)
		{
			rs.add(r[i]);
		}
	}	
	
	public ArrayList<Loc> deepClone()
	{
		ArrayList<Loc> l = new ArrayList<Loc>();
		for (int i = 0; i < this.rs.size(); i++)
		{
			l.add(new Loc(this.rs.get(i)));
		}
		return l;
	}
	
	public boolean equals(MultiLoc r)
	{
		boolean t = true;
		
		for (int i = 0; i < rs.size(); i++)
		{
			t = t && this.rs.get(i).equals(r.rs.get(i));
		}
		return t;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		MultiLoc r = (MultiLoc) o;
		return equals(r);
	}
	
	
	@Override
	public int hashCode()
	{
		String s = "0";
		for (int i = 0; i< rs.size(); i++)
		{
			s = s + Integer.toString(rs.get(i).hashCode());
		}
		return Integer.parseInt(s);
	}
	
	
	public String toString()
	{
		String s = "";
		for (int i = 0; i< rs.size(); i++)
		{
			s = s + rs.get(i).toString() + ",";
		}
		return s;
	}
}

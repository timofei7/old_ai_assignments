package hw2_P2;

public class MultiNode implements Comparable<MultiNode> 
{
	MultiLoc state;
	MultiNode parent;
	double distance;

	MultiNode(MultiLoc current, MultiNode parent, double distance)
	{
		this.state = current;
		this.parent = parent;
		this.distance = distance;
	}
	
	@Override
	public int compareTo(MultiNode o)
	{
		return (int) Math.floor(distance - o.distance);
	}
	
	public String toString()
	{
		String s = state.toString();
		try
		{
			s = "State: " + state.toString() + ", parent: " + parent.state.toString() + ", distance: " + distance;
		}
		catch (Exception e){}

		return s;
	}
}
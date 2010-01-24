package hw2_P2;


/**
 * the node object for storing a state its parent and its distance
 * this one deals with time state
 * TODO:  make a generic class of Node to handle both
 */
public class TimeNode implements Comparable<TimeNode> 
{
	TimeLoc state;
	TimeNode parent;
	double distance;

	TimeNode(TimeLoc current, TimeNode parent, double distance)
	{
		this.state = current;
		this.parent = parent;
		this.distance = distance;
	}
	
	@Override
	public int compareTo(TimeNode o)
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
//	
//	public boolean equals(Node n)
//	{ //TODO check this, we're basically making nodes equal by state but in our case this is desirable
//		return this.state.equals(n.state); // && this.parent.state.equals(n.parent.state) && this.distance == n.distance;
//	}
//	
//	@Override
//	public boolean equals(Object o)
//	{
//		Node n = (Node) o;
//		return this.equals(n);
//	}
//
//
//	@Override
//	public int hashCode()
//	{
//		return this.state.hashCode(); //just hash by state we don't care about distance or parent in this case
//	}
	 
}

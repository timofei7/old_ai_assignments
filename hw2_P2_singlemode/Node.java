package hw2_P2_singlemode;


/**
 * the node object for storing a state its parent and its distance
 */
public class Node implements Comparable<Node> 
{
	RobotState state;
	Node parent;
	double distance;

	Node(RobotState current, Node parent, double distance)
	{
		this.state = current;
		this.parent = parent;
		this.distance = distance;
	}
	
	@Override
	public int compareTo(Node o)
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

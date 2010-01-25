package hw2_P2;


/**
 * the node object for storing a state its parent and its distance
 */
public class Node implements Comparable<Node> 
{
	Loc state;
	Node parent;
	double distance;
	
	int rid; //not generally used

	/**
	 * a node typically holds state, that states parent, and distance
	 * @param current
	 * @param parent
	 * @param distance
	 */
	Node(Loc current, Node parent, double distance)
	{
		this.state = current;
		this.parent = parent;
		this.distance = distance;
	}
	
	/**
	 * special case for sorting robots by distance
	 * not for public consumption
	 * @param distance
	 * @param rid
	 */
	Node(double distance, int rid)
	{
		this.state = null;
		this.parent = null;
		this.distance = distance;
		this.rid = rid;
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

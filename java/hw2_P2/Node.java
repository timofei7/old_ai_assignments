package hw2_P2;


/**
 * the node object for storing a state its parent and its distance
 * this is for single locations
 */
public class Node implements Comparable<Node> 
{
	public Loc state;
	public Node parent;
	public double G; //the exact cost in steps to get here
	public double H; //the heuristic cost
	
	int rid; //not generally used

	/**
	 * a node typically holds state, that states parent, and distance
	 * @param current
	 * @param parent
	 * @param distance
	 */
	public Node(Loc current, Node parent, double G, double H)
	{
		this.state = current;
		this.parent = parent;
		this.G = G;
		this.H = H;
		this.rid = 0;

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
		this.G = distance;
		this.H = 0;
		this.rid = rid;
	}
	
	/**
	 * g + h the estimated distance
	 * @return
	 */
	public double distance()
	{
		return G + H;
	}
	
	
	@Override
	public int compareTo(Node o)
	{
		return (int) Math.floor(distance() - o.distance());
	}
	
	public String toString()
	{
		String s = state.toString();
		try
		{
			s = "State: " + state.toString() + ", parent: " + parent.state.toString() + ", distance: " + distance();
		}
		catch (Exception e){}

		return s;
	}
}

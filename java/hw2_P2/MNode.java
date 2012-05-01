package hw2_P2;


/**
 * the node object for storing a state its parent and its distance
 */
public class MNode implements Comparable<MNode> 
{
	public MLoc state;
	public MNode parent;
	public double G; //the exact cost in steps to get here
	public double H; //the heuristic cost
		
	/**
	 * a node typically holds state, that states parent, and distance
	 * @param current
	 * @param parent
	 * @param distance
	 */
	public MNode(MLoc current, MNode parent, double G, double H)
	{
		this.state = current;
		this.parent = parent;
		this.G = G;
		this.H = H;
	}
	
	
	/**
	 * g + h the estimated distance
	 * @return
	 */
	public double distance()
	{
		return G + H;
	}
	
	
	/**
	 * compare nodes based on F
	 * break ties based on H only
	 */
	@Override
	public int compareTo(MNode o)
	{
		return (int) Math.floor(distance() - o.distance());
	}
	
	public String toString()
	{
		String s = state.toString();
		try
		{
			s = "State: " + state.toString() + ", parent: " + parent.state.toString() + ", H: " + H + ", G: " + G;
		}
		catch (Exception e){}

		return s;
	}
}

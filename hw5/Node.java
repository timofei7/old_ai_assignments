package hw5;


/**
 * viterbi seach node
 * @author tim
 *
 */
public class Node
{
	public String state; //this nodes state
	public Node parent; //this nodes parent
	public Double state_probability;  //the probability of this state
	public Double path_probability;   //the probability of the path
	
	
	/**
	 * constructs a node
	 * @param s string identifying this node state
	 * @param p this node's parent Node
	 * @param sp this state's probability
	 * @param pp the most probably path probability to here
	 */
	public Node(String s, Node p, Double sp, Double pp)
	{
		this.state= s;
		this.parent = p;
		this.state_probability = sp;
		this.path_probability = pp;
	}
	
	public String toString()
	{
		String path = state; //reconstruct the path
		Node a = parent;
		while (a != null)
		{
			path = a.state + ","+path;
			a = a.parent;
		}
		
		return "state_probability=" + state_probability+ ", path_probability="+path_probability + ", path="+path;
	}
	
	/**
	 * updates this node only if the path probability is greater
	 * @param s
	 * @param p
	 * @param sp
	 * @param pp
	 */
	public void updateMax(String s, Node p, Double sp, Double pp)
	{
		if (pp > this.path_probability)
		{
			this.state= s;
			this.parent = p;
			this.state_probability = sp;
			this.path_probability = pp;
		}
	}
}

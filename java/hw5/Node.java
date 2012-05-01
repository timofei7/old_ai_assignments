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
	public Double probability;  //the probability of this state
	public boolean log;
	
	
	/**
	 * constructs a node
	 * @param s string identifying this node state
	 * @param p this node's parent Node
	 * @param sp this state's probability
	 * @param pp the most probably path probability to here
	 */
	public Node(String s, Node p, Double pr)
	{
		this.state= s;
		this.parent = p;
		this.probability = pr;
		this.log = false;
	}
	
	public Node(String s, Node p, Double pr, boolean log)
	{
		this.state= s;
		this.parent = p;
		this.probability = pr;
		this.log = log;
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
		
		if (log)
			return "probability=" + Math.exp(probability)+ ", path="+path;
		else
			return "probability=" + probability+ ", path="+path;
	}
	
	
	/**
	 * updates this node only if the path probability is greater
	 * @param s
	 * @param p
	 * @param sp
	 * @param pp
	 */
	public void updateMax(String s, Node p, Double pr)
	{
		//System.out.println("compare: " + this.state + "=" +s +  " me: " + this.probability + " with: " + pr);
		if (pr >= this.probability)
		{
			this.state= s;
			this.parent = p;
			this.probability = pr;
		}
	}
}

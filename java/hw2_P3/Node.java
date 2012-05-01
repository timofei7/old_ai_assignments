package hw2_P3;

/**
 * very basic node
 * @author tim
 *
 */
public class Node
{
	public State state;
	public Node parent;
	public double distance;
	
	Node(State state, Node parent, double distance)
	{
		this.state = state;
		this.parent = parent;
		this.distance = distance;
	}

}

package hw2_P2;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * For speed sake we're keeping a hashset along with our priority queue.
 * Thus membership testing and pulling specific objects is fast
 * But we are using twice the memory... 
 * @author tim tregubov
 */
public class PriorityHashQueue extends PriorityQueue<Node>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3830101596032534204L;

	private HashMap<Loc, Node> hash;
	
	public PriorityHashQueue()
	{
		super();
		hash = new HashMap<Loc, Node>();
	}
	
	@Override
	public boolean add(Node o)
	{
		hash.put(o.state, o);
		return super.add(o);
	}
	
	@Override
	public boolean contains(Object o)
	{
		Node n = (Node) o;
		return hash.containsKey(n.state);  //now we're fast!
	}
	
	@Override
	public Node poll()
	{
		Node o = super.poll();
		hash.remove(o.state);
		return o;
	}
	
	@Override
	public void clear()
	{
		hash.clear();
		super.clear();
	}
	
	@Override
	public boolean remove(Object o)
	{
		Node n = (Node) o;
		hash.remove(n.state);
		return super.remove(o);
	}
	
	
	/**
	 * To update the priority of an object this will replace the old object with a new one.
	 * @param o
	 * @return
	 */
	public boolean update(Node o, Node n)
	{
		return remove(o) && add(n);
	}
	
	public Node get(Loc s)
	{
		return hash.get(s);
	}
	
	
}

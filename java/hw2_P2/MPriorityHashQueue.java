package hw2_P2;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * For speed sake we're keeping a hashset along with our priority queue.
 * Thus membership testing and pulling specific objects is fast
 * But we are using twice the memory... 
 * @author tim tregubov
 */
public class MPriorityHashQueue extends PriorityQueue<MNode>
{

	private static final long serialVersionUID = 3830101596032534204L;

	private HashMap<MLoc, MNode> hash;
	
	public MPriorityHashQueue()
	{
		super();
		hash = new HashMap<MLoc, MNode>();
	}
	
	@Override
	public boolean add(MNode o)
	{
		hash.put(o.state, o);
		return super.add(o);
	}
	
	@Override
	public boolean contains(Object o)
	{
		MNode n = (MNode) o;
		return hash.containsKey(n.state);  //now we're fast!
	}
	
	@Override
	public MNode poll()
	{
		MNode o = super.poll();
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
		MNode n = (MNode) o;
		hash.remove(n.state);
		return super.remove(o);
	}
	
	
	/**
	 * To update the priority of an object this will replace the old object with a new one.
	 * @param o
	 * @return
	 */
	public boolean update(MNode o, MNode n)
	{
		return remove(o) && add(n);
	}
	
	public MNode get(MLoc s)
	{
		return hash.get(s);
	}
	
	
}

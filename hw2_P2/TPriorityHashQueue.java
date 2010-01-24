package hw2_P2;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * For speed sake we're keeping a hashset along with our priority queue.
 * Thus membership testing and pulling specific objects is fast
 * But we are using twice the memory... 
 * @author tim tregubov
 */
public class TPriorityHashQueue extends PriorityQueue<TimeNode>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3830101596032534204L;

	private HashMap<TimeLoc, TimeNode> hash;
	
	public TPriorityHashQueue()
	{
		super();
		hash = new HashMap<TimeLoc, TimeNode>();
	}
	
	@Override
	public boolean add(TimeNode o)
	{
		hash.put(o.state, o);
		return super.add(o);
	}
	
	@Override
	public boolean contains(Object o)
	{
		TimeNode n = (TimeNode) o;
		return hash.containsKey(n.state);  //now we're fast!
	}
	
	@Override
	public TimeNode poll()
	{
		TimeNode o = super.poll();
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
		TimeNode n = (TimeNode) o;
		hash.remove(n.state);
		return super.remove(o);
	}
	
	
	/**
	 * To update the priority of an object this will replace the old object with a new one.
	 * @param o
	 * @return
	 */
	public boolean update(TimeNode o, TimeNode n)
	{
		return remove(o) && add(n);
	}
	
	public TimeNode get(TimeLoc s)
	{
		return hash.get(s);
	}
	
	
}

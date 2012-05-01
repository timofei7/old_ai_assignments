package hw2_P3;

import java.util.HashSet;

/**
 * holds state
 * @author tim
 *cs44 w10 hw2 p3
 */
public class State extends HashSet<Loc> implements Cloneable
{
	private static final long serialVersionUID = -5495262245602711531L;
	
	public int id;	
	
	
	/**
	 * yes we can deep clone
	 */
    @Override
    public State clone()
    {
    	State l = new State();
    	
    	Loc[] t = new Loc[super.size()];
    	super.toArray(t);
    	
    	for (int i=0; i < super.size(); i++)
    	{
    		l.add(new Loc(t[i].x, t[i].y));
    	}
    	return l;
    }


    
    
    public boolean equals(State s)
    {
    	return this.containsAll(s) && s.containsAll(this);
    }
    
    
	@Override
	public boolean equals(Object o)
	{
		State s = (State) o;
		return equals(s);
	}

}

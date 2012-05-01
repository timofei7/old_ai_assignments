package hw2_P2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 *  is the robot location
 */
public class MLoc implements Cloneable
{
	
	public List<Loc> locs;
	
	public int id;
			
    public MLoc(int c)
    {
        locs = new ArrayList<Loc>();
    }
    
    
    /**
     * just make MLoc of a Loc[]
     * THIS is *not* a clone however! watch out!
     * @param l
     */
    public MLoc(Loc[] l)
    {
    	locs = Arrays.asList(l);
    }

    
    @Override
    public MLoc clone()
    {
    	ArrayList<Loc> l = new ArrayList<Loc>();
    	for (int i=0; i < locs.size(); i++)
    	{
    		l.add(new Loc(locs.get(i).x, locs.get(i).y));
    	}
    	MLoc ml = new MLoc(locs.size());
    	ml.locs = l;
    	return ml;
    }

    public boolean equals(MLoc r)
    {
        boolean t = true;
        if (locs.size() != r.locs.size())
        {
        	t = false;
        }
        else
        {
	        for (int i =0; i < locs.size(); i++)
	        {
	            t = t && r.locs.get(i).equals(locs.get(i));
	        }
        }
        return t;
    }


    @Override
    public boolean equals(Object o)
    {
        MLoc r = (MLoc) o;
        return equals(r);
    }


    @Override
    public int hashCode()
    {
    	String s = "";
        for (int i = 0; i< locs.size(); i++)
        {
            s = s + Integer.toString(locs.get(i).hashCode());
        }
        
        return s.hashCode(); //use the strings builtin hash nice
    }
    
    public String toString()
    {
    	String s = "";
    	for (int i = 0; i< locs.size(); i++)
    	{
    		s = s + locs.get(i).toString(); 
    	}
    	return s;
    }
    
}

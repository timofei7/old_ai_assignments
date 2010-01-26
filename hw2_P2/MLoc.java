package hw2_P2;


/**
 *  is the robot location
 */
public class MLoc implements Cloneable
{
	
	public Loc[] locs;
	
	public int id;
			
    public MLoc(int c)
    {
        locs = new Loc[c];
    }
    
    
    /**
     * just make MLoc of a Loc[]
     * @param l
     */
    public MLoc(Loc[] l)
    {
        locs = l;
    }

    
    @Override
    public MLoc clone()
    {
    	Loc[] l = new Loc[locs.length];
    	for (int i=0; i < locs.length; i++)
    	{
    		l[i] = locs[i];
    	}
    	MLoc ml = new MLoc(locs.length);
    	ml.locs = l;
    	return ml;
    }

    public boolean equals(MLoc r)
    {
        boolean t = true;
        if (locs.length != r.locs.length)
        {
        	t = false;
        }
        else
        {
	        for (int i =0; i < locs.length; i++)
	        {
	            t = t && r.locs[i].equals(locs[i]);
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
        for (int i = 0; i< locs.length; i++)
        {
            s = s + Integer.toString(locs[i].hashCode());
        }
        
        //return Integer.parseInt(s);
        return s.hashCode(); //try using strings builtin hashcode
    }
    
    public String toString()
    {
    	String s = "";
    	for (int i = 0; i< locs.length; i++)
    	{
    		s = s + locs[i].toString(); 
    	}
    	return s;
    }

}

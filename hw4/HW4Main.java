package hw4;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 *  main driver for hw4
 * @author tim modified from Devin/Jiawei
 */
public class HW4Main
{
	private static final String names = "ABCDEFGHIJKLMNOPQRSTUVXYZ";

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		//test();
		//System.out.println(coordSet(22, 10, new Rect(2,2)));
		
		ArrayList <CircuitProblem> problems = CircuitProblem.loadCircuitProblems("example.txt");
				
		System.out.println("here's what we read in: ");
		for(CircuitProblem cp:problems)
		{
			System.out.println(cp);
		}
		
		System.out.println("answers:");
		int i = 1;
		for(CircuitProblem cp:problems)
		{
			System.out.println("Problem " + i++ + ": "+solve(cp));	
		}
		
	}
	
	/**
	 * does the main work to solve a circuitproblem
	 * @param cp
	 * @return
	 */
	public static boolean solve(CircuitProblem cp)
	{
		String variables = "";
		String values = "";
		
		//build variables string
		for (int i = 0; i < cp.size; i++)
		{
			if (variables=="") variables = variables + names.charAt(i);
			else variables = variables + "." + names.charAt(i);
		}
		
		//all possible values 
		for (int i = 0; i < cp.width; i++)
		{
			for (int j=0; j<cp.height; j++)
			{
				if (values=="") values = "" + Rect.getInt(i, j, cp.width);//  Integer.toString(i) + Integer.toString(j);
				else values = values + "." + Rect.getInt(i, j, cp.width);
			}
		}
		
		DomainList dl = new DomainList(cp.size, 0);
		
		/*
		 * The domain for each variable is:  {(x,y) | x in 0:m-w, y in 0:n-h } That is all x,y coordinates (for the upper left starting corner)
    	 * that are for x between 0 and m-w and for y between 0 and n-h.  This is on a grid that has 0,0 in upper left.
    	 * we proceed to fill the domain lists
		 */
		int ri = 0;
		for (Rect r : cp.compList) //for each component
		{
			for (int x=0; x<cp.width; x++)
			{
				for (int y=0; y<cp.height; y++)
				{
					if (x <= cp.width - r.width && y <= cp.height - r.height)
					{
						dl.addValue(ri, Rect.getInt(x,y,cp.width));
					}
				}
			}
			ri++;
		}
		
		
		printDL(dl,cp);

		System.out.println("variables: " + variables);
		System.out.println("values: " + values);
		CSP csp = new CSP(variables, dl, values);

		// build the constraints
		int ai = 0;
		HashSet<Integer> seen = new HashSet<Integer>();
		for (Rect a : cp.compList) //this is the first component of the binary constraint
		{
			Set<Integer> ad = dl.getValues(ai); //get the legal values domain for a
			//System.out.println("a: " + a + " ai:" + ai);
			int bi=0;
			for (Rect b : cp.compList) //we run the first component paired against every other component
			{
				//System.out.println("b: " + b + " bi:" + bi);
				String constraint = "";//we'll put the constraint string in here
				if (ai != bi && !seen.contains(bi)) //ignore if the same index indicating the same component or if we've already looked at this pair in reverse
				{		
					for (Integer i : ad)
					{
						Set<Integer> bd = dl.getValues(bi); //get a copy of the legal values domain for b
						Set<Integer> aCols = coordSet(i, cp.width, a);
						bd.removeAll(aCols); //remove all direct collision coordinates from domain for b
						for (Integer c : bd) //now for all the possible positions for b that don't START in a
						{					 //we need to check for start positions that would overlap in the other direction
							Set<Integer> bCols = coordSet(c, cp.width,b);
							if (java.util.Collections.disjoint((Collection<Integer>)bCols, (Collection<Integer>)aCols)) 
							{//are any of the b coords in a's spots? if not great add the constraint!
								if (constraint =="") constraint = i+"."+c;
								else constraint = constraint + ":" + i+"."+c;
								//System.out.println("a: " + Rect.intToString(i, cp.width)+ " b: " + Rect.intToString(c, cp.width));
							}
						}
					}
				}
				if (constraint!="")
				{
					csp.addConstraint(""+names.charAt(ai), ""+names.charAt(bi), constraint);
					System.out.println("constraint: (" + names.charAt(ai)+","+ names.charAt(bi) + ", {"+constraint+"}");
				}
				bi++;
			}
			seen.add(ai);
			ai++;
		}
		
				
		PartialAssignment pa = new PartialAssignment(cp.size);
		
		csp.backtrackingSearch(pa);
		return false;
	}
	
	
	/**
	 * mapcolor test from devin
	 */
	public static void test() {
		
		String variables = "WA.NT.SA.NSW.Q.V";
		String values="r.g.b";
		String constraint = "r.g:r.b:g.r:g.b:b.r:b.g";
		
		
		CSP mapcolor = new CSP(variables, values);
		
		mapcolor.addConstraint("WA", "NT", constraint);
		mapcolor.addConstraint("SA", "NT", constraint);
		mapcolor.addConstraint("SA", "WA", constraint);
		mapcolor.addConstraint("SA", "NSW", constraint);
		mapcolor.addConstraint("SA", "V", constraint);
		mapcolor.addConstraint("SA", "Q", constraint);
		mapcolor.addConstraint("NSW", "Q", constraint);
		mapcolor.addConstraint("NSW", "V", constraint);
		
		PartialAssignment pa = new PartialAssignment(6);
		
		mapcolor.backtrackingSearch(pa);
	}
	
	
	/**
	 * use reflection to pass a converter to print the domainlist
	 * mostly just my effort to learn a bit about java reflection
	 */
	@SuppressWarnings("unchecked")
	public static void printDL(DomainList dl, CircuitProblem cp)
	{
		try
		{
			Class cls = Class.forName("hw4.Rect");
			Object foo = cls.newInstance();
			Method method = cls.getMethod("intToString", new Class[] { int.class, int.class });
			System.out.println(dl.toString(foo, method, cp.width));
		}
		catch (Exception e)
		{ 
			System.out.println("error printing: " + e);
		}
	}
	
	/**
	 * takes a start rect to indicate left upper corner coordinates
	 * and then given the rect r builds a list of all absolute internal coordinates
	 * @param orig
	 * @param cols
	 * @param r
	 * @return set of coordinates
	 */
	public static Set<Integer> coordSet(int orig, int cols, Rect r)
	{
		Rect o = Rect.fromInt(orig, cols);
		int xorg = o.width;
		int yorg = o.height;
		
		Set<Integer> s = new HashSet<Integer>();
				
		for (int x=xorg; x < (xorg + r.width); x++)
		{
			for (int y=yorg; y < (yorg + r.height); y++)
			{
				s.add(Rect.getInt(x, y, cols));
			}
		}
		return s;
	}

}

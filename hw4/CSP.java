package hw4;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.PriorityQueue;


/**
 * CSP class
 * Variables X_0... X_(n-1);
 * Domains D_0... D_(n-1)
 * constraints C_0... C_(m-1);
 * 
 * Simplifying assumption about domains:  Each variable takes on an integer value in 
 * the range 0...x_max.  We can use a hash map or simple function to convert back and
 * forth between integers and elements of a set, if it's necessary to do so.  
 * For example, for a map coloring problem, we might choose red=0, green=1, blue=2.
 * For a circuit board layout, we might convert coordinates (x, y) to x+y*cols.
 * 
 * What sort of data structure do we want to use to store the remaining values of the 
 * variables?  A boolean array will make it easy to delete values or check for a 
 * legitimate value, but enumerating remaining values will be linear in the 
 * size of the domain.  For now we'll use the boolean array, but we should perhaps
 * cache a list of remaining values at the same time, so that the number of enumerations
 * can be minimized.
 * 
 * @author tim (with code from djb, January 2010)
 */
public class CSP {
		
	private int n;  // the number of variables

    private int numValues;  // the maximum size of the domain over all variables
	
    // Hashtables used to convert from string descriptions of variables and values to 
    // integer values.  For example, in a map-coloring problem, we might have 
    // r=0, g=1, b=2.  Internally, this CSP solver only works with integers.
	private Hashtable<String, Integer> variableHash;
	private Hashtable<String, Integer> valueHash;
	 
	//  the "inverse" of the variableHash table.  Given an integer index i, or a value for a variable,
	//  these give the strings corresponding to the names of the variables.  Used only for printing and display;
	//  all of the constraint satisfaction solving is done using just the integer values
	public Hashtable<Integer, String> variableNames;
	public Hashtable<Integer, String> valueNames;
	
	//  The set of constraints, hashed by the set of variables that involve the constraint.  
	//  For example, a constraint between variable X_4 and X_6 would be indexed in the constraint table
	//  by the key (4, 6).  
	private Hashtable<IntegerPair, Constraint> constraintTable;
    
	//  But to check partial assignments, we'll need to iterate over the constraints.  So we'll
	//  also keep a list of the variables that share a constraint
	private ArrayList<IntegerPair> constrainedVariablesList;
	
	// the domain list ordered by variable order
	private DomainList domainlist; 
	
	private boolean solutionFound;
    
	
	/**
	 * construct a CSP
	 * @param variables the variables
	 */
	public CSP(String variables, DomainList dl, String values) {
	
		domainlist = dl;
		
		variableHash = new Hashtable<String, Integer>();
		valueHash = new Hashtable<String, Integer>();
		variableNames = new Hashtable<Integer, String>();
		valueNames = new Hashtable<Integer, String>();
		
		hashPSV(variables, variableHash, variableNames);
		hashPSV(values, valueHash, valueNames);
		n = variableHash.size();
		numValues = valueHash.size();
		
		constraintTable= new Hashtable<IntegerPair, Constraint>();
		constrainedVariablesList = new ArrayList<IntegerPair>();
		
		// add the special character ? to the value hash, used in partial assignments
		valueHash.put("?", -1);
		valueNames.put(-1, "?");
		
		solutionFound = false;
	}
	
	/**
	 * if all value domains are equal
	 * @param variables
	 * @param values
	 */
	public CSP(String variables, String values) {
		
		variableHash = new Hashtable<String, Integer>();
		valueHash = new Hashtable<String, Integer>();
		variableNames = new Hashtable<Integer, String>();
		valueNames = new Hashtable<Integer, String>();
		
		hashPSV(variables, variableHash, variableNames);
		hashPSV(values, valueHash, valueNames);
		n = variableHash.size();
		numValues = valueHash.size();
		
		
		constraintTable= new Hashtable<IntegerPair, Constraint>();
		constrainedVariablesList = new ArrayList<IntegerPair>();
		
		// add the special character ? to the value hash, used in partial assignments
		valueHash.put("?", -1);
		valueNames.put(-1, "?");
		
		solutionFound = false;
	}
	
	/**
	 * add a constraint to the CSP
	 * @param variable1
	 * @param variable2
	 * @param valuePairList
	 */
	public void addConstraint(String variable1, String variable2, String valuePairList) {

		IntegerPair variables;
		int v1 =  variableHash.get(variable1);
		int v2 =  variableHash.get(variable2);
		variables = new IntegerPair(v1, v2);
		Constraint c = new Constraint(valuePairList, valueHash);
		
		constraintTable.put(variables, c);
		
		constrainedVariablesList.add(variables);
		
		// to be careful, add the symmetric constraint.  This is somewhat wasteful of memory,
		// but only in the problem specification.
		
		variables = new IntegerPair(v2, v1);
		constraintTable.put(variables, c);
		
		// System.out.println(c.satisfied(0, 1));
		
	}
	
	/**
	 * Loop over all constraints and check if they are satisfied for this partial assignment
	 * @param pa
	 * @return
	 */
	public boolean checkAssignment(PartialAssignment pa)
	{		
		
		for(IntegerPair variables:constrainedVariablesList)
		{
			int value1 = pa.get(variables.first);
			int value2 = pa.get(variables.second);


			// any constraint involving one unassigned variable is automatically satisfied
			if(value1 != -1 && value2 != -1) {

				Constraint c = constraintTable.get(variables);


				// empty constraint automatically satisfied
				if(c!= null && !c.satisfied(value1, value2))
				{
				    //System.out.print("Constraint violated:  ");
					//System.out.print(variableNames.get(variables.first) + "=" + valueNames.get(value1) );
					//System.out.println(", " + variableNames.get(variables.second) + "=" + valueNames.get(value2) );		
					return false;
				}

			}			
		}
		return true;

	}
	

	/**
	 *  take a period-separated list of strings and create from it hashtables that can be used to map from 
	 *  strings in the list to an index in the list, and vice-versa. 
	 */
	private static void hashPSV(String psv, Hashtable<String, Integer> stringToInt, Hashtable<Integer, String> intToString) {
		
		// split on periods.  There is some weirdness in how regular expressions are parsed.  
		//  backslash period says use the literal period (instead of the special character period, which
		//  is a regex wildcard), but the java compiler doesn't recognize \.  The first backslash escapes the 
		//  backslash for the java compiler, the second backslash escapes the period for the regex parser.
		
		String stringArray[] = psv.split("\\.");
	
		for(int i = 0; i < stringArray.length; i++)
		{
			String s = stringArray[i].trim();
			stringToInt.put(s, i);
			intToString.put(i, s);
		}
	}
	

	
	/**
	 * does a backTrackingSearch
	 * @param assign
	 */
	public void backtrackingSearch(final PartialAssignment assign, int count) {
		
		count = count + 1;
		
		if (solutionFound == true) return;
		
		// Clone the assignment, since we don't want to clobber the values already assigned
		PartialAssignment pa = (PartialAssignment) assign.clone();
		
		ArrayList<Integer> unassignedVars = pa.getUnassignedVariables();
		// If the assignment is complete, test it
		if(unassignedVars.size() == 0)
		{
			if(checkAssignment(pa) )
			{
				// solution found!
				System.out.println("Solution!  ");
				pa.prettyPrint(variableNames, valueNames);
				solutionFound = true;
			}
			else
			{
				return;
			}
		} else {
			// for now just choose the first unassigned variable
			int variable = unassignedVars.get(0);
			// for now just choose the values in order
			for(int value = 0; value < numValues; value++ ) 
			{
				pa.set(variable, value);
				backtrackingSearch(pa, count);
			}
			return;
		}
	}
	
	
	/**
	 *  does a backTrackingSearch
	 *  Idea: choose the variable with the fewest remaining values.
	 *  If you can force a failure, you can prune that section of the search tree. 
	 * @param assign
	 */
	public void backtrackingSearchMRV(final PartialAssignment assign, int count) {
		
		count = count + 1;
		
		if (solutionFound == true) return;
		
		// Clone the assignment, since we don't want to clobber the values already assigned
		PartialAssignment pa = (PartialAssignment) assign.clone();
		
		ArrayList<Integer> unassignedVars = pa.getUnassignedVariables();
		// If the assignment is complete, test it
		if(unassignedVars.size() == 0) 
		{
			if(checkAssignment(pa) ) 
			{
				// solution found!
				System.out.println("Solution!  ");
				pa.prettyPrint(variableNames, valueNames);
				solutionFound = true;
			}
			else 
			{
				return;
			}
		} else {
			// choose the first temporarily
			int variable = unassignedVars.get(0);
			int size = Integer.MAX_VALUE;
			for (Integer v : unassignedVars)
			{
				if (domainlist.getValues(v).size() < size) //choose the one with the smallest domain
				{
					size = domainlist.getValues(v).size();
					variable = v;
				}
			}
			// for now just choose the values in order
			for(Integer value : domainlist.getValues(variable)) 
			{
				pa.set(variable, value);
				backtrackingSearchMRV(pa, count);
			}
			return;
		}
	}
	
	/**
	 *  does a backTrackingSearch
	 *  Idea: choose the variable with the fewest remaining values.
	 *  If you can force a failure, you can prune that section of the search tree. 
	 * @param assign
	 */
	public void backtrackingSearchMRVLCV(final PartialAssignment assign, int count) {

		count = count + 1;
		
		if (solutionFound == true) return;
		
		// Clone the assignment, since we don't want to clobber the values already assigned
		PartialAssignment pa = (PartialAssignment) assign.clone();
		
		ArrayList<Integer> unassignedVars = pa.getUnassignedVariables();
		// If the assignment is complete, test it
		if(unassignedVars.size() == 0) {
			if(checkAssignment(pa) ) {
				// solution found!
				System.out.println("Solution!  ");
				pa.prettyPrint(variableNames, valueNames);
				solutionFound = true;
			} else{
				return;
			}
		} else {
			// choose the first temporarily
			int variable = unassignedVars.get(0);
			int size = Integer.MAX_VALUE;
			for (Integer v : unassignedVars)
			{
				if (domainlist.getValues(v).size() < size) //choose the one with the smallest domain
				{
					size = domainlist.getValues(v).size();
					variable = v;
				}
			}
			PriorityQueue<priorInt> order = new PriorityQueue<priorInt>();
			// for now just choose the values in order
			int o = 0;
			for(Integer value : domainlist.getValues(variable))
			{
				PartialAssignment ta = (PartialAssignment) assign.clone();
				ta.set(variable, value);
				order.add(new priorInt(value,ta.getUnassignedVariables().size()));
				o++;
			}
			//ordered by least constraining value
			for(int i=1; i < domainlist.getValues(variable).size(); i++)
			{
				pa.set(variable, order.poll().value);
				backtrackingSearchMRVLCV(pa, count);
			}
			return;
		}
	}
		
	
	/**
	 * converts to conjunctive normal form
	 * based on creating new symbols
	 * (A,B),{((0,0),(3,3)),((0,0),(5,5))} = A00B33 v A00B55 
	 * anded with (A,C),{((0,0),(3,3)),((0,0),(5,5))} = A00C33 v A00C55
	 * 
	 */
	public void outputCNF()
	{
		System.out.println("CNF: ");
		for (IntegerPair ip: constrainedVariablesList)
		{
			Constraint c = constraintTable.get(ip);
			for ()
		}
		
	}
	
	
	
	/**
	 * an int with a priority
	 * @author tim
	 */
	private class priorInt implements Comparable<priorInt>
	{
		public int priority;
		public int value;
		
		priorInt(int value, int priority)
		{
			this.value = value;
			this.priority = priority;
		}
		
		@Override
		public int compareTo(priorInt o)
		{
			return (int) Math.floor(priority - o.priority);
		}
		
		public String toString()
		{
			return "v: " + value + " p: " + priority;
		}
		
	}
}

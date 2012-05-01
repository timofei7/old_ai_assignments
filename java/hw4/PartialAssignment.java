package hw4;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * a partial assignment gives a value to each variable, and the value -1 to each unassigned variable
 * @author tim modified from dlb
 */
public class PartialAssignment implements Cloneable {
		
		private int assignment[];
		
		
		/**
		 *  create a new empty partial assignment
		 * @param numVars
		 */
		public PartialAssignment(int numVars) {
			assignment = new int[numVars];
			Arrays.fill(assignment, -1);
		}
		
		//TODO: figure out what this was for
//		public PartialAssignment(String assignString, Hashtable<String, Integer> valueHash) 
//		{
//			String[] valueString = assignString.split("\\.");
//			int n = valueString.length;
//			assignment = new int[n];
//			for(int i = 0; i < n; i++) 
//			{
//				assignment[i] = valueHash.get(valueString[i]);
//			}
//						
//		}
		
		/**
		 * get assignment at index
		 * @param index the index 
		 * @return int value 
		 */
		public int get(int index) 
		{
			return assignment[index];
			
		}
		
		/**
		 * set the assignment value at position index
		 * @param index
		 * @param value
		 */
		public void set(int index, int value) 
		{
			assignment[index] = value;
		}

		
		/**
		 * prints the assignment given the translation hashes. 
		 * @param variableNames
		 * @param valueNames
		 */
		public Map<String,String> prettyPrint(Hashtable<Integer, String> variableNames, Hashtable<Integer, String> valueNames) {
			String s = "";
			Map<String, String> a = new HashMap<String, String>();
			for(int var = 0; var < assignment.length; var++) 
			{
				a.put(variableNames.get(var), valueNames.get(assignment[var]));
				s = s + variableNames.get(var) + "=" 
					+ valueNames.get(assignment[var]) + "  " ;
			}
			System.out.println(s);
			return a;
		}
		
		/**
		 * 
		 * create an arraylist with a list of the indices of variables that have not yet been assigned.
		 * these indices give the candidate variables to assign in the next step of a backtracking search
		 * @return
		 */
		public ArrayList<Integer> getUnassignedVariables() {
			ArrayList<Integer> uav = new ArrayList<Integer>();
			for(int i = 0; i < assignment.length; i++) 
			{
				if(assignment[i] == -1)
				{
					uav.add(i);
				}
			}
			
			return uav;
		}
		
		
		/**
		 * deep clone
		 */
		public Object clone() {
			PartialAssignment copy;
			try {
				copy = (PartialAssignment) super.clone();
			}
			catch( CloneNotSupportedException e )
			{
				return null;
			}

			copy.assignment = new int[assignment.length];
			System.arraycopy(assignment, 0, copy.assignment, 0, assignment.length);
			
				
			return copy;
		} 
	
	}
	
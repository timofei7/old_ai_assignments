package hw4;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;


public class PartialAssignment implements Cloneable {
		// a partial assignment gives a value to each variable, and the value -1 to each unassigned variable
		
		private int assignment[];
		
		// create a new empty partial assignment
		public PartialAssignment(int numVars) {
			assignment = new int[numVars];
			Arrays.fill(assignment, -1);
		}
		
		public PartialAssignment(String assignString, Hashtable<String, Integer> valueHash) {

			String[] valueString = assignString.split("\\.");
			int n = valueString.length;
			assignment = new int[n];
			for(int i = 0; i < n; i++) {
				assignment[i] = valueHash.get(valueString[i]);
			}
						
		}
		public int get(int index) {
			return assignment[index];
			
		}
		
		public void set(int index, int value) {
			assignment[index] = value;
		}

		public void prettyPrint(Hashtable<Integer, String> variableNames, Hashtable<Integer, String> valueNames) {
			String s = "";
			for(int var = 0; var < assignment.length; var++) {
				s = s + variableNames.get(var) + "=" 
					+ valueNames.get(assignment[var]) + "  " ;
			}
			System.out.println(s);
		}
		
		// create an arraylist with a list of the indices of variables that have not yet been assigned.
		//  these indices give the candidate variables to assign in the next step of a backtracking search
		public ArrayList<Integer> getUnassignedVariables() {
			ArrayList<Integer> uav = new ArrayList<Integer>();
			for(int i = 0; i < assignment.length; i++) {
				if(assignment[i] == -1){
					uav.add(i);
				}
			}
			
			return uav;
		}
		
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
	
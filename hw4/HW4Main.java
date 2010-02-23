package hw4;

import java.util.ArrayList;


/**
 *  main driver for hw4
 * @author tim (with sample code from Devin/Jiawei)
 */
public class HW4Main
{
	private static final String names = "ABCDEFGHIJKLMNOPQRSTUVXYZ";

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		//tinytest();
		
		ArrayList <CircuitProblem> problems = CircuitProblem.loadCircuitProblems("example.txt");
		
		for(CircuitProblem cp:problems)
		{
			System.out.println(cp);
		}
		
		int i = 1;
		for(CircuitProblem cp:problems) {
			System.out.println("Problem " + i++ + ": "+solve(cp));
			
		}
		
	}
	
	
	public static boolean solve(CircuitProblem cp)
	{
		String variables = "";
		String values = "";
		
		//build variables list
		for (int i = 0; i < cp.size; i++)
		{
			if (variables=="") variables = variables + names.charAt(i);
			else variables = variables + "." + names.charAt(i);
		}
		
		//all possible values //TODO: should we limit the domain here or in the contraints for possible locations?
		for (int i = 0; i < cp.width; i++)
		{
			for (int j=0; j<cp.height; j++)
			{
				if (values=="") values = Integer.toString(i) + Integer.toString(j);
				else values = values + "." + Integer.toString(i) + Integer.toString(j);
			}
		}
		CSP csp = new CSP(variables, values);
		
		
		for (int i = 0; i < cp.width; i++)
		{
			for (int j=0; j<cp.height; j++)
			{
				
			}
		}
		csp.addConstraint("A", "B", "00.02:01.00");
		
		PartialAssignment pa = new PartialAssignment(2);
		
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

}

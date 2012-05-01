package hw4;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * reads in circuit problem from file and stores it
 * @author tim modified from dlb and jiawei
 *
 */
public class CircuitProblem
{
	//board dimensions
	public int height;
	public int width; 
	//number of components
	public int size; 
	
	public ArrayList<Rect> compList;
	
	/**
	 * constructor
	 */
	public CircuitProblem() {
		compList = new ArrayList<Rect>();
	}
	
	/**
	 * add a component to the board
	 * @param c
	 */
	public void add(Rect c) {
		compList.add(c);
	}
	
	public String toString()
	{
		String s = "board: " + width + "," +height +"\npieces: ";
		for (Rect c: compList)
		{
			s = s + c.toString();
		}
		return s;
	}
	
	
	/**
	 * reads a file of circuit problems and spits out an arraylist of said problem objects
	 * @param filename
	 * @return arraylist of circuitproblem objects
	 * (from Devin and Jiawei)
	 */
	public static ArrayList<CircuitProblem> loadCircuitProblems(String filename) {
		
		ArrayList<CircuitProblem> problemList; 
		
		problemList = new ArrayList<CircuitProblem>();

		Scanner sc = null;
		try {
			sc = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String s = null;
		assert (sc.hasNext());

		s = sc.next();
		int numProblems = Integer.parseInt(s); 

		for (int i = 0; i < numProblems; i++) {
			
			CircuitProblem cp = new CircuitProblem();
			
			assert (sc.hasNext());
			s = sc.next();
			int m = Integer.parseInt(s);

			assert (sc.hasNext());
			s = sc.next();
			int n = Integer.parseInt(s);

			assert (sc.hasNext());
			s = sc.next();
			int numComponents = Integer.parseInt(s);

			
			for (int j = 0; j < numComponents; j++) {
				int w, h;
				assert (sc.hasNext());
				s = sc.next();
				w = Integer.parseInt(s);
				if (w > m) { System.out.println("piece is bigger than board! " + j); System.exit(0);}

				assert (sc.hasNext());
				s = sc.next();
				h = Integer.parseInt(s);
				if (h > n) { System.out.println("piece is bigger than board! " + j); System.exit(0);}

				cp.add(new Rect(w, h));
			}
			
			cp.height = n;
			cp.width = m;
			cp.size = numComponents;
			
			problemList.add(cp);
		}
		return problemList;
	}


}

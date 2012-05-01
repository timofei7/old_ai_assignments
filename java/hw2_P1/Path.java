package hw2_P1;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Does a bfs search over urls to find the path connecting them
 * @author tim tregubov
 * cs44 w10 hw1 p1
 */
public class Path
{

	private Parser parser;	
	
	public Path()
	{
		parser = new Parser();
	}
	
	
	public void findURLS(String from, String to)
	{		
		bfs(from, to);
	}
	
	
	/**
	 * the node object for storing a state its parent and its distance
	 */
	private class Node
	{
		String state;
		Node parent;
		double distance;

		Node(String current, Node parent, double distance)
		{
			this.state = current;
			this.parent = parent;
			this.distance = distance;
		}
	}
	
	
	
	/**
	 * do a breadth-first search to find an optimal solution
	 * @return
	 */
	public LinkedList<String> bfs(String initial, String finish)
	{
		Date time = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(time));
		
		LinkedList<Node> frontier = new LinkedList<Node>();
		LinkedList<String> solution = new LinkedList<String>();
		HashSet<String> visited;

		frontier.addLast(new Node(initial, null, 0)); //add start state to a queue
		visited = new HashSet<String>(); // clear the visited list

		while (!frontier.isEmpty())
		{
			Node current = frontier.removeFirst(); //choose a leaf node and remove it from the frontier

			if (goalTest(current.state, finish)) //if the node contains a goal state then return the corresponding solution
			{
				for (Node node = current; node != null; node = node.parent)
				{
					solution.addFirst(node.state);  //add path nodes to solution
				}
				System.out.println("FOUND SOLUTION: " + solution.toString());
				System.out.println("THE DISTANCE IS: " + solution.size());
				time = new java.util.Date();
				System.out.println(sdf.format(time));
				return solution;  //yay!
			}
			
			LinkedList<String> childs = parser.getAnchorsInPage(current.state, finish); //else expand the chosen node,
			
			for(int i = 0; i < childs.size(); i++) {               // adding the resulting nodes to the frontier
				if(! visited.contains(childs.get(i)) ) {     // don't add children if they are on the visited list
					frontier.addLast(new Node(childs.get(i), current, current.distance + 1) );
					//System.out.println(childs.get(i).toString()); //to print order added to frontier
					visited.add(childs.get(i)); // Add the node to the visited list
				}
			}			
		}
		return null;  //if the frontier is empty return failure
	}

	
	
	/**
	 * tests whether we have reached the goal state
	 * 
	 * @param current
	 * @param finish the goal
	 * @return boolean
	 */
	private boolean goalTest(String current, String finish)
	{

		//System.out.println("comparing: " + current + " to " + finish);
		if (current.equalsIgnoreCase(finish)){System.out.println("SCREAM AND SHOUT YAY!!!!!!!");}
		return current.equalsIgnoreCase(finish);
	}
}

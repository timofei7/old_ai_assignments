package hw2_P1;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class Path
{

	private HashSet<String> visited;
	private Parser parser;
	
	private String toURL;
	
	
	public Path()
	{
		parser = new Parser();
	}
	
	
	public void findURLS(String from, String to)
	{
		toURL = to;
		
		bfs(from);
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
	public LinkedList<String> bfs(String initial)
	{
		LinkedList<Node> frontier = new LinkedList<Node>();
		LinkedList<String> solution = new LinkedList<String>();


		frontier.addLast(new Node(initial, null, 0)); //add start state to a queue
		visited = new HashSet<String>(); // clear the visited list

		while (!frontier.isEmpty())
		{
			Node current = frontier.removeFirst(); //choose a leaf node and remove it from the frontier

			if (goalTest(current.state)) //if the node contains a goal state then return the corresponding solution
			{
				for (Node node = current; node != null; node = node.parent)
				{
					solution.addFirst(node.state);  //add path nodes to solution
				}
				System.out.println("FOUND SOLUTION:" + solution.toString());
				return solution;  //yay!
			}
			
			ArrayList<String> childs = parser.getAnchorsInPage(current.state); //else expand the chosen node,
			
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
	 * @return boolean
	 */
	private boolean goalTest(String current)
	{

		System.out.println("comparing: " + current + " to " + toURL);
		if (current.equalsIgnoreCase(toURL)){System.out.println("SCREAM AND SHOUT YAY!!!!!!!");}
		return current.equalsIgnoreCase(toURL);
	}
}

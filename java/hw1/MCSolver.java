package hw1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * This class is for solving the missionary vs cannibals toy problem
 * using a simple breadth first search and a queue
 * @author tim tregubov
 * for cs44w10
 */

public class MCSolver
{

	private HashSet<Integer> visited;

	/**
	 * the node object for storing a state its parent and its distance
	 */
	private class Node
	{
		State state;
		Node parent;
		double distance;

		Node(State current, Node parent, double distance)
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
	public LinkedList<State> bfs()
	{
		LinkedList<Node> frontier = new LinkedList<Node>();
		LinkedList<State> solution = new LinkedList<State>();


		frontier.addLast(new Node(new State(), null, 0)); //add start state to a queue
		visited = new HashSet<Integer>(); // clear the visited list

		while (!frontier.isEmpty())
		{
			Node current = frontier.removeFirst(); //choose a leaf node and remove it from the frontier

			if (goalTest(current.state)) //if the node contains a goal state then return the corresponding solution
			{
				for (Node node = current; node != null; node = node.parent)
				{
					solution.addFirst(node.state);  //add path nodes to solution
				}
				return solution;  //yay!
			}
			
			ArrayList<State> childs = findChildren(current.state); //else expand the chosen node,
			
			for(int i = 0; i < childs.size(); i++) {               // adding the resulting nodes to the frontier
				if(! visited.contains(childs.get(i).get()) ) {     // don't add children if they are on the visited list
					frontier.addLast(new Node(childs.get(i), current, current.distance + 1) );
					//System.out.println(childs.get(i).toString()); //to print order added to frontier
					visited.add(childs.get(i).get()); // Add the node to the visited list
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
	private boolean goalTest(State current)
	{
		return current.missionaries == 0 && 
			   current.cannibals == 0 &&
			   current.boat == 0;
	}

	/**
	 * finds the children of the current state
	 * makes sure they are possible moves according to all the rules
	 * @param current
	 * @return
	 */
	private ArrayList<State> findChildren(State current)
	{
		int move; // this is used to perform the boat move
		int b; // next boat
		ArrayList<State> list = new ArrayList<State>();

		if (current.boat == 1)
		{
			move = -1;
			b = 0;
		} else
		{
			move = 1;
			b = 1;
		}

		for (int i = 1; i <= 5; i++)
		{
			int m, c; // for new values
			m = current.missionaries;
			c = current.cannibals;
			State n = new State();

			// perform one of 5 variations 01,10,11,20,02
			switch (i) {
				case 1:
					c = c + move;
					break;
				case 2:
					m = m + move;
					break;
				case 3:
					c = c + move;
					m = m + move;
					break;
				case 4:
					m = m + move + move;
					break;
				case 5:
					c = c + move + move;
					break;
			}

			if (c >= 0 && m >= 0 && c <= 3 && m <= 3)  //make sure they are in the right range of possible moves
			{
				n.set(m, c, b);
				
				if ( //make sure there are never more cannibals than missionaries
						m==0 		|| //ok to have more cannibals if missionaries are 0
						(3-m) == 0 	|| //on either side
						(c <= m && (3-c) <= (3-m)) //less/equal cannibals on both sides
				   ) 
				{
					list.add(n);
				}
			}
		}
		return list;

	}

	/**
	 * stores the state
	 */
	public class State
	{
		public int missionaries;
		public int cannibals;
		public int boat;

		/**
		 * constructor that initializes to start state of 331
		 */
		public State()
		{
			missionaries = 3;
			cannibals = 3;
			boat = 1;
		}

		/**
		 * sets the state obj
		 * @param m missionaries
		 * @param c cannibals
		 * @param b boat location 1 for original side 0 for other side.
		 */
		public void set(int m, int c, int b)
		{
			missionaries = m;
			cannibals = c;
			boat = b;
		}
		
		/**
		 * gets the state as a string
		 */
		public String toString()
		{
			return Integer.toString(missionaries) + Integer.toString(cannibals) + Integer.toString(boat);
		}
		
		/**
		 * gets the state as one integer
		 */
		public int get()
		{
			return missionaries * 100 + cannibals * 10 + boat;
		}
		
		
		/**
		 * override default object equals
		 * states are euqal if their int values are equal
		 * for some reason the hashset call to this doesn't work though... 
		 * ugh reverting to hashing the int values
		 */
		@Override
		public boolean equals(Object o)
		{
			State p = (State) o;
			return this.get() == p.get();
		}

	}

}

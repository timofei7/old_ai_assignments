package hw2_P2_singlemode;

/**
 * 
 * @author Tim Tregubov
 * using some code written for cs5
 * @version 0.1 4/21/2006
 */

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

import javax.swing.Timer;

public class P2Graphical extends Applet implements ActionListener
{

	private static final long serialVersionUID = 4596161053061003374L;

	private final int GRID_SIZE = 7; // how many cols/rows we want.
	private final int SQUARE_SIZE = 50; // how big to make each square
	private final int HORIZ_OFFSET = 18;
	private final int VERT_OFFSET = 30;
	private final int WIN_SIZE = SQUARE_SIZE * GRID_SIZE; // how big to make the
															// window
	private MMap maze;
	private RobotSolver rs;
	private Timer timer;
	private LinkedList<RobotState> solution;
	private frameManager fm;

	/**
	 * initialize applet
	 */
	public void init()
	{
		setSize(WIN_SIZE, WIN_SIZE);
		maze = new MMap(MMap.MapSet.MAP2);
		repaint();
		rs = new RobotSolver(maze);
		solution = rs.astar();
	    timer = new Timer(1000, this);            // lets have a timer for drawing wavelets
	    timer.start();                                          // start up the timer
	    fm = new frameManager();
	}

	/**
	 * paint the board on every repaint()
	 */
	public void paint(Graphics page)
	{
		drawCheckBoard(page);
	}


	/**
	 * Draws the maze
	 * @param page
	 */
	private void drawCheckBoard(Graphics page)
	{
		for (int i = 0; i < GRID_SIZE; i++)
		{ // create each column up till our gridsize
			for (int j = 0; j < GRID_SIZE; j++)
			{ // create each row up till our gridsize
				if (maze.isCollision(i, j)) // TODO: change this to display walls!
				{
					page.setColor(Color.gray);
				}
				else
				{
					page.setColor(Color.lightGray);
				}
				page.fillRect((i * SQUARE_SIZE), (j * SQUARE_SIZE),
						SQUARE_SIZE, SQUARE_SIZE); // draw our squares!
				
				page.setColor(Color.red);
				if (fm.isA(i, j))
				{
					page.drawString("A", ((i * SQUARE_SIZE) + HORIZ_OFFSET), ((j * SQUARE_SIZE) + VERT_OFFSET));
				}
				if (fm.isB(i,j))
				{
					page.drawString("B", ((i * SQUARE_SIZE) + HORIZ_OFFSET), ((j * SQUARE_SIZE) + VERT_OFFSET));

				}
				if (fm.isC(i,j))
				{
					page.drawString("C", ((i * SQUARE_SIZE) + HORIZ_OFFSET), ((j * SQUARE_SIZE) + VERT_OFFSET));

				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
        fm.next();
        repaint();
	}
	
	
	
	public class frameManager
	{
		private int currentFrame;
		private int numFrames;
		
		frameManager()
		{
			currentFrame = 0;
			numFrames = solution.size();;
		}
		
		public boolean isA(int x, int y)
		{
			return x==solution.get(currentFrame).x && y==solution.get(currentFrame).y;
		}
		public boolean isB(int x, int y)
		{
			return false;
			//return x==solution.get(currentFrame).x && y==solution.get(currentFrame).y;
		}
		public boolean isC(int x, int y)
		{
			return false;
			//return x==solution.get(currentFrame).x && y==solution.get(currentFrame).y;
		}
		
		public void next()
		{
			currentFrame = (currentFrame + 1) % numFrames;
		}
	}

}

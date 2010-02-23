package hw4;

/**
 * a simple height width rectangle tuple
 * @author tim (with sample code from Devin/Jiawei)
 */

public class Rect {
	public int width;
	public int height;
	
	public Rect(int w, int h)
	{
		width = w;
		height = h;
	}
	
	public String toString()
	{
		return "("+width+","+height+")";
	}
	
}

package hw4;

/**
 * a simple height width rectangle tuple
 * @author tim (with sample code from Devin/Jiawei)
 */

public class Rect {
	
	
	public int width;
	public int height;
	
	
	/**
	 * empty constructor
	 */
	public Rect()
	{
		width=0;
		height=0;
	}
	
	
	/**
	 * constructs a rect
	 * @param w
	 * @param h
	 */
	public Rect(int w, int h)
	{
		width = w;
		height = h;
	}
	
	
	/**
	 * rect in a string
	 */
	public String toString()
	{
		return "("+width+","+height+")";
	}
	
	
	/**
	 * converts a rect to a string given the columns
	 */
	public static String intToString(int i, int cols)
	{
		Rect r = fromInt(i,cols);
		return "("+r.width+","+r.height+")";
	}
	
	
	/**
	 * width + height*cols
	 * @param cols
	 * @return
	 */
	public int toInt(int cols)
	{
		return width + (height * cols);
	}
	
	
	/**
	 * width + height*cols
	 */
	public static int getInt(int width, int height, int cols)
	{
		return width + (height * cols);
	}
	
	/**
	 * returns a rect with width/cols set by the width+height*cols integer given
	 * @param z
	 * @param cols
	 */
	public static Rect fromInt (int z, int cols)
	{
		int w = z%cols;
		int h = z/cols;
		return new Rect(w, h);
	}	
}

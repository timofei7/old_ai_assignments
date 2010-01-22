package hw2_P1;


public class P1Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		//Scanner input = new Scanner(System.in);
		Path p = new Path();
		
		//ArrayList<String> t1 = p.getAnchorsInPage("http://www.cs.dartmouth.edu/~tim");
		//ArrayList<String> t2 = p.getAnchorsInPage("http://www.google.com");
		
		//System.out.println(t1);
		//System.out.println(t2);
		
		//p.predecessorWithSearch("www.cs.dartmouth.edu");
		
		p.findURLS("http://www.cs.dartmouth.edu/~tim", "http://www.zingweb.com/tim/link.html");
		
//		do
//		{
//			System.out.print("Enter URL 1: ");
//			String url1 = input.nextLine();
//			System.out.print("Enter URL 2: ");
//			String url2 = input.nextLine();
//		
//			ArrayList<String> t1 = p.getAnchorsInPage(url1);
//			ArrayList<String> t2 = p.getAnchorsInPage(url2);
//
//			System.out.println("\n\nAnother?");
//
//		} while (true);
			    
	}
}

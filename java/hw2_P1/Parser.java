package hw2_P1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;


/**
 * parses pages to pull out links, does some cleanup
 * TODO: cleanup is faulty, doesn't work with wikipedia
 * @author tim tregubov
 * cs44 w10 hw2 p1
 *
 */
public class Parser
{
	
	ParserDelegator parser;  // parse object for html
	HTMLEditorKit.ParserCallback callback;  //callback on tags
	ArrayList<String> list;  //temporary store of links
	
	public Parser()
	{
		parser = new ParserDelegator();
		callback = new ParseTags();
		list = new ArrayList<String>();
	}
	
	
	
	/**
	 * gets all the url references in the page
	 * and fully qualifies them before returning
	 * @param geturl
	 * @return
	 */
	public LinkedList<String> getAnchorsInPage(String geturl, String target)
	{
		list = new ArrayList<String>(); //clean out list
		LinkedList<String> nlist = new LinkedList<String>(); //for modified urls
		

		try
		{
			Thread.sleep(200);

			URL url = null;
			
			try
			{
				url = new URL(geturl);
			} catch (MalformedURLException e)
			{
				System.out.println("	bad url: " + geturl);
			}
			
			BufferedReader reader = null;
			
			try
			{
				reader = new BufferedReader(new InputStreamReader(url.openStream()));
				parser.parse(reader, callback, true);
			} catch (IOException e)
			{
				System.out.println("	interwebs error: " + e.toString());
			}
		
		} catch (Exception e)
		{
			System.out.println("	unknown error: " + e.toString());
		}

		
		//TODO: I am not doing a very robust job of cleaning urls...
		//TODO: this is partially broken and does a bad job with some links
		for (String s : list)
		{
			//System.out.println("Looking at: " + s);
			String temp = s;
			boolean bad = false;
			
			
			if (temp.endsWith("/"))
			{
				temp = temp.substring(0, temp.length() -1);
			}
			
			if (temp.matches("./|/|"))
			{
				bad = true;
			}
			
			if (!bad && temp.startsWith("mailto:"))
			{
				bad = true;
			}
			
			if (!bad && temp.startsWith("/"))
			{
				temp = getDomain(geturl) + temp;
			}
			
			if (!bad && !temp.matches("^https?://.*")) //starts with http or https
			{
				temp = geturl + "/" + temp; //no relative urls
			}
			
			String[] splits = temp.split("/");
			
			if (!bad && (splits.length > 1) && splits[splits.length - 1].matches(".*#.*"))
			{
				bad = true;
			}
			
			
			if (!bad && temp != "")
			{
				nlist.addLast(temp);
			}
		}
		return nlist;
		
	}
	
	
	/**
	 * callback class to parse out all the href's on a page
	 */
	private class ParseTags extends HTMLEditorKit.ParserCallback
	{

		public void handleStartTag(HTML.Tag tag, MutableAttributeSet attr, int pos)
		{
			if (tag == HTML.Tag.A)
			{
				String href=(String)attr.getAttribute(HTML.Attribute.HREF);
				if (href!=null)
				{
					list.add(href);
				}
			}

		}
	}
	
	
	/**
	 * gets domain name from a string if it exists
	 * returns null otherwise
	 * @param g
	 * @return
	 */
	public String getDomain(String g)
	{
		String[] s = g.split("/");
		String ns = null;
		try
		{
			if (s[2] != null && s[2].matches(".*[.][a-zA-Z]{2,3}+")) //pull out domain name
			{
				ns = s[2];
			}
		}
		catch(Exception e)
		{
			System.out.println("\tmalformed domain in: " + g);
		}
		return ns;
		
	}

}

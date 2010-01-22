package hw2_P1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;



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
	public ArrayList<String> getAnchorsInPage(String geturl)
	{
		list = new ArrayList<String>(); //clean out list
		ArrayList<String> nlist = new ArrayList<String>(); //for modified urls
		

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

		
		//TODO: is this enough for qualifying  and cleaning out relative links?
		for (String s : list)
		{
			
			if (s.matches(".*#.*") || s == geturl)
			{
				//do nothing, these just link us back to the same page
			}
			else if (! s.matches("^https?://.*")) //starts with http or https
			{
				nlist.add(geturl + "/" + s); //no relative urls
			}
			else
			{
				nlist.add(s);  //ok as is
			}
		}
		return nlist;
		
	}
	
	
	/**
	 * callback class to parse out all the href's on a page
	 */
	public class ParseTags extends HTMLEditorKit.ParserCallback
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

}

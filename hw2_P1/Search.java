package hw2_P1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Search
{

	
	
	/**
	 * gets predecessors from google
	 * @param geturl is the url to check for links
	 * @return is a list of urls that link to the page given
	 * TODO: CURRENTLY THIS ONLY PULLS THE FIRST 8 results! make it use start and cursor to look for more
	 */
	public ArrayList<String> predecessorWithSearch(String geturl)
	{		
		ArrayList<String> preds = new ArrayList<String>();
		
		try
		{
			// see http://code.google.com/apis/ajaxsearch/documentation/#fonje
			URL url = new URL("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=link:" + geturl);
			URLConnection connection = url.openConnection();
			connection.addRequestProperty("Referer", "http://www.cs.dartmouth.edu/~tim"); // its me

			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}

			JSONObject json = new JSONObject(builder.toString());  //requires json library

			JSONArray rd = json.getJSONObject("responseData").getJSONArray("results");
			
			for (int i =0; i < rd.length(); i++) //go through the current result set pulling out url
			{
				System.out.println(rd.getJSONObject(i).getString("url"));
				preds.add(rd.getJSONObject(i).getString("url"));
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return preds;
	}
	
	
}

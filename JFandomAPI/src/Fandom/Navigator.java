package Fandom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is being used for retrieving web information.
 * @author Mika Thein
 */
public class Navigator {
	
	/**
	 * Returns the redirect URL of a given URL.
	 * @param url the URL
	 * @return the redirect of that URL
	 */
	static public String getRedirect(String url) {
		URL urlTmp = null;
		String redUrl = null;
		HttpURLConnection connection = null;
		url = FandomParser.toURL(url, true);
		try {
			urlTmp = new URL(url);
			connection = (HttpURLConnection) urlTmp.openConnection();
			connection.getResponseCode();
		} catch (IOException e) {
			if(Fandom.DEBUG) e.printStackTrace();
		}

		redUrl = connection.getURL().toString();
		connection.disconnect();
		
		return redUrl;
	}
	
	/**
	 * Receives (first-line) text from a URL and returns it as String.
	 * @param url the URL
	 * @return the web text as String
	 */
	static public String receiveTextFromWebsite(String url) {
		try {
			URL u = new URL(url);
			return new BufferedReader(new InputStreamReader(u.openStream())).readLine();
		} catch(java.io.FileNotFoundException e) {
			if(Fandom.DEBUG) e.printStackTrace();
			throw new NotFoundException("The given URL does not exist");
		} catch(IOException e) {
			if(Fandom.DEBUG) e.printStackTrace();
			throw new RuntimeException("Something went wrong (set static boolean Fandom.DEBUG to true to print exact stack trace)");
		}
	}

}

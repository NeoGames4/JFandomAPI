package Fandom;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Parses text like wikitext or URL text.
 * @author Mika Thein
 */
public class FandomParser {
	
	/**
	 * Converts a text into URL format.
	 * @param text the text
	 * @param encode whether param encoding should be used
	 * @see #parse(String)
	 * @return the encoded text
	 */
	static public String toURL(String text, boolean encode) {
		try {
			if(encode) return URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
			return text.replace(" ", "_");
		} catch(Exception e) {
			if(Fandom.DEBUG) e.printStackTrace();
			return text;
		}
	}
	
	/**
	 * Capitalises every character after a ':', '/', '_', '-' or a ' '.
	 * @see #capitalise(String, String)
	 * @param text the text
	 * @return the capitalised text
	 */
	static public String capitalise(String text) {
		return capitalise(capitalise(capitalise(capitalise(capitalise(text.toLowerCase(), ":"), "/"), "_"), "-"), " ");
	}
	
	/**
	 * Returns the most similar page title.
	 * @param text the page title
	 * @param fandom the Fandom
	 * @return the most similar page title
	 */
	static public String getMostSimilarPageTitle(String title, Fandom fandom) {
		return fandom.search(title)[0].title;
	}
	
	/**
	 * Returns the most similar page title.
	 * @param text the page title
	 * @param tolerance the level of tolerance (0 to 1)
	 * @param fandom the Fandom
	 * @return the most similar page title or the original version of the text
	 */
	static public String getMostSimilarPageTitle(String title, double tolerance, Fandom fandom) {
		try {
			String result = getMostSimilarPageTitle(title, fandom);
			if(Math.min(title.length(), result.length())/Math.max(title.length(), result.length()) <= tolerance || title.trim().equalsIgnoreCase(result.trim())) return title;
			int i = 0;
			for(char c : result.toLowerCase().trim().toCharArray()) {
				for(char ch : title.toLowerCase().trim().toCharArray()) {
					if(c == ch) {
						i++;
						break;
					}
				}
			}
			if(i/title.length() <= tolerance) return title;
			return result;
		}catch(Exception e) { return title; }
	}
	
	/**
	 * Capitalises {@code text} after every appearance of {@code characters}.
	 * @param text the text
	 * @param characters the characters
	 * @see #capitalise(String)
	 * @return the capitalised text
	 */
	static public String capitalise(String text, String characters) {
		StringBuilder result = new StringBuilder("");
		for(String word : text.split(characters)) {
			result.append(characters + word.substring(0, 1).toUpperCase());
			result.append(word.substring(1));
		} return result.toString().substring(characters.length());
	}
	
	/**
	 * Parses wikitext to plain text.
	 * @param text the text
	 * @see #toURL(String, boolean)
	 * @return the parsed text
	 */
	public static String parse(String text) {
		String chars = "A-Za-zÀ-ÖØ-öø-ÿ\\s-_\\d";
		text = text.replace("\n", " ").replaceAll("\\{\\{.*\\}\\}", "");						// {{text}}
		text = text.replaceAll("\\[\\[([" + chars + "]*)\\]\\]", "$1");							// [[mentions]]
		text = text.replaceAll("\\[\\[[" + chars + "]*\\|([" + chars + "]*)\\]\\]", "$1");		// [[mentions|text]]
		text = text.replace("<br>", "\n").replaceAll("<.*>", "");								// <text>
		text = text.replaceAll("[']{2,4}([" + chars + "]*)[']{2,4}", "'$1'");					// ''text''
		text = text.replaceAll("[=]+\\s?([" + chars + "]*)\\s?[=]+", "\n$1\n");					// == text ==
		text = text.replaceAll("\\[\\[.*\\]\\]", "");											// [[text:more text|even more text]]
		text = text.replace("&quot;", "\"").replace("&amp;", "&");								// &quot; &amp;
		return text.replaceAll("\n[\\s]*", "\n");
	}


}

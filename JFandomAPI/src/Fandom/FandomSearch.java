package Fandom;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class can be used to search the {@link Fandom}.
 * @author Mika Thein
 * @see #FandomSearch(FandomSearchOptions, Fandom)
 * @see #search(String)
 */
public class FandomSearch {
	
	/**
	 * The {@link Fandom}.
	 */
	public final Fandom fandom;
	/**
	 * The search options.
	 * @see {@link FandomSearchOptions}
	 */
	public final FandomSearchOptions options;
	
	/**
	 * Creates a new FandomSearch instance.
	 * @param options search options (namespaces, limit, offset, ...)
	 * @param fandom the Fandom
	 * @see FandomSearchOptions
	 */
	public FandomSearch(FandomSearchOptions options, Fandom fandom) {
		this.fandom = fandom;
		this.options = options;
	}
	
	/**
	 * Searches the Fandom.
	 * @param text search term
	 * @return the results as a {@link FandomSearchResult} array
	 */
	public FandomSearchResult[] search(String text) {
		try {
			String result = Navigator.receiveTextFromWebsite(buildUrl(text));
			JSONArray jsonArray = new JSONObject(result).getJSONObject("query").getJSONArray("search");
			FandomSearchResult[] results = new FandomSearchResult[jsonArray.length()];
			for(int i = 0; i<jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String title = jsonObject.getString("title");
				int namespace = jsonObject.getInt("ns");
				long size = jsonObject.getLong("size");
				long wordCount = jsonObject.getLong("wordcount");
				String timestamp = jsonObject.getString("timestamp");
				results[i] = new FandomSearchResult(title, jsonObject.getLong("pageid"), namespace, size, wordCount, timestamp, fandom);
			} return results;
		} catch(Exception e) {
			if(Fandom.DEBUG) e.printStackTrace();
		} Fandom.warn("Caught an exception while looking for results. Something might went wrong.\nSet static boolean Fandom.DEBUG to true to print the exact stack trace");
		return new FandomSearchResult[0];
	}
	
	private String buildUrl(String text) {
		String parameters = "";
		if(options != null) {
			if(options.getNamespaces() != null) parameters += "&srnamespace=" + options.getNamespaces();
			else if(options.getOffset() > 0) parameters += "&sroffset=" + options.getOffset();
			else if(options.getType() >= 0) parameters += "&srwhat=" + (options.getType() == FandomSearchOptions.TITLE ? "title" : options.getType() == FandomSearchOptions.TEXT ? "text" : "nearmatch");
			else if(options.getSortPriority() == FandomSearchOptions.RELEVANCE) parameters += "&srsort=relevance";
			parameters += "&srlimit=" + options.getLimit();
		}
		return "https://" + fandom.baseUrl + "/api.php?action=query&list=search&srsearch=" + FandomParser.toURL(text, true) + parameters + "&srprop=size|wordcount|timestamp&format=json";
	}
	
	/**
	 * A search result.
	 * @author Mika Thein
	 * @see #getAsFandomArticle()
	 * @see #getAsFandomImage()
	 * @see #getAsFandomUser()
	 * @see #getAsFandomPage()
	 */
	public class FandomSearchResult {
		
		/**
		 * The title.
		 */
		public final String title;
		/**
		 * The ID.
		 */
		public final long id;
		/**
		 * The namespace of the result.
		 */
		public final int namespace;
		/**
		 * The size of the result.
		 */
		public final long size;
		/**
		 * The amount of words.
		 */
		public final long wordCount;
		/**
		 * The timestamp.
		 */
		public final String timestamp;
		/**
		 * The Fandom.
		 */
		public final Fandom fandom;
		
		/**
		 * Creates a new FandomSearchResult instance (should not be used manually!).<p>
		 * <b>Use {@link FandomSearch#search(String)} instead.</b>
		 * @param title the title
		 * @param id the ID
		 * @param namespace the namespace
		 * @param size the size
		 * @param wordCount the word count
		 * @param timestamp the timestamp
		 * @param fandom the Fandom
		 */
		public FandomSearchResult(String title, long id, int namespace, long size, long wordCount, String timestamp, Fandom fandom) {
			this.title = title;
			this.id = id;
			this.namespace = namespace;
			this.size = size;
			this.wordCount = wordCount;
			this.timestamp = timestamp;
			this.fandom = fandom;
		}
		
		/**
		 * @return the result as FandomPage
		 * @see #getAsFandomArticle()
		 * @see #getAsFandomImage()
		 * @see #getAsFandomCategory()
		 * @see #getAsFandomUser()
		 */
		public FandomPage getAsFandomPage() {
			return new FandomPage(id, fandom);
		}
		
		/**
		 * @return the result as FandomArticle
		 * @see #getAsFandomPage()
		 * @see #getAsFandomImage()
		 * @see #getAsFandomCategory()
		 * @see #getAsFandomUser()
		 */
		public FandomArticle getAsFandomArticle() {
			return new FandomArticle(id, fandom);
		}
		
		/**
		 * @return the result as FandomImage
		 * @see #getAsFandomPage()
		 * @see #getAsFandomArticle()
		 * @see #getAsFandomCategory()
		 * @see #getAsFandomUser()
		 */
		public FandomImage getAsFandomImage() {
			return new FandomImage(id, fandom);
		}
		
		/**
		 * @return the result as FandomCategory
		 * @see #getAsFandomPage()
		 * @see #getAsFandomArticle()
		 * @see #getAsFandomImage()
		 * @see #getAsFandomUser()
		 */
		public FandomCategory getAsFandomCategory() {
			return new FandomCategory(title, fandom);
		}
		
		/**
		 * @return the result as FandomUser
		 * @see #getAsFandomPage()
		 * @see #getAsFandomArticle()
		 * @see #getAsFandomImage()
		 * @see #getAsFandomCategory()
		 */
		public FandomUser getAsFandomUser() {
			return new FandomUser(id, fandom);
		}
		
	}

}

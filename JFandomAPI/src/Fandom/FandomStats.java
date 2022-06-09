package Fandom;
import org.json.JSONObject;

/**
 * Can be used to receive statistics about a Fandom.
 * @author Mika Thein
 * @see Fandom
 */
public class FandomStats {
	
	/**
	 * The Fandom.
	 */
	public final Fandom fandom;
	/**
	 * The amount of edits.
	 */
	final public long edits;
	/**
	 * The amount of articles.
	 */
	final public long articles;
	/**
	 * The amount of pages.
	 */
	final public long pages;
	/**
	 * The amount of images.
	 */
	final public long images;
	/**
	 * The amount of users.
	 */
	final public long users;
	/**
	 * The amount of users (last 30 days).
	 */
	final public long activeUsers;
	/**
	 * The amount of admins.
	 */
	final public long admins;
	/**
	 * The amount of jobs.
	 */
	final public long jobs;
	/**
	 * The amount of forum posts.
	 */
	final public long forumPosts;
	/**
	 * The amount of wall posts.
	 */
	final public long wallPosts;
	/**
	 * The amount of article comments.
	 */
	final public long articleComments;
	/**
	 * The amount of total posts.
	 */
	final public long totalPosts;
	
	/**
	 * Creates a new FandomStats instance.
	 * @param fandom the Fandom
	 */
	public FandomStats(Fandom fandom) {
		this.fandom = fandom;
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=query&meta=siteinfo&siprop=statistics&format=json")).getJSONObject("query").getJSONObject("statistics");
		edits = jsonObject.getLong("edits");
		articles = jsonObject.getLong("articles");
		pages = jsonObject.getLong("pages");
		images = jsonObject.getLong("images");
		users = jsonObject.getLong("users");
		activeUsers = jsonObject.getLong("activeusers");
		admins = jsonObject.getLong("admins");
		jobs = jsonObject.getLong("jobs");
		jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/wikia.php?controller=DiscussionPost&method=getPosts&limit=1&format=json")).getJSONObject("_embedded").getJSONArray("count").getJSONObject(0);
		forumPosts = jsonObject.getLong("FORUM");
		wallPosts = jsonObject.getLong("WALL");
		articleComments = jsonObject.getLong("ARTICLE_COMMENT");
		totalPosts = jsonObject.getLong("total");
	}
	
	/**
	 * @return the url to the statistics page
	 */
	public String getUrl() {
		return "https://" + fandom.baseUrl + "/wiki/Special:Statistics";
	}
	
	@Override
	public String toString() {
		return getUrl();
	}

}

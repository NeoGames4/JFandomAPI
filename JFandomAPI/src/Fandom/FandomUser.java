package Fandom;
import java.io.IOException;

import org.json.JSONObject;

import FandomActivity.FandomPost;
import FandomActivity.FandomRecentChange;

/**
 * This class reprsents Fandom users.
 * @author Mika Thein
 * @see Fandom
 * @see Fandom#getUser(long)
 * @see Fandom#getUser(String)
 */
public class FandomUser {
	
	public static final int NAMESPACE = Fandom.NAMESPACE_USER;
	
	/**
	 * The username.
	 */
	public final String username;
	/**
	 * The user ID.
	 */
	public final long id;
	private String name;
	private String avatarUrl;
	private String bio;
	private String websiteUrl;
	private String twitterName;
	private String facebookName;
	private String discordName;
	private double edits;
	private int localEdits;
	private int posts;
	private String registrationDate;
	private String url;
	private String discussionsUrl;
	
	private FandomArticle article;
	
	/**
	 * The Fandom.
	 */
	public final Fandom fandom;

	/**
	 * Creates a new FandomUser instance.
	 * @param username the username
	 * @param fandom the Fandom
	 * @see #FandomUser(long, Fandom)
	 */
	public FandomUser(String username, Fandom fandom) {
		this(getUserIdFromUsername(username, fandom), fandom);
	}
	
	/**
	 * Returns the user ID by the user's name.
	 * @param username the username
	 * @param fandom the Fandom
	 * @return the user ID
	 */
	private static long getUserIdFromUsername(String username, Fandom fandom) {
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=query&list=users&ususers=" + FandomParser.toURL(username, true) + "&format=json")).getJSONObject("query").getJSONArray("users").getJSONObject(0);
		return jsonObject.getLong("userid");
	}
	
	/**
	 * Creates a new FandomUser instance.
	 * @param userId the user ID
	 * @param fandom the Fandom
	 * @see #FandomUser(String, Fandom)
	 */
	public FandomUser(long userId, Fandom fandom) {
		this.id = userId;
		this.fandom = fandom;
		try {
			String text = Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/wikia.php?controller=UserProfile&method=getUserData&format=json&userId=" + userId);
			JSONObject jsonObject = new JSONObject(text).getJSONObject("userData");
			username = jsonObject.optString("username");
			article = fandom.articleExists("User:" + username) ? new FandomArticle("User:" + username, fandom) : null;
			avatarUrl = jsonObject.optString("avatar");
			name = jsonObject.isNull("name") ? null : jsonObject.getString("name");
			bio = jsonObject.isNull("bio") ? null : jsonObject.getString("bio");
			websiteUrl = jsonObject.isNull("website") ? null : jsonObject.optString("website");
			twitterName = jsonObject.isNull("twitter") ? null : jsonObject.optString("twitter");
			facebookName = jsonObject.isNull("fbPage") ? null : jsonObject.optString("fbPage");
			discordName = jsonObject.isNull("discordHandle") ? null : jsonObject.optString("discordHandle");
			edits = jsonObject.optInt("edits");
			localEdits = jsonObject.getInt("localEdits");
			posts = jsonObject.getInt("posts");
			registrationDate = jsonObject.optString("registration");
			url = jsonObject.optString("userPage");
			discussionsUrl = jsonObject.has("discussionsUrl") ? jsonObject.optString("discussionUserUrl") : null;
		} catch(Exception e) {
			if(Fandom.DEBUG) e.printStackTrace();
			throw new ReadingException("Can't read user page content (set static Fandom.DEBUG to true to print the exact exception stack trace)\n(This exception might occur if the page is not really an user page.)");
		}
	}
	
	/**
	 * Returns the user's page as article (might be {@code null}).
	 * @see #hasUserPage()
	 * @return the user's page as article
	 */
	public FandomArticle getAsArticle() {
		return article;
	}
	
	/**
	 * @return the name
	 */
	public String getName() { return name; }
	
	/**
	 * @return the avatar URL
	 */
	public String getAvatarUrl() { return avatarUrl; }
	
	/**
	 * @return the bio
	 */
	public String getBio() { return bio; }
	
	/**
	 * @return the website
	 */
	public String getWebsite() { return websiteUrl; }
	
	/**
	 * @return the Twitter username
	 */
	public String getTwitter() { return twitterName; }
	
	/**
	 * @return the Facebook username
	 */
	public String getFacebook() { return facebookName; }
	
	/**
	 * @return the Discord handle
	 */
	public String getDiscord() { return discordName; }
	
	/**
	 * @return the amount of edits by this user
	 */
	public double getEditsCount() { return edits; }
	
	/**
	 * @return the amount of local edits by this user
	 */
	public int getLocalEditsCount() { return localEdits; }
	
	/**
	 * @return the amount of posts
	 */
	public int getPostsCount() { return posts; }
	
	/**
	 * @return the registration date
	 */
	public String getRegistrationDate() { return registrationDate; }
	
	/**
	 * @return the discussions URL
	 */
	public String getDiscussionsUrl() { return discussionsUrl != null ? fandom.baseUrl + discussionsUrl : null; }
	
	/**
	 * @see #getAsArticle()
	 * @return whether the user has a user page
	 */
	public boolean hasUserPage() { return article != null; }
	
	/**
	 * Might want to use getAsArticle().getWikitextDescription() instead.
	 * @see #getAsArticle()
	 * @return returns the plain description of the user page
	 */
	public String getUserPageDescription() { return article.description; }
	
	/**
	 * Returns recent changes by this user.
	 * @param limit the max amount of recent changes (0 to 500)
	 * @see #getRecentChanges(FandomPage, int)
	 * @see #getRecentPosts()
	 * @return recent changes
	 * @throws IOException if the API is unreachable
	 */
	public FandomRecentChange[] getRecentChanges(int limit) throws IOException {
		return fandom.getActivity().getRecentChanges(this, limit);
	}
	
	/**
	 * Returns recent changes at a specific page by this user.
	 * @param page the Fandom page
	 * @param limit the max amount of recent changes (0 to 500)
	 * @see #getRecentChanges(int)
	 * @see #getRecentPosts()
	 * @return recent changes
	 * @throws IOException if the API is unreachable
	 */
	public FandomRecentChange[] getRecentChanges(FandomPage page, int limit) throws IOException {
		return fandom.getActivity().getRecentChanges(this, page, limit);
	}
	
	/**
	 * Returns recent forum posts by this user.
	 * @param limit the max amount of recent posts (0 to 100)
	 * @see #getRecentWallPosts(int)
	 * @see #getRecentArticleComments(int)
	 * @see #getRecentPosts()
	 * @see FandomActivity.FandomActivity#getRecentForumPosts(FandomUser, int)
	 * @return recent forum posts by this user
	 * @throws IOException if the API is unreachable
	 */
	public FandomPost[] getRecentForumPosts(int limit) throws IOException {
		return fandom.getActivity().getRecentForumPosts(this, limit);
	}
	
	/**
	 * Returns recent wall posts by this user.
	 * @param limit the max amount of recent posts (0 to 100)
	 * @see #getRecentForumPosts(int)
	 * @see #getRecentArticleComments(int)
	 * @see #getRecentPosts()
	 * @see FandomActivity.FandomActivity#getRecentWallPosts(FandomUser, int)
	 * @return recent wall posts by this user
	 * @throws IOException if the API is unreachable
	 */
	public FandomPost[] getRecentWallPosts(int limit) throws IOException {
		return fandom.getActivity().getRecentWallPosts(this, limit);
	}
	
	/**
	 * Returns recent article comments by this user.
	 * @param limit the max amount of recent posts (0 to 100)
	 * @see #getRecentForumPosts(int)
	 * @see #getRecentWallPosts(int)
	 * @see #getRecentPosts()
	 * @see FandomActivity.FandomActivity#getRecentArticleComments(FandomUser, int)
	 * @return recent article comments by this user
	 * @throws IOException if the API is unreachable
	 */
	public FandomPost[] getRecentArticleComments(int limit) throws IOException {
		return fandom.getActivity().getRecentArticleComments(this, limit);
	}
	
	/**
	 * Returns recent posts by this user.
	 * @see #getRecentForumPosts(int)
	 * @see #getRecentWallPosts(int)
	 * @see #getRecentArticleComments(int)
	 * @see FandomActivity.FandomActivity#getRecentPosts(FandomUser, int, int, int)
	 * @return recent posts by this user
	 * @throws IOException if the API is unreachable
	 */
	public FandomPost[] getRecentPosts() throws IOException {
		return fandom.getActivity().getRecentPosts(this, 100, 100, 100);
	}
	
	/**
	 * @return the URL
	 */
	public String getUrl() {
		return url;
	}
	
	@Override
	public String toString() {
		return getUrl();
	}
	
}

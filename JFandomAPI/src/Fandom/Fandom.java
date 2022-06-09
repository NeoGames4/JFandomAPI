package Fandom;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.json.JSONObject;

import Fandom.FandomSearch.FandomSearchResult;
import FandomActivity.FandomActivity;
import FandomActivity.FandomPost;
import FandomActivity.FandomThread;

/**
 * The Fandom instance represents a Fandom community.
 * @author Mika Thein
 * @version 1.0
 * @see #Fandom(String)
 * @see #getPage(long)
 * @see #getArticle(long)
 * @see #getUser(long)
 */
public class Fandom {
	
	/**
	 * Whether debug information should be printed.
	 * @see #PRINT_WARNINGS
	 * @see #RESET_STORAGE
	 */
	public static boolean DEBUG = false;
	/**
	 * Whether warnings should be printed.
	 * @see #DEBUG
	 * @see #RESET_STORAGE
	 */
	public static boolean PRINT_WARNINGS = true;
	
	/**
	 * Whether the storage of recent activities of a Fandom community should be cleared after a new {@link Fandom} instance was created.
	 * <p>If this value is {@code true}, all listeners may react on events they already have reacted on every time a new instance is being created.
	 * @see #DEBUG
	 * @see #PRINT_WARNINGS
	 */
	public static boolean RESET_STORAGE = true;
	
	public static final int NAMESPACE_MEDIA = -2;
	public static final int NAMESPACE_SPECIAL = -1;
	public static final int NAMESPACE_MAIN = 0;
	public static final int NAMESPACE_TALK = 1;
	public static final int NAMESPACE_USER = 2;
	public static final int NAMESPACE_USER_TALK = 3;
	public static final int NAMESPACE_PROJECT = 4;
	public static final int NAMESPACE_PROJECT_TALK = 5;
	public static final int NAMESPACE_FILE = 6;
	public static final int NAMESPACE_FILE_TALK = 7;
	public static final int NAMESPACE_MEDIA_WIKI = 8;
	public static final int NAMESPACE_MEDIA_WIKI_TALK = 9;
	public static final int NAMESPACE_TEMPLATE = 10;
	public static final int NAMESPACE_TEMPLATE_TALK = 11;
	public static final int NAMESPACE_HELP = 12;
	public static final int NAMESPACE_HELP_TALK = 13;
	public static final int NAMESPACE_CATEGORY = 14;
	public static final int NAMESPACE_CATEGORY_TALK = 15;
	
	/**
	 * The base URL of the Fandom including the server name and language (optional).
	 * <p>Examples:<br>
	 * {@code disney.fandom.com}<br>
	 * {@code avatar.fandom.com/de}
	 * @see #root
	 * @see #lang
	 * @see #name
	 */
	public final String baseUrl;
	/**
	 * The server name.
	 * @see #baseUrl
	 */
	public final String root;
	/**
	 * The Fandom name.
	 * @see #id
	 * @see #lang
	 * @see #icon
	 */
	public final String name;
	/**
	 * The Fandom ID.
	 * @see #name
	 */
	public final String id;
	/**
	 * The Fandom language (as short form, like {@code en} or {@code de}).
	 */
	public final String lang;
	/**
	 * The Fandom icon.
	 * @see #name
	 * @see #id
	 */
	public final String icon;
	/**
	 * The title of the main page.
	 * @see #getMainPageUrl()
	 * @see #getUrl()
	 */
	public final String mainPageTitle;
	private String timeZone;
	private String generator;
	private String phpVersion;
	private String phpsAPI;
	private String dbType;
	private String dbVersion;
	private String legalTitleChars;
	private String illegalUsernameChars;
	private long maxArticleSize;
	private long maxUploadSize;
	private long minUploadChunkSize;
	private String rightsInfoText;
	private String rightsInfoUrl;
	
	/**
	 * Creates a new Fandom instance.
	 * The argument has to match the following regular expression pattern:
	 * <blockquote><pre>[a-z-]+\.fandom\.com[/a-z-]*</pre></blockquote>
	 * It should look like "{@code fandom-id.fandom.com[/lang]}"<br>
	 * Examples:<br>
	 * {@code disney.fandom.com}<br>
	 * {@code avatar.fandom.com/de}
	 * @param fandomUrl the server name and language (optional)
	 * @throws InvalidFormatException if {@code fandomUrl} does not match the regular expression or the length is larger than 50 characters or shorter than 3 characters
	 * @throws NotFoundException if the FANDOM couldn't be found
	 * @throws RuntimeException if something else went wrong (set {@link #DEBUG} to {@code true} to receive more information)
	 * @see #baseUrl
	 */
	public Fandom(String fandomUrl) {
		if(!Pattern.matches("[a-z-]+\\.fandom\\.com[/a-z-]*", fandomUrl)) throw new InvalidFormatException("Fandom-URL \"" + fandomUrl + "\" does not match the pattern \"fandom-name.fandom.com[/lang]\"");
		id = fandomUrl.split(".fandom.com", 2)[0];
		if(id.length() > 50) throw new InvalidFormatException("Fandom-ID (URL name) \"" + id + "\" can't be longer than 50 characters");
		else if(id.length() < 3) throw new InvalidFormatException("Fandom-ID (URL name) \"" + id + "\" can't be shorter than 3 characters");
		baseUrl = fandomUrl;
		String root = "";
		String name = "";
		String lang = "";
		String icon = "";
		String mainPageTitle = "";
		try {
			String text = Navigator.receiveTextFromWebsite("https://" + baseUrl + "/api.php?action=query&meta=siteinfo&siprop=general|rightsinfo&format=json");
			JSONObject jsonObject = new JSONObject(text).getJSONObject("query").getJSONObject("general");
			root = jsonObject.getString("servername");
			name = jsonObject.getString("sitename");
			lang = jsonObject.has("lang") ? jsonObject.getString("lang") : "en";
			icon = jsonObject.getString("logo");
			mainPageTitle = jsonObject.getString("mainpage");
			timeZone = jsonObject.getString("timezone");
			generator = jsonObject.getString("generator");
			phpVersion = jsonObject.getString("phpversion");
			phpsAPI = jsonObject.getString("phpsapi");
			dbType = jsonObject.getString("dbtype");
			dbVersion = jsonObject.getString("dbversion");
			legalTitleChars = jsonObject.getString("legaltitlechars");
			illegalUsernameChars = jsonObject.getString("invalidusernamechars");
			maxArticleSize = jsonObject.getLong("maxarticlesize");
			maxUploadSize = jsonObject.getLong("maxuploadsize");
			minUploadChunkSize = jsonObject.getLong("minuploadchunksize");
			jsonObject = new JSONObject(text).getJSONObject("query").getJSONObject("rightsinfo");
			rightsInfoText = jsonObject.getString("text");
			rightsInfoUrl = jsonObject.getString("url");
		} catch(NotFoundException e) {
			throw new NotFoundException("The given fandom does not exist");
		} catch(RuntimeException e) {
			if(DEBUG) e.printStackTrace();
			throw new RuntimeException("Something unexpected went wrong. The fandom-URL might be incorrect or the WikiMedia-API has been updated (set static boolean Fandom.DEBUG to true to print exact stack trace)");
		}
		this.root = root;
		this.name = name;
		this.lang = lang;
		this.icon = icon;
		this.mainPageTitle = mainPageTitle;
		try {
			if(!FandomActivity.storageFile.exists()) {
				FandomActivity.storageFile.getParentFile().mkdirs();
				if(FandomActivity.storageFile.createNewFile()) {
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FandomActivity.storageFile));
					bufferedWriter.write("{}");
					bufferedWriter.close();
				}
			}
		} catch(Exception e) {}
		if(RESET_STORAGE && FandomActivity.storageFile.exists()) {
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(FandomActivity.storageFile));
				String lines = "";
				for(String l; (l = bufferedReader.readLine()) != null; lines += l);
				bufferedReader.close();
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FandomActivity.storageFile));
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("last", "");
				JSONObject total = new JSONObject(lines).put(baseUrl, jsonObject);
				bufferedWriter.write(total.toString());
				bufferedWriter.close();
			} catch(Exception e) {}
		}
	}
	
	/**
	 * Returns the rights info text.
	 * @return rights info text
	 */
	public String getRightsInfoText() {
		return rightsInfoText;
	}
	
	/**
	 * Returns the URL leading to the rights information.
	 * @return rights info URL
	 */
	public String getRightsInfoUrl() {
		return rightsInfoUrl;
	}
	
	/**
	 * Searches the Fandom for a specific argument.
	 * @param argument the search argument
	 * @return search results as {@link FandomSearchResult}
	 * @see #search(String, FandomSearchOptions)
	 * @see #getSearchUrl(String)
	 */
	public FandomSearchResult[] search(String argument) {
		return search(argument);
	}
	
	/**
	 * Searches the Fandom for a specific argument.
	 * @param argument the search argument
	 * @param options search options
	 * @return results as {@link FandomSearchResult}
	 * @see #search(String)
	 * @see #getSearchUrl(String)
	 */
	public FandomSearchResult[] search(String argument, FandomSearchOptions options) {
		return new FandomSearch(options, this).search(argument);
	}
	
	/**
	 * Returns the search URL of the search page of {@code text}.
	 * @param argument the search argument
	 * @return the URL of the search page
	 * @see #search(String)
	 * @see #getMainPageUrl()
	 * @see #getUrl()
	 */
	public String getSearchUrl(String argument) {
		return "https://" + baseUrl + "/wiki/Special:Search?query=" + FandomParser.toURL(argument, true);
	}
	
	/**
	 * Returns Fandom statistics as {@link FandomStats}.
	 * @return FandomStats Fandom statistics
	 */
	public FandomStats getStats() {
		return new FandomStats(this);
	}
	
	/**
	 * Returns the URL of the main page.
	 * @return the URL of the main page
	 * @see #getSearchUrl(String)
	 * @see #getUrl()
	 */
	public String getMainPageUrl() {
		return "https://" + baseUrl + "/wiki/" + FandomParser.toURL(mainPageTitle, true);
	}
	
	/**
	 * Checks if the article {@code articleTitle} exists.
	 * @param articleTitle the title of the article
	 * @return whether the article exists
	 */
	public boolean articleExists(String articleTitle) {
		try {
			getArticle(articleTitle);
			return true;
		} catch(Exception e) {
			if(DEBUG) e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Checks if the user {@code username} exists.
	 * @param username the username
	 * @return whether the user exists
	 */
	public boolean userExists(String username) {
		try {
			FandomUser fandomUser = new FandomUser(username, this);
			return true;
		} catch(Exception e) {
			if(DEBUG) e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Checks if the page with ID {@code pageId} exists.
	 * @param pageId the page id
	 * @return whether the page exists
	 */
	public boolean pageExists(long pageId) {
		try {
			new FandomPage(pageId, this);
			return true;
		} catch(Exception e) {
			if(DEBUG) e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Returns the page with title {@code title} as {@link FandomPage}.
	 * @param title the page title
	 * @return the page as FandomPage
	 * @throws NotFoundException if the page couldn't be found
	 * @see #getPage(long)
	 * @see #getArticle(String)
	 * @see #getUser(String)
	 * @see #getImage(String)
	 */
	public FandomPage getPage(String title) {
		return new FandomPage(title, this);
	}
	
	/**
	 * Returns the page with ID {@code id} as {@link FandomPage}.
	 * @param id the page ID
	 * @return the page as FandomPage
	 * @throws NotFoundException if the page couldn't be found
	 * @see #getPage(String)
	 * @see #getArticle(long)
	 * @see #getUser(long)
	 * @see #getImage(long)
	 * @see #getDiscussionPost(long)
	 * @see #getDiscussionThreadByPostId(long)
	 * @see #getDiscussionThread(long)
	 */
	public FandomPage getPage(long id) {
		return new FandomPage(id, this);
	}
	
	/**
	 * Returns the article with title {@code title} as {@link FandomArticle}.
	 * @param title the page title
	 * @return the page as FandomArticle
	 * @throws NotFoundException if the article couldn't be found
	 * @throws ReadingException if the content couldn't be read
	 * @see #getArticle(long)
	 * @see #getPage(String)
	 */
	public FandomArticle getArticle(String title) {
		return new FandomArticle(title, this);
	}
	
	/**
	 * Returns the article with ID {@code id} as {@link FandomArticle}.
	 * @param id the page ID
	 * @return the page as FandomArticle
	 * @throws NotFoundException if the article couldn't be found
	 * @throws ReadingException if the content couldn't be read
	 * @see #getArticle(String)
	 * @see #getPage(long)
	 */
	public FandomArticle getArticle(long id) {
		return new FandomArticle(id, this);
	}
	
	/**
	 * Returns the user named {@code name} as {@link FandomUser}.
	 * @param name the username
	 * @return the user as FandomUser
	 * @throws NotFoundException if the article couldn't be found
	 * @throws ReadingException if the content couldn't be read
	 * @see #getUser(long)
	 * @see #getPage(String)
	 */
	public FandomUser getUser(String name) {
		return new FandomUser(name, this);
	}
	
	/**
	 * Returns the user with ID {@code id} as {@link FandomUser}.
	 * @param id the user ID
	 * @return the user as FandomUser
	 * @throws NotFoundException if the article couldn't be found
	 * @throws ReadingException if the content couldn't be read
	 * @see #getUser(String)
	 * @see #getPage(long)
	 */
	public FandomUser getUser(long id) {
		return new FandomUser(id, this);
	}
	
	/**
	 * Returns the image with file name {@code fileName} as {@link FandomImage}.
	 * @param fileName the file name
	 * @return the image as FandomImage
	 * @throws NotFoundException if the article couldn't be found
	 * @throws ReadingException if the content couldn't be read
	 * @see #getImage(long)
	 * @see #getPage(String)
	 */
	public FandomImage getImage(String fileName) {
		return new FandomImage(fileName, this);
	}
	
	/**
	 * Returns the image with ID {@code id} as {@link FandomImage}.
	 * @param id the file ID
	 * @return the image as FandomImage
	 * @throws NotFoundException if the article couldn't be found
	 * @throws ReadingException if the content couldn't be read
	 * @see #getImage(String)
	 * @see #getPage(long)
	 */
	public FandomImage getImage(long id) {
		return new FandomImage(id, this);
	}
	
	/**
	 * Returns the discussions thread containing the post with ID {@code id} as {@link FandomThread}.
	 * @param postId the post id
	 * @return the thread as FandomThread
	 * @see #getDiscussionThread(long)
	 * @see #getDiscussionPost(long)
	 * @see #getPage(long)
	 */
	public FandomThread getDiscussionThreadByPostId(long postId) {
		return FandomThread.getThreadByPostId(postId, this);
	}
	
	/**
	 * Returns the discussions post with ID {@code id} as {@link FandomPost}.
	 * @param id the post id
	 * @return the post as FandomPost
	 * @see #getDiscussionThreadByPostId(long)
	 * @see #getDiscussionThread(long)
	 * @see #getPage(long)
	 */
	public FandomPost getDiscussionPost(long id) {
		return FandomPost.getPostById(id, this);
	}
	
	/**
	 * Returns the discussions thread with ID {@code id} as {@link FandomThread}.
	 * @param id the thread id
	 * @return the thread as FandomThread
	 * @see #getDiscussionThreadByPostId(long)
	 * @see #getDiscussionPost(long)
	 * @see #getPage(long)
	 */
	public FandomThread getDiscussionThread(long id) {
		return new FandomThread(id, this);
	}
	
	/**
	 * @return a {@link FandomActivity.FandomActivity} instance
	 */
	public FandomActivity getActivity() {
		return new FandomActivity(this);
	}
	
	/**
	 * Returns the namespaces as {@link Element}-Array.
	 * <p><b>Note:</b> This does not update the namespace constants (like {@link #NAMESPACE_MAIN}).
	 * <p>If you're unfamiliar with namespaces, check <a href="https://community.fandom.com/wiki/Help:Namespace">community.fandom.com/wiki/Help:Namespace</a>.
	 * @return the namespaces as Element-Array with the namespace ID as key and the namespace title as value
	 * @see #NAMESPACE_MAIN
	 * @see #NAMESPACE_SPECIAL
	 * @see #NAMESPACE_USER
	 * @see #NAMESPACE_CATEGORY
	 * @see #NAMESPACE_FILE
	 */
	public Element[] getNamespaces() {
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + baseUrl + "/api.php?action=query&meta=siteinfo&siprop=namespaces&format=json")).getJSONObject("query").getJSONObject("namespaces");
		ArrayList<Element> elements = new ArrayList<>();
		Iterator<String> iterator = jsonObject.keys();
		while(iterator.hasNext()) {
			String key = iterator.next();
			String value = jsonObject.getJSONObject(key).getString("*");
			elements.add(new Element(key, value));
		}
		Element[] elementsArray = new Element[elements.size()];
		for(int i = 0; i<elements.size(); i++) elementsArray[i] = elements.get(i);
		return elementsArray;
	}
	
	/**
	 * Returns a random page ID with namespace {@code namespace}.<br>
	 * If you're unfamiliar with namespaces, check <a href="https://community.fandom.com/wiki/Help:Namespace">community.fandom.com/wiki/Help:Namespace</a>.
	 * @param namespace the namespace
	 * @return a random page ID
	 * @see #getRandomPageId()
	 * @see #NAMESPACE_MAIN
	 * @see #NAMESPACE_SPECIAL
	 * @see #NAMESPACE_USER
	 */
	public long getRandomPageId(int namespace) {
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + baseUrl + "/api.php?action=query&list=random&format=json&rnnamespace=" + namespace)).getJSONObject("query");
		return jsonObject.getJSONArray("random").getJSONObject(0).getLong("id");
	}
	
	/**
	 * Returns a random page ID of any namespace.
	 * @return a random page ID
	 * @see #getRandomPageId(int)
	 */
	public long getRandomPageId() {
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + baseUrl + "/api.php?action=query&list=random&format=json")).getJSONObject("query");
		return jsonObject.getJSONArray("random").getJSONObject(0).getLong("id");
	}
	
	/**
	 * Returns the time zone of the FANDOM.
	 * @return time zone abbreviations
	 */
	public String getTimeZone() {
		return timeZone;
	}
	
	/**
	 * Returns the generator.
	 * @return the generator
	 */
	public String getGenerator() {
		return generator;
	}
	
	/**
	 * Returns the PHP version.
	 * @return the PHP version
	 * @see #getPhpsAPI()
	 */
	public String getPhpVersion() {
		return phpVersion;
	}
	
	/**
	 * Returns the PHP API title.
	 * @return the title of the PHP API
	 * @see #getPhpsAPI()
	 */
	public String getPhpsAPI() {
		return phpsAPI;
	}
	
	/**
	 * Returns the database type.
	 * @return the type of the database
	 * @see #getDbVersion()
	 */
	public String getDbType() {
		return dbType;
	}
	
	/**
	 * Returns the database version.
	 * @return the version of the database
	 * @see #getDbType()
	 */
	public String getDbVersion(){
		return dbVersion;
	}
	
	/**
	 * Returns legal title characters as String.
	 * @return legal title characters
	 * @see #getIllegalUsernameChars()
	 */
	public String getLegalTitleChars() {
		return legalTitleChars;
	}
	
	/**
	 * Returns illegal username characters as String.
	 * @return illegal username characters
	 * @see #getLegalTitleChars()
	 */
	public String getIllegalUsernameChars() {
		return illegalUsernameChars;
	}
	
	/**
	 * Returns the maximum article size.
	 * @return the maximum article size
	 * @see #getMaxUploadSize()
	 * @see #getMinUploadChunkSize()
	 */
	public long getMaxArticleSize() {
		return maxArticleSize;
	}
	
	/**
	 * Returns the maximum upload size.
	 * @return the maximum upload size
	 * @see #getMaxArticleSize()
	 * @see #getMinUploadChunkSize()
	 */
	public long getMaxUploadSize() {
		return maxUploadSize;
	}
	
	/**
	 * Returns the minimum upload chunk size.
	 * @return the minimum upload chunk size
	 * @see #getMaxArticleSize()
	 * @see #getMaxUploadSize()
	 */
	public long getMinUploadChunkSize() {
		return minUploadChunkSize;
	}
	
	/**
	 * Returns the URL of the main page.
	 * @return the main page URL
	 * @see #getMainPageUrl()
	 */
	public String getUrl() {
		return getMainPageUrl();
	}
	
	@Override
	public String toString() {
		return getUrl();
	}
	
	/**
	 * Sends out a warning and logs it, if {@link #PRINT_WARNINGS} is {@code true}.
	 * @param text the text
	 */
	static public void warn(String text) {
		if(PRINT_WARNINGS) System.err.println("[WARNING] " + text);
	}

}

package FandomActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import Fandom.Fandom;
import Fandom.FandomPage;
import Fandom.FandomParser;
import Fandom.FandomUser;
import Fandom.InvalidFormatException;
import Fandom.Navigator;
import Fandom.ReadingException;

/**
 * Can be used to receive information about Fandom activities like recent changes or forum posts.
 * @author Mika Thein
 * @see #FandomActivity(Fandom)
 * @see Fandom
 */
public class FandomActivity {
	
	/**
	 * The Fandom.
	 */
	public final Fandom fandom;
	
	private ArrayList<FandomActivityListener> listener = new ArrayList<>();
	
	private String last = "";
	
	private boolean hasListenerThread = false;
	
	/**
	 * The storage file is used to store IDs of recent activities to prevent triggering listeners for multiple times due to the same event.
	 */
	static public File storageFile = new File("./strg/storage.json");
	static {{
		try {
			storageFile = new File(FandomActivity.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString().split(":", 2)[1] + "/JFandomAPI_Storage/storage.json");
		} catch(Exception e) { e.printStackTrace(); }
	}}
	
	/**
	 * Creates a new FandomActivity instance.
	 * @param fandom the {@link Fandom}
	 * @see #getRecentChanges(int)
	 * @see #getRecentChanges(FandomUser, FandomPage, int)
	 * @see #getRecentForumPosts(int)
	 * @see #getRecentWallPosts(int)
	 * @see #getRecentArticleComments(int)
	 */
	public FandomActivity(Fandom fandom) {
		this.fandom = fandom;
	}
	
	/**
	 * @param limit the max amount of recent changes (0 to 100)
	 * @see #getRecentChanges(FandomUser, int)
	 * @see #getRecentChanges(FandomPage, int)
	 * @see #getRecentChanges(FandomUser, FandomPage, int)
	 * @return {@code limit} recent changes
	 * @throws IOException if the API is unreachable
	 */
	public FandomRecentChange[] getRecentChanges(int limit) throws IOException {
		return getRecentChanges(null, null, limit);
	}
	
	/**
	 * @param author the Fandom user
	 * @param limit the max amount of recent changes (0 to 100)
	 * @see #getRecentChanges(int)
	 * @see #getRecentChanges(FandomPage, int)
	 * @see #getRecentChanges(FandomUser, FandomPage, int)
	 * @return {@code limit} recent changes by {@code author}
	 * @throws IOException if the API is unreachable
	 */
	public FandomRecentChange[] getRecentChanges(FandomUser author, int limit) throws IOException {
		return getRecentChanges(author, null, limit);
	}
	
	/**
	 * @param page the Fandom page
	 * @param limit the max amount of recent changes (0 to 100)
	 * @see #getRecentChanges(int)
	 * @see #getRecentChanges(FandomUser, int)
	 * @see #getRecentChanges(FandomUser, FandomPage, int)
	 * @return {@code limit} recent changes on {@code page}
	 * @throws IOException if the API is unreachable
	 */
	public FandomRecentChange[] getRecentChanges(FandomPage page, int limit) throws IOException {
		return getRecentChanges(null, page, limit);
	}
	
	/**
	 * @param author the Fandom user
	 * @param page the Fandom page
	 * @param limit the max amount of recent changes (0 to 100)
	 * @return {@code limit} recent changes by {@code author} on {@code page}
	 * @throws IOException if the API is unreachable
	 */
	public FandomRecentChange[] getRecentChanges(FandomUser author, FandomPage page, int limit) throws IOException {
		return getRecentChanges(author, page, null, limit);
	}
	
	/**
	 * Besides {@code limit}, each parameter can be set to {@code null} to be ignored.
	 * @param author the Fandom user
	 * @param page the Fandom page
	 * @param latestTimestamp the latest timestamp (inclusive, stops generating any earlier changes)
	 * @param limit the max amount of recent changes (0 to 100)
	 * @return {@code limit} recent changes by {@code author} on {@code page} since {@code timestamp}
	 * @throws IOException if the API is unreachable
	 */
	public FandomRecentChange[] getRecentChanges(FandomUser author, FandomPage page, String latestTimestamp, int limit) throws IOException {
		JSONArray jsonArray = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=query&list=recentchanges&rcprop=title|ids|sizes|flags|user|timestamp|comment|tags|loginfo" + (author != null ? "&rcuser=" + FandomParser.toURL(author.username, true) : "") + (page != null ? "&rctitle=" + FandomParser.toURL(page.title, true) : "") + (latestTimestamp != null && latestTimestamp.length() > 0 ? "&rcend=" + FandomParser.toURL(latestTimestamp, true) : "") + "&rclimit=" + limit + "&format=json")).getJSONObject("query").getJSONArray("recentchanges");
		ArrayList<FandomRecentChange> recentChanges = new ArrayList<>();
		for(int i = 0; i<jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String user = jsonObject.optString("user"),
					title = jsonObject.optString("title"),
					comment = jsonObject.optString("comment"),
					timestamp = jsonObject.optString("timestamp");
			String[] tags = jsonArrayToArray(jsonObject.getJSONArray("tags"));
			long id = jsonObject.optLong("rcid"),
					newRevisionId = jsonObject.optLong("revid"),
					oldRevisionId = jsonObject.optLong("old_revid"),
					pageId = jsonObject.optLong("pageid");
			if(jsonObject.getString("type").equals("edit")) {
				long oldLen = jsonObject.getLong("oldlen");
				long newLen = jsonObject.getLong("newlen");
				recentChanges.add(new FandomEdit(user, title, comment, tags, timestamp, oldLen, newLen, id, newRevisionId, oldRevisionId, pageId, fandom));
			} else if(jsonObject.getString("type").equals("log")) {
				long logId = jsonObject.getLong("logid");
				long revisionId = jsonObject.getLong("revid");
				int action = FandomLog.toActionInt(jsonObject.getString("logaction"));
				switch(jsonObject.getString("logtype")) {
					case "upload":
						String imgSha1 = jsonObject.getJSONObject("logparams").getString("img_sha1");
						recentChanges.add(new FandomLogUpload(user, title, comment, tags, imgSha1, timestamp, id, logId, action, revisionId, pageId, fandom));
						break;
					case "move":
						String targetTitle = jsonObject.getJSONObject("logparams").getString("target_title");
						recentChanges.add(new FandomLogMove(user, title, comment, tags, targetTitle, timestamp, id, logId, action, revisionId, pageId, fandom));
						break;
					case "delete":
						recentChanges.add(new FandomLogDelete(user, title, comment, tags, timestamp, id, logId, action, pageId, fandom));
						break;
					case "protect":
						String description = jsonObject.getJSONObject("logparams").getString("description");
						JSONArray jsonArrayDetails = jsonObject.getJSONObject("logparams").getJSONArray("details");
						FandomLogProtectDetail[] details = new FandomLogProtectDetail[jsonArrayDetails.length()];
						for(int j = 0; j<details.length; j++) {
							int type = FandomLogProtectDetail.toTypeInt(jsonArrayDetails.getJSONObject(j).getString("type"));
							String level = jsonArrayDetails.getJSONObject(j).getString("level");
							String expiry = jsonArrayDetails.getJSONObject(j).getString("expiry");
							details[j] = new FandomLogProtectDetail(type, level, expiry);
						}
						recentChanges.add(new FandomLogProtect(user, title, description, tags, details, timestamp, id, logId, action, revisionId, pageId, fandom));
						break;
					case "block":
						String duration = jsonObject.getJSONObject("logparams").getString("duration");
						String[] flags = new String[jsonObject.getJSONObject("logparams").getJSONArray("flags").length()];
						jsonArrayDetails = jsonObject.getJSONObject("logparams").getJSONArray("details");
						details = new FandomLogProtectDetail[jsonArrayDetails.length()];
						for(int j = 0; j<details.length; j++) flags[j] = jsonObject.getJSONObject("logparams").getJSONArray("flags").getString(j);
						String expiry = "NEVER";
						if(jsonObject.getJSONObject("logparams").has("expiry")) expiry = jsonObject.getJSONObject("logparams").getString("expiry");
						recentChanges.add(new FandomLogBlock(user, title, comment, tags, timestamp, duration, expiry, flags, id, logId, action, revisionId, pageId, fandom));
						break;
				}
			} else if(jsonObject.getString("type").equals("new")) {
				long len = jsonObject.optLong("newlen");
				recentChanges.add(new FandomNew(user, title, comment, tags, timestamp, len, id, newRevisionId, pageId, fandom));
			} else {
				recentChanges.add(new FandomUnknown(user, title, comment, tags, timestamp, id, newRevisionId, oldRevisionId, pageId, fandom));
			}
		}
		FandomRecentChange[] recentChangesArray = new FandomRecentChange[recentChanges.size()];
		for(int i = 0; i<recentChangesArray.length; i++) recentChangesArray[i] = recentChanges.get(recentChanges.size()-1-i);
		return recentChangesArray;
	}
	
	/**
	 * @param limit the max amount of forum posts (0 to 100)
	 * @see #getRecentForumPosts(FandomUser, int)
	 * @see #getRecentWallPosts(int)
	 * @see #getRecentArticleComments(int)
	 * @see #getRecentPosts(int, int, int)
	 * @return recent forum posts
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public FandomPost[] getRecentForumPosts(int limit) throws MalformedURLException, IOException {
		return getRecentForumPosts(null, limit);
	}
	
	/**
	 * @param author the author
	 * @param limit the max amount of forum posts (0 to 100)
	 * @see #getRecentForumPosts(int)
	 * @see #getRecentWallPosts(FandomUser, int)
	 * @see #getRecentArticleComments(FandomUser, int)
	 * @see #getRecentPosts(FandomUser, int, int, int)
	 * @return recent forum posts by {@code author}
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public FandomPost[] getRecentForumPosts(FandomUser author, int limit) throws MalformedURLException, IOException {
		if(limit < 1 || limit > 100) throw new InvalidFormatException("The limit has to be between 1 and 100 (inclusive).");
		JSONArray jsonArray = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/wikia.php?controller=DiscussionPost&method=getPosts&containerType=FORUM&limit=" + limit + (author != null ? "&userId=" + author.id : "") + "&format=json")).getJSONObject("_embedded").getJSONArray("doc:posts");
		ArrayList<FandomPost> posts = new ArrayList<>();
		for(int i = 0; i<jsonArray.length(); i++) {
			try {
				posts.add(FandomPost.getPostById(Long.parseLong(jsonArray.getJSONObject(i).getString("id")), fandom));
			} catch(Exception e) {}
		}
		FandomPost[] postsArray = new FandomPost[posts.size()];
		for(int i = 0; i<postsArray.length; i++) postsArray[i] = posts.get(posts.size()-1-i);
		return postsArray;
	}
	
	/**
	 * @param limit the max amount of wall posts (0 to 100)
	 * @see #getRecentWallPosts(FandomUser, int)
	 * @see #getRecentForumPosts(int)
	 * @see #getRecentArticleComments(int)
	 * @see #getRecentPosts(int, int, int)
	 * @return recent wall posts
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public FandomPost[] getRecentWallPosts(int limit) throws MalformedURLException, IOException {
		return getRecentWallPosts(null, limit);
	}
	
	/**
	 * @param author the author
	 * @param limit the max amount of wall posts (0 to 100)
	 * @see #getRecentWallPosts(int)
	 * @see #getRecentForumPosts(FandomUser, int)
	 * @see #getRecentArticleComments(FandomUser, int)
	 * @see #getRecentPosts(FandomUser, int, int, int)
	 * @return recent wall posts by {@code author}
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public FandomPost[] getRecentWallPosts(FandomUser author, int limit) throws MalformedURLException, IOException {
		if(limit < 1 || limit > 100) throw new InvalidFormatException("The limit has to be between 1 and 100 (inclusive).");
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/wikia.php?controller=DiscussionPost&method=getPosts&containerType=WALL&limit=" + limit + (author != null ? "&userId=" + author.id : "") + "&format=json")).getJSONObject("_embedded");
		JSONArray jsonArray = jsonObject.getJSONArray("wallOwners");
		HashMap<String, Long> wallOwners = new HashMap<>();
		for(int i = 0; i<jsonArray.length(); i++) {
			wallOwners.put(jsonArray.getJSONObject(i).getString("wallContainerId"), Long.parseLong(jsonArray.getJSONObject(i).getString("userId")));
		}
		jsonArray = jsonObject.getJSONArray("doc:posts");
		ArrayList<FandomPost> posts = new ArrayList<>();
		for(int i = 0; i<jsonArray.length(); i++) {
			try {
				jsonObject = jsonArray.getJSONObject(i);
				JSONObject createdBy = jsonObject.getJSONObject("createdBy");
				FandomPostAuthor postAuthor = new FandomPostAuthor(createdBy.getString("name"), Long.parseLong(createdBy.getString("id")), !createdBy.isNull("avatarUrl") ? createdBy.getString("avatarUrl") : null);
				int postPosition = jsonObject.getInt("position"),
						upvoteCount = jsonObject.getInt("upvoteCount");
				long epochSecond = jsonObject.getJSONObject("creationDate").getLong("epochSecond"),
						lastRevId = Long.parseLong(jsonObject.getString("latestRevisionId")),
						siteId = Long.parseLong(jsonObject.getString("siteId")),
						id = Long.parseLong(jsonObject.getString("id")),
						threadId = Long.parseLong(jsonObject.getString("threadId")),
						wallId = Long.parseLong(jsonObject.getString("forumId")),
						wallOwner = wallOwners.get(wallId + "");
				String rawContent = !jsonObject.isNull("rawContent") ? jsonObject.getString("rawContent") : null,
						title = !jsonObject.isNull("title") ? jsonObject.getString("title") : null,
						wallName = jsonObject.getString("forumName");
				posts.add(new FandomWallPost(title, rawContent, id, siteId, lastRevId, postAuthor, wallName, wallId, wallOwner, epochSecond, postPosition, upvoteCount, new FandomThread.Builder(threadId, fandom)));
			} catch(Exception e) {}
		}
		FandomPost[] postsArray = new FandomPost[posts.size()];
		for(int i = 0; i<postsArray.length; i++) postsArray[i] = posts.get(posts.size()-1-i);
		return postsArray;
	}
	
	/**
	 * @param limit the max amount of article comments (0 to 100)
	 * @see #getRecentArticleComments(FandomUser, int)
	 * @see #getRecentForumPosts(int)
	 * @see #getRecentWallPosts(int)
	 * @see #getRecentPosts(int, int, int)
	 * @return recent article comments
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public FandomPost[] getRecentArticleComments(int limit) throws MalformedURLException, IOException {
		return getRecentArticleComments(null, limit);
	}
	
	/**
	 * @param author the author
	 * @param limit the max amount of article comments (0 to 100)
	 * @see #getRecentArticleComments(int)
	 * @see #getRecentForumPosts(FandomUser, int)
	 * @see #getRecentWallPosts(FandomUser, int)
	 * @see #getRecentPosts(FandomUser, int, int, int)
	 * @return recent article comments by {@code author}
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public FandomPost[] getRecentArticleComments(FandomUser author, int limit) throws MalformedURLException, IOException {
		if(limit < 1 || limit > 100) throw new InvalidFormatException("The limit has to be between 1 and 100 (inclusive).");
		JSONArray jsonArray = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/wikia.php?controller=DiscussionPost&method=getPosts&containerType=ARTICLE_COMMENT&limit=" + limit + (author != null ? "&userId=" + author.id : "") + "&format=json")).getJSONObject("_embedded").getJSONArray("doc:posts");
		ArrayList<FandomPost> posts = new ArrayList<>();
		for(int i = 0; i<jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				JSONObject createdBy = jsonObject.getJSONObject("createdBy");
				FandomPostAuthor postAuthor = new FandomPostAuthor(createdBy.getString("name"), Long.parseLong(createdBy.getString("id")), !createdBy.isNull("avatarUrl") ? createdBy.getString("avatarUrl") : null);
				int upvoteCount = jsonObject.getInt("upvoteCount"),
						postPosition = jsonObject.getInt("position");
				long epochSecond = jsonObject.getJSONObject("creationDate").getLong("epochSecond"),
						lastRevId = Long.parseLong(jsonObject.getString("latestRevisionId")),
						siteId = Long.parseLong(jsonObject.getString("siteId")),
						id = Long.parseLong(jsonObject.getString("id")),
						threadId = Long.parseLong(jsonObject.getString("threadId")),
						containerId = Long.parseLong(jsonObject.getString("forumId"));
				String rawContent = !jsonObject.isNull("rawContent") ? jsonObject.getString("rawContent") : null,
						title = !jsonObject.isNull("title") ? jsonObject.getString("title") : null,
						funnel = jsonObject.getString("funnel");
				posts.add(new FandomArticleComment(title, rawContent, id, siteId, lastRevId, containerId, postAuthor, funnel, epochSecond, postPosition, upvoteCount, new FandomThread.Builder(threadId, fandom)));
			} catch(Exception e) { e.printStackTrace(); }
		}
		FandomPost[] postsArray = new FandomPost[posts.size()];
		for(int i = 0; i<postsArray.length; i++) postsArray[i] = posts.get(posts.size()-1-i);
		return postsArray;
	}
	
	/**
	 * @param limitForum the max amount of forum posts (0 to 100)
	 * @param limitWall the max amount of wall posts (0 to 100)
	 * @param limitArticleComments the max amount of article comments (0 to 100)
	 * @see #getRecentPosts(FandomUser, int, int, int)
	 * @see #getRecentForumPosts(int)
	 * @see #getRecentWallPosts(int)
	 * @see #getRecentArticleComments(int)
	 * @return recent posts
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public FandomPost[] getRecentPosts(int limitForum, int limitWall, int limitArticleComments) throws MalformedURLException, IOException {
		FandomPost[] recentForumActivities = getRecentForumPosts(limitForum), recentWallActivities = getRecentWallPosts(limitWall), recentArticleActivities = getRecentArticleComments(limitArticleComments);
		ArrayList<FandomPost> all = new ArrayList<>();
		for(FandomPost p : recentForumActivities) {
			for(int i = 0; i<all.size(); i++) {
				if(Instant.parse(all.get(i).timestamp).getEpochSecond() > Instant.parse(p.timestamp).getEpochSecond()) {
					all.add(i, p);
					break;
				}
			}
		} for(FandomPost p : recentWallActivities) {
			for(int i = 0; i<all.size(); i++) {
				if(Instant.parse(all.get(i).timestamp).getEpochSecond() > Instant.parse(p.timestamp).getEpochSecond()) {
					all.add(i, p);
					break;
				}
			}
		} for(FandomPost p : recentArticleActivities) {
			for(int i = 0; i<all.size(); i++) {
				if(Instant.parse(all.get(i).timestamp).getEpochSecond() > Instant.parse(p.timestamp).getEpochSecond()) {
					all.add(i, p);
					break;
				}
			}
		} FandomPost[] posts = new FandomPost[all.size()];
		for(int i = 0; i<posts.length; i++) posts[i] = all.get(i);
		return posts;
	}
	
	/**
	 * @param author the author
	 * @param limitForum the max amount of forum posts (0 to 100)
	 * @param limitWall the max amount of wall posts (0 to 100)
	 * @param limitArticleComments the max amount of article comments (0 to 100)
	 * @see #getRecentPosts(int, int, int)
	 * @see #getRecentForumPosts(FandomUser, int)
	 * @see #getRecentWallPosts(FandomUser, int)
	 * @see #getRecentArticleComments(FandomUser, int)
	 * @return recent posts by {@code author}
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public FandomPost[] getRecentPosts(FandomUser author, int limitForum, int limitWall, int limitArticleComments) throws MalformedURLException, IOException {
		FandomPost[] recentForumActivities = getRecentForumPosts(author, limitForum), recentWallActivities = getRecentWallPosts(author, limitWall), recentArticleActivities = getRecentArticleComments(author, limitArticleComments);
		ArrayList<FandomPost> all = new ArrayList<>();
		for(FandomPost p : recentForumActivities) {
			for(int i = 0; i<all.size(); i++) {
				if(Instant.parse(all.get(i).timestamp).getEpochSecond() > Instant.parse(p.timestamp).getEpochSecond()) {
					all.add(i, p);
					break;
				}
			}
		} for(FandomPost p : recentWallActivities) {
			for(int i = 0; i<all.size(); i++) {
				if(Instant.parse(all.get(i).timestamp).getEpochSecond() > Instant.parse(p.timestamp).getEpochSecond()) {
					all.add(i, p);
					break;
				}
			}
		} for(FandomPost p : recentArticleActivities) {
			for(int i = 0; i<all.size(); i++) {
				if(Instant.parse(all.get(i).timestamp).getEpochSecond() > Instant.parse(p.timestamp).getEpochSecond()) {
					all.add(i, p);
					break;
				}
			}
		} FandomPost[] posts = new FandomPost[all.size()];
		for(int i = 0; i<posts.length; i++) posts[i] = all.get(i);
		return posts;
	}
	
	private String getLatestRecentActivityTimestamp() throws FileNotFoundException, IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(storageFile));
		return new JSONObject(bufferedReader.readLine()).getJSONObject(fandom.baseUrl).optString("last");
	}
	
	private void addFandom() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(storageFile));
		String lines = "";
		for(String l; (l = bufferedReader.readLine()) != null; lines += l);
		bufferedReader.close();
		if(!new JSONObject(lines).has(fandom.baseUrl)) {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(storageFile));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("last", last);
			JSONObject total = new JSONObject(lines).put(fandom.baseUrl, jsonObject);
			bufferedWriter.write(total.toString());
			bufferedWriter.close();
		}
	}
	
	private void saveLastValues() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(FandomActivity.storageFile));
		String lines = "";
		for(String l; (l = bufferedReader.readLine()) != null; lines += l);
		bufferedReader.close();
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FandomActivity.storageFile));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("last", last);
		JSONObject total = new JSONObject(lines).put(fandom.baseUrl, jsonObject);
		bufferedWriter.write(total.toString());
		bufferedWriter.close();
	}
	
	/**
	 * Adds a listener to the watchlist. All listeners will be notified whenever a new activity was registered.<br>
	 * If {@link Fandom#RESET_STORAGE} is {@code false}, these listeners will be notified for events they may already have been notified for before.
	 * @param l the listener
	 */
	public void addListener(FandomActivityListener l) {
		listener.add(l);
		if(!hasListenerThread) {
			try {
				addFandom();
			} catch(Exception e) {
				if(Fandom.DEBUG) e.printStackTrace();
				throw new RuntimeException("The Fandom cannot be added to the storage. (Set Fandom.DEBUG to true to print the exception stacktrace.)");
			} try {
				last = getLatestRecentActivityTimestamp();
				hasListenerThread = true;
				ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
				scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						if(listener.size() > 0) {
							try {
								FandomRecentChange[] recentChanges = getRecentChanges(null, null, last, 30);
								FandomPost[] recentPosts = getRecentPosts(15, 15, 15);
								ArrayList<FandomActivityElement> all = new ArrayList<>();
								for(FandomRecentChange r : recentChanges) all.add(r);
								for(FandomPost p : recentPosts) {
									for(int i = 0; i<all.size(); i++) {
										if(Instant.parse(all.get(i).timestamp).getEpochSecond() > Instant.parse(p.timestamp).getEpochSecond()) {
											all.add(i, p);
											break;
										}
									}
								}
								ArrayList<Thread> threads = new ArrayList<>();
								for(FandomActivityElement e : all) {
									if(last.length() > 0 && Instant.parse(last).getEpochSecond() > Instant.parse(e.timestamp).getEpochSecond()) break;
									if(e instanceof FandomRecentChange) {
										FandomRecentChange recentChange = (FandomRecentChange) e;
										for(FandomActivityListener recentChangesListener : listener) {
											if(recentChangesListener instanceof FandomActivityAdapter && ((FandomActivityAdapter) recentChangesListener).earliestTimestamp.length() > 0 && Instant.parse(((FandomActivityAdapter) recentChangesListener).earliestTimestamp).getEpochSecond() > Instant.parse(e.timestamp).getEpochSecond()) continue;
											Thread t = new Thread(() -> {
												recentChangesListener.recentChangeHappened(recentChange);
												recentChangesListener.somethingHappened(recentChange);
											});
											t.start();
											threads.add(t);
										}
									} else if(e instanceof FandomPost) {
										FandomPost recentPost = (FandomPost) e;
										for(FandomActivityListener recentChangesListener : listener) {
											if(recentChangesListener instanceof FandomActivityAdapter && ((FandomActivityAdapter) recentChangesListener).earliestTimestamp.length() > 0 && Instant.parse(((FandomActivityAdapter) recentChangesListener).earliestTimestamp).getEpochSecond() > Instant.parse(e.timestamp).getEpochSecond()) continue;
											Thread t = new Thread(() -> {
												recentChangesListener.postHappened(recentPost);
												recentChangesListener.somethingHappened(recentPost);
											});
											t.start();
											threads.add(t);
										}
									}
									for(Thread t : threads) t.join(2000);
									threads.clear();
								}
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
								last = all.size() > 0 ? format.format(new Date(format.parse(all.get(all.size()-1).timestamp).getTime()+1000l)) : last;
								saveLastValues();
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
					}
				}, 1, 10, TimeUnit.SECONDS);
			} catch(Exception e) {
				if(Fandom.DEBUG) e.printStackTrace();
				throw new ReadingException("Can't read recent activities (set static Fandom.DEBUG to true to print the exact exception stack trace)");
			}
		}
	}
	
	static private String[] jsonArrayToArray(JSONArray jsonArray) {
		String[] array = new String[jsonArray.length()];
		for(int i = 0; i<array.length; i++) {
			array[i] = jsonArray.getString(i);
		} return array;
	}

}

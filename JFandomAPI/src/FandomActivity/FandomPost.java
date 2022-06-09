package FandomActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import Fandom.Fandom;
import Fandom.Navigator;

/**
 * Represents a Fandom post.
 * @author Mika Thein
 * @see FandomActivity#getRecentForumPosts(int)
 * @see FandomActivity#getRecentWallPosts(int)
 * @see FandomActivity#getRecentArticleComments(int)
 */
public class FandomPost extends FandomActivityElement {
	
	/**
	 * The author.
	 */
	public final FandomPostAuthor author;
	/**
	 * The ID.
	 */
	public final long id;
	/**
	 * The site ID.
	 */
	public final long siteId;
	/**
	 * The position (within the thread).
	 */
	public final int position;
	/**
	 * The amount of upvotes.
	 */
	public final int upvoteCount;
	private FandomThread thread = null;
	private FandomThread.Builder threadBuilder = null;
	
	/**
	 * Creates a new FandomPost instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity} instead.</b>
	 * @param title the title
	 * @param rawContent the content in plain text
	 * @param id the ID
	 * @param siteId the site ID
	 * @param latestRevId the latest revision ID
	 * @param author the author
	 * @param epochSecond the epoch seconds
	 * @param position the position
	 * @param upvoteCount the amount of upvotes
	 * @param threadBuilder the thread builder
	 */
	public FandomPost(String title, String rawContent, long id, long siteId, long latestRevId, FandomPostAuthor author, long epochSecond, int position, int upvoteCount, FandomThread.Builder threadBuilder) {
		super(author.name, title, rawContent, getSimpleDateFormat(threadBuilder.fandom).format(new Date(epochSecond * 1000)), latestRevId, threadBuilder.fandom);
		this.id = id;
		this.siteId = siteId;
		this.author = author;
		this.position = position;
		this.upvoteCount = upvoteCount;
		this.threadBuilder = threadBuilder;
	}
	
	/**
	 * Creates a new FandomPost instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity} instead.</b>
	 * @param title the title
	 * @param rawContent the content in plain text
	 * @param id the ID
	 * @param siteId the site ID
	 * @param latestRevId the latest revision ID
	 * @param author the author
	 * @param epochSecond the epoch seconds
	 * @param position the position
	 * @param upvoteCount the amount of upvotes
	 * @param thread the thread
	 */
	public FandomPost(String title, String rawContent, long id, long siteId, long latestRevId, FandomPostAuthor author, long epochSecond, int position, int upvoteCount, FandomThread thread) {
		super(author.name, title, rawContent, getSimpleDateFormat(thread.fandom).format(new Date(epochSecond * 1000)), latestRevId, thread.fandom);
		this.id = id;
		this.siteId = siteId;
		this.author = author;
		this.position = position;
		this.upvoteCount = upvoteCount;
		this.thread = thread;
	}
	
	/**
	 * @return the thread
	 */
	public FandomThread getThread() {
		if(thread != null) return thread;
		else if(threadBuilder != null) return threadBuilder.build();
		else return null;
	}
	
	/**
	 * Returns a Fandom forum post by its ID.<br>
	 * Currently, only {@link FandomPoll}s, {@link FandomReply}s and {@link FandomPost} (at the forum) can be retrieved!
	 * @param postId the post ID
	 * @param fandom the Fandom
	 * @return the Fandom post as FandomPoll, FandomReply or FandomPost
	 */
	public static FandomPost getPostById(long postId, Fandom fandom) { // https://avatar.fandom.com/de/wikia.php?controller=DiscussionPost&method=getPost&postId=4400000000000125140&format=json
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/wikia.php?controller=DiscussionPost&method=getPost&postId=" + postId + "&format=json"));
		JSONObject createdBy = jsonObject.getJSONObject("createdBy");
		FandomPostAuthor author = new FandomPostAuthor(createdBy.getString("name"), Long.parseLong(createdBy.getString("id")), !createdBy.isNull("avatarUrl") ? createdBy.getString("avatarUrl") : null);
		long epochSecond = jsonObject.getJSONObject("creationDate").getLong("epochSecond");
		long lastRevId = Long.parseLong(jsonObject.getString("latestRevisionId"));
		int postPosition = jsonObject.getInt("position");
		String rawContent = !jsonObject.isNull("rawContent") ? jsonObject.getString("rawContent") : null;
		long siteId = Long.parseLong(jsonObject.getString("siteId"));
		long threadId = Long.parseLong(jsonObject.getString("threadId"));
		String title = !jsonObject.isNull("title") ? jsonObject.getString("title") : null;
		int upvoteCount = jsonObject.getInt("upvoteCount");
		if(jsonObject.has("poll")) {
			JSONObject pollObject = jsonObject.getJSONObject("poll");
			int totalVotes = pollObject.getInt("totalVotes");
			long pollId = pollObject.getLong("id");
			ArrayList<FandomPollChoice> choices = new ArrayList<>();
			JSONArray choicesArray = pollObject.getJSONArray("answers");
			for(int i = 0; i<choicesArray.length(); i++) {
				JSONObject choiceObject = choicesArray.getJSONObject(i);
				choices.add(new FandomPollChoice(choiceObject.getLong("id"), !choiceObject.isNull("text") ? choiceObject.getString("text") : null, choiceObject.getInt("votes"), null, !choiceObject.isNull("image") ? choiceObject.getString("image") : null, choiceObject.getInt("position")));
			}
			return new FandomPoll(title, rawContent, postId, siteId, lastRevId, author, epochSecond, postPosition, upvoteCount, new FandomThread.Builder(threadId, fandom), choices, pollId, totalVotes);
		} else if(postPosition > 1) {
			return new FandomReply(rawContent, postId, siteId, lastRevId, author, epochSecond, postPosition, upvoteCount, new FandomThread.Builder(threadId, fandom));
		} return new FandomPost(title, rawContent, postId, siteId, lastRevId, author, epochSecond, postPosition, upvoteCount, new FandomThread.Builder(threadId, fandom));
	}
	
	private static SimpleDateFormat getSimpleDateFormat(Fandom fandom) {
		SimpleDateFormat a = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH:mm:ss'Z'");
		try {
			a.setTimeZone(TimeZone.getTimeZone(fandom.getTimeZone()));
		} catch(Exception e) { if(Fandom.DEBUG) e.printStackTrace(); }
		return a;
	}

}

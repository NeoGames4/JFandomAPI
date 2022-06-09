package FandomActivity;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import Fandom.Fandom;
import Fandom.Navigator;
import Fandom.NotFoundException;

/**
 * Represents a Fandom thread.
 * @author Mika Thein
 * @see #getThreadByPostId(long, Fandom)
 * @see FandomPost#getThread()
 * @see Builder
 */
public class FandomThread {
	
	private ArrayList<FandomPost> content = new ArrayList<>();
	private final String json;
	
	/**
	 * The creator of the thread.
	 */
	public final FandomPostAuthor author;
	/**
	 * The epoch seconds.
	 */
	public final long epochSecond;
	/**
	 * The forum ID.
	 */
	public final long forumId;
	/**
	 * The forum name.
	 */
	public final String forumName;
	/**
	 * The funnel.
	 */
	public final String funnel;
	/**
	 * The ID.
	 */
	public final long id;
	/**
	 * The title of the first post.
	 */
	public final String title;
	/**
	 * The trending score.
	 */
	public final BigDecimal trendingScore;
	/**
	 * The amount of upvotes.
	 */
	public final long upvoteCount;
	
	/**
	 * The Fandom.
	 */
	public final Fandom fandom;
	
	/**
	 * Creates a new FandomThread instance (should not be used manually!).<p>
	 * <b>Use {@link #FandomThread(long, Fandom)}, {@link #getThreadByPostId(long, Fandom)} or {@link FandomPost#getThread()} instead.</b>
	 * @param title the title
	 * @param id the ID
	 * @param author the author
	 * @param epochSecond the epoch seconds
	 * @param forumId the forum ID
	 * @param forumName the forum name
	 * @param funnel the funnel
	 * @param trendingScore the trending score
	 * @param upvoteCount the amount of upvotes
	 * @param content the content
	 * @param json the thread as JSON
	 * @param fandom the Fandom
	 */
	public FandomThread(String title, long id, FandomPostAuthor author, long epochSecond, long forumId, String forumName, String funnel, BigDecimal trendingScore, long upvoteCount, ArrayList<FandomPost> content, String json, Fandom fandom) {
		this.title = title;
		this.id = id;
		this.author = author;
		this.epochSecond = epochSecond;
		this.forumId = forumId;
		this.forumName = forumName;
		this.funnel = funnel;
		this.trendingScore = trendingScore;
		this.upvoteCount = upvoteCount;
		this.content = content;
		this.json = json;
		this.fandom = fandom;
	}
	
	/**
	 * Creates a new FandomThread instance.
	 * @param threadId the thread ID
	 * @param fandom the Fandom
	 * @see #getThreadByPostId(long, Fandom)
	 */
	public FandomThread(long threadId, Fandom fandom) {
		this.fandom = fandom;
		this.json = Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/wikia.php?controller=DiscussionThread&method=getThread&threadId=" + threadId + "&viewableOnly=true&limit=100");
		JSONObject jsonObject = new JSONObject(json);
		JSONObject createdBy = jsonObject.getJSONObject("createdBy");
		this.author = new FandomPostAuthor(createdBy.getString("name"), Long.parseLong(createdBy.getString("id")), !createdBy.isNull("avatarUrl") ? createdBy.getString("avatarUrl") : null);
		this.epochSecond = jsonObject.getJSONObject("creationDate").getLong("epochSecond");
		this.forumId = Long.parseLong(jsonObject.getString("forumId"));
		this.forumName = jsonObject.getString("forumName");
		this.funnel = jsonObject.getString("funnel");
		this.id = Long.parseLong(jsonObject.getString("id"));
		this.title = !jsonObject.isNull("title") ? jsonObject.getString("title") : null;
		this.trendingScore = jsonObject.getBigDecimal("trendingScore");
		this.upvoteCount = (long) jsonObject.getInt("upvoteCount");
		int postCount = jsonObject.getInt("postCount");
		for(int i = 1; i<=postCount+1; i++) content.add(buildPost(i, json, fandom));
	}
	
	/**
	 * Returns the FandomThread of a specific post.
	 * @param postId the post ID
	 * @param fandom the Fandom
	 * @see #FandomThread(long, Fandom)
	 * @return the thread of the given post
	 */
	public static FandomThread getThreadByPostId(long postId, Fandom fandom) {
		String json = Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/wikia.php?controller=DiscussionPermalink&method=getThreadByPostId&postId=" + postId + "&viewableOnly=true&limit=100");
		JSONObject jsonObject = new JSONObject(json);
		JSONObject createdBy = jsonObject.getJSONObject("createdBy");
		FandomPostAuthor author = new FandomPostAuthor(createdBy.getString("name"), Long.parseLong(createdBy.getString("id")), !createdBy.isNull("avatarUrl") ? createdBy.getString("avatarUrl") : null);
		long epochSecond = jsonObject.getJSONObject("creationDate").getLong("epochSecond");
		long forumId = Long.parseLong(jsonObject.getString("forumId"));
		String forumName = jsonObject.getString("forumName");
		String funnel = jsonObject.getString("funnel");
		long id = Long.parseLong(jsonObject.getString("id"));
		String title = !jsonObject.isNull("title") ? jsonObject.getString("title") : null;
		BigDecimal trendingScore = jsonObject.getBigDecimal("trendingScore");
		long upvoteCount = (long) jsonObject.getInt("upvoteCount");
		int postCount = jsonObject.getInt("postCount");
		ArrayList<FandomPost> content = new ArrayList<>();
		for(int i = 1; i<=postCount+1; i++) content.add(buildPost(i, json, fandom));
		return new FandomThread(title, id, author, epochSecond, forumId, forumName, funnel, trendingScore, upvoteCount, content, json, fandom);
	}
	
	private static FandomPost buildPost(int position, String json, Fandom fandom) {
		JSONObject jsonObject = null;
		if(position == 1) {
			jsonObject = new JSONObject(json).getJSONObject("_embedded").getJSONArray("firstPost").getJSONObject(0);
		} else {
			JSONArray jsonArray = new JSONObject(json).getJSONObject("_embedded").getJSONArray("doc:posts");
			for(int i = 0; i<jsonArray.length(); i++) {
				if(jsonArray.getJSONObject(i).getInt("position") == position) {
					jsonObject = jsonArray.getJSONObject(i);
					break;
				}
			}
		} if(jsonObject != null) {
			JSONObject createdBy = jsonObject.getJSONObject("createdBy");
			FandomPostAuthor author = new FandomPostAuthor(createdBy.getString("name"), Long.parseLong(createdBy.getString("id")), !createdBy.isNull("avatarUrl") ? createdBy.getString("avatarUrl") : null);
			long epochSecond = jsonObject.getJSONObject("creationDate").getLong("epochSecond");
			long id = Long.parseLong(jsonObject.getString("id"));
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
				return new FandomPoll(title, rawContent, id, siteId, lastRevId, author, epochSecond, postPosition, upvoteCount, new FandomThread.Builder(threadId, fandom), choices, pollId, totalVotes);
			} else if(position > 1) {
				return new FandomReply(rawContent, id, siteId, lastRevId, author, epochSecond, postPosition, upvoteCount, new FandomThread.Builder(threadId, fandom));
			} return new FandomPost(title, rawContent, id, siteId, lastRevId, author, epochSecond, postPosition, upvoteCount, new FandomThread.Builder(threadId, fandom));
		} return null;
	}
	
	/**
	 * @return the amount of posts within this thread
	 */
	public int getSize() {
		return content.size();
	}
	
	/**
	 * @see #get(int)
	 * @return the first 100 of posts of this thread
	 */
	public FandomPost[] getContent() {
		FandomPost[] posts = new FandomPost[content.size()];
		for(int i = 0; i<posts.length; i++) posts[i] = content.get(i);
		return posts;
	}
	
	/**
	 * @param position the position of a specific post
	 * @see #getContent()
	 * @return the Fandom post at the given position
	 */
	public FandomPost get(int position) {
		for(FandomPost p : content) {
			if(p.position == position) return p;
		} throw new NotFoundException("This thread does not contain a post with the position of " + position + " (size: " + getSize() + ").");
	}
	
	/**
	 * @return the thread as JSON
	 */
	public String asJSON() {
		return json;
	}
	
	@Override
	public String toString() {
		return asJSON();
	}
	
	/**
	 * A thread builder represents a thread without actually loading its data. It might be useful if the data of the thread is not required.
	 * @author Mika Thein
	 * @see FandomThread
	 */
	public static class Builder {
		
		/**
		 * The thread ID.
		 */
		public final long threadId;
		/**
		 * The Fandom.
		 */
		public final Fandom fandom;
		
		/**
		 * Creates a new Builder instance.
		 * @param threadId the thread ID
		 * @param fandom the Fandom
		 */
		public Builder(long threadId, Fandom fandom) {
			this.threadId = threadId;
			this.fandom = fandom;
		}
		
		/**
		 * @return the thread
		 */
		public FandomThread build() {
			return new FandomThread(threadId, fandom);
		}
		
	}

}

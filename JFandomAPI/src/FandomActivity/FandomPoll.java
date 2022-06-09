package FandomActivity;

import java.util.List;

/**
 * Represents a Fandom poll.
 * @author Mika Thein
 * @see FandomActivity#getRecentForumPosts(int)
 */
public class FandomPoll extends FandomPost {
	
	/**
	 * The poll choices.
	 */
	public final FandomPollChoice[] choices;
	/**
	 * The poll ID.
	 */
	public final long pollId;
	/**
	 * The amount of total votes.
	 */
	public final int totalVotes;
	
	/**
	 * Creates a new FandomPoll instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentForumPosts(int)} instead.</b>
	 * @param title the title
	 * @param rawContent the content in plain text
	 * @param id the ID
	 * @param siteId the site ID
	 * @param latestRevId the latest revision ID
	 * @param author the author
	 * @param epochSecond the epoch seconds
	 * @param position the position
	 * @param upvoteCount the upvote count
	 * @param threadBuilder the thread builder
	 * @param choices the choices
	 * @param pollId the poll ID
	 * @param totalVotes the amount of total votes
	 */
	public FandomPoll(String title, String rawContent, long id, long siteId, long latestRevId, FandomPostAuthor author, long epochSecond, int position, int upvoteCount, FandomThread.Builder threadBuilder, FandomPollChoice[] choices, long pollId, int totalVotes) {
		super(title, rawContent, id, siteId, latestRevId, author, epochSecond, position, upvoteCount, threadBuilder);
		this.choices = choices;
		this.pollId = pollId;
		this.totalVotes = totalVotes;
	}
	
	/**
	 * Creates a new FandomPoll instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentForumPosts(int)} instead.</b>
	 * @param title the title
	 * @param rawContent the content in plain text
	 * @param id the ID
	 * @param siteId the site ID
	 * @param latestRevId the latest revision ID
	 * @param author the author
	 * @param epochSecond the epoch seconds
	 * @param position the position
	 * @param upvoteCount the upvote count
	 * @param threadBuilder the thread builder
	 * @param choices the choices
	 * @param pollId the poll ID
	 * @param totalVotes the amount of total votes
	 */
	public FandomPoll(String title, String rawContent, long id, long siteId, long latestRevId, FandomPostAuthor author, long epochSecond, int position, int upvoteCount, FandomThread.Builder threadBuilder, List<FandomPollChoice> choices, long pollId, int totalVotes) {
		super(title, rawContent, id, siteId, latestRevId, author, epochSecond, position, upvoteCount, threadBuilder);
		FandomPollChoice[] choicesArray = new FandomPollChoice[choices.size()];
		for(int i = 0; i<choices.size(); i++) choicesArray[i] = choices.get(i);
		this.choices = choicesArray;
		this.pollId = pollId;
		this.totalVotes = totalVotes;
	}
	
	/**
	 * Creates a new FandomPoll instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentForumPosts(int)} instead.</b>
	 * @param title the title
	 * @param rawContent the content in plain text
	 * @param id the ID
	 * @param siteId the site ID
	 * @param latestRevId the latest revision ID
	 * @param author the author
	 * @param epochSecond the epoch seconds
	 * @param position the position
	 * @param upvoteCount the upvote count
	 * @param thread the thread
	 * @param choices the choices
	 * @param pollId the poll ID
	 * @param totalVotes the amount of total votes
	 */
	public FandomPoll(String title, String rawContent, long id, long siteId, long latestRevId, FandomPostAuthor author, long epochSecond, int position, int upvoteCount, FandomThread thread, FandomPollChoice[] choices, long pollId, int totalVotes) {
		super(title, rawContent, id, siteId, latestRevId, author, epochSecond, position, upvoteCount, thread);
		this.choices = choices;
		this.pollId = pollId;
		this.totalVotes = totalVotes;
	}
	
	/**
	 * Creates a new FandomPoll instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentForumPosts(int)} instead.</b>
	 * @param title the title
	 * @param rawContent the content in plain text
	 * @param id the ID
	 * @param siteId the site ID
	 * @param latestRevId the latest revision ID
	 * @param author the author
	 * @param epochSecond the epoch seconds
	 * @param position the position
	 * @param upvoteCount the upvote count
	 * @param thread the thread
	 * @param choices the choices
	 * @param pollId the poll ID
	 * @param totalVotes the amount of total votes
	 */
	public FandomPoll(String title, String rawContent, long id, long siteId, long latestRevId, FandomPostAuthor author, long epochSecond, int position, int upvoteCount, FandomThread thread, List<FandomPollChoice> choices, long pollId, int totalVotes) {
		super(title, rawContent, id, siteId, latestRevId, author, epochSecond, position, upvoteCount, thread);
		FandomPollChoice[] choicesArray = new FandomPollChoice[choices.size()];
		for(int i = 0; i<choices.size(); i++) choicesArray[i] = choices.get(i);
		this.choices = choicesArray;
		this.pollId = pollId;
		this.totalVotes = totalVotes;
	}

}

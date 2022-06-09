package FandomActivity;

/**
 * Represents a Fandom reply.
 * @author Mika Thein
 * @see FandomPost
 */
public class FandomReply extends FandomPost {
	
	/**
	 * Creates a new FandomReply instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentForumPosts(int)} instead.</b>
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
	public FandomReply(String rawContent, long id, long siteId, long latestRevId, FandomPostAuthor author, long epochSecond, int position, int upvoteCount, FandomThread.Builder threadBuilder) {
		super(null, rawContent, id, siteId, latestRevId, author, epochSecond, position, upvoteCount, threadBuilder);
	}
	
	/**
	 * Creates a new FandomReply instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentForumPosts(int)} instead.</b>
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
	public FandomReply(String rawContent, long id, long siteId, long latestRevId, FandomPostAuthor author, long epochSecond, int position, int upvoteCount, FandomThread thread) {
		super(null, rawContent, id, siteId, latestRevId, author, epochSecond, position, upvoteCount, thread);
	}

}

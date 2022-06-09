package FandomActivity;

import Fandom.Fandom;

/**
 * Represents a Fandom wall post.
 * @author Mika Thein
 * @see FandomPost
 * @see FandomActivity#getRecentWallPosts(int)
 */
public class FandomWallPost extends FandomPost {
	
	/**
	 * The wall name.
	 */
	public final String wallName;
	/**
	 * The wall ID.
	 */
	public final long wallId;
	/**
	 * The wall owner ID.
	 * @see Fandom#getUser(long)
	 */
	public final long wallOwnerId;
	
	/**
	 * Creates a new FandomWallPost instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentWallPosts(int)} instead.</b>
	 * @param title the title
	 * @param rawContent the raw content
	 * @param id the ID
	 * @param siteId the site ID
	 * @param latestRevId the latest revision ID
	 * @param author the author
	 * @param wallName the wall name
	 * @param wallId the wall ID
	 * @param wallOwnerId the ID of the wall owner
	 * @param epochSecond the epoch seconds
	 * @param position the position
	 * @param upvoteCount the amount of upvotes
	 * @param thread the thread
	 */
	public FandomWallPost(String title, String rawContent, long id, long siteId, long latestRevId, FandomPostAuthor author, String wallName, long wallId, long wallOwnerId, long epochSecond, int position, int upvoteCount, FandomThread thread) {
		super(title, rawContent, id, siteId, latestRevId, author, epochSecond, position, upvoteCount, thread);
		this.wallName = wallName;
		this.wallId = wallId;
		this.wallOwnerId = wallOwnerId;
	}
	
	/**
	 * Creates a new FandomWallPost instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentWallPosts(int)} instead.</b>
	 * @param title the title
	 * @param rawContent the raw content
	 * @param id the ID
	 * @param siteId the site ID
	 * @param latestRevId the latest revision ID
	 * @param author the author
	 * @param wallName the wall name
	 * @param wallId the wall ID
	 * @param wallOwnerId the ID of the wall owner
	 * @param epochSecond the epoch seconds
	 * @param position the position
	 * @param upvoteCount the amount of upvotes
	 * @param threadBuilder the thread builder
	 */
	public FandomWallPost(String title, String rawContent, long id, long siteId, long latestRevId, FandomPostAuthor author, String wallName, long wallId, long wallOwnerId, long epochSecond, int position, int upvoteCount, FandomThread.Builder threadBuilder) {
		super(title, rawContent, id, siteId, latestRevId, author, epochSecond, position, upvoteCount, threadBuilder);
		this.wallName = wallName;
		this.wallId = wallId;
		this.wallOwnerId = wallOwnerId;
	}

}

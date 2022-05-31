package FandomActivity;

import Fandom.FandomArticle;
import Fandom.FandomPage;

/**
 * Represents an article comment.
 * @author Mika Thein
 * @see FandomActivity#getRecentArticleComments(int)
 */
public class FandomArticleComment extends FandomPost {
	
	/**
	 * The funnel.
	 */
	public final String funnel;
	/**
	 * The page ID (equals to the container ID).
	 */
	public final long pageId;
	
	/**
	 * Creates a new FandomArticleComment instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentArticleComments(int)} instead.</b>
	 * @param title the title
	 * @param rawContent the content in raw text
	 * @param id the ID
	 * @param siteId the site ID
	 * @param latestRevId the latest revision ID
	 * @param containerId the container ID
	 * @param author the author
	 * @param funnel the funnel
	 * @param epochSecond the time in epoch seconds
	 * @param position the position within the thread
	 * @param upvoteCount the upvote count
	 * @param thread the thread
	 */
	public FandomArticleComment(String title, String rawContent, long id, long siteId, long latestRevId, long containerId, FandomPostAuthor author, String funnel, long epochSecond, int position, int upvoteCount, FandomThread thread) {
		super(title, rawContent, id, siteId, latestRevId, author, epochSecond, position, upvoteCount, thread);
		this.pageId = containerId;
		this.funnel = funnel;
	}
	
	/**
	 * Creates a new FandomArticleComment instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentArticleComments(int)} instead.</b>
	 * @param title the title
	 * @param rawContent the content in raw text
	 * @param id the ID
	 * @param siteId the site ID
	 * @param latestRevId the latest revision ID
	 * @param containerId the container ID
	 * @param author the author
	 * @param funnel the funnel
	 * @param epochSecond the time in epoch seconds
	 * @param position the position within the thread
	 * @param upvoteCount the upvote count
	 * @param threadBuilder the thread builder
	 */
	public FandomArticleComment(String title, String rawContent, long id, long siteId, long latestRevId, long containerId, FandomPostAuthor author, String funnel, long epochSecond, int position, int upvoteCount, FandomThread.Builder threadBuilder) {
		super(title, rawContent, id, siteId, latestRevId, author, epochSecond, position, upvoteCount, threadBuilder);
		this.pageId = containerId;
		this.funnel = funnel;
	}

}

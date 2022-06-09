package FandomActivity;

/**
 * This class can be used to receive notifications whenever a recent change or a post happened.<br>
 * Use {@link FandomActivityAdapter#FandomActivityAdapter(String)} to set a timestamp offset.
 * @author Mika Thein
 * @see FandomActivityAdapter
 * @see #recentChangeHappened(FandomRecentChange)
 * @see #postHappened(FandomPost)
 * @see #somethingHappened(FandomActivityElement)
 */
public interface FandomActivityListener {
	
	/**
	 * Whenever a recent change happens.
	 * @param recentChange the recent change
	 * @see FandomRecentChange
	 * @see FandomEdit
	 * @see FandomLog
	 * @see FandomNew
	 */
	public void recentChangeHappened(FandomRecentChange recentChange);
	
	/**
	 * Whenever a post happens.
	 * @param post the post
	 * @see FandomPost
	 * @see FandomPoll
	 * @see FandomReply
	 * @see FandomWallPost
	 * @see FandomArticleComment
	 */
	public void postHappened(FandomPost post);
	
	/**
	 * Whenever a post or a recent change happens.
	 * @param activityElement the activity element
	 * @see FandomActivityElement
	 * @see FandomPost
	 * @see FandomRecentChange
	 */
	public void somethingHappened(FandomActivityElement activityElement);

}

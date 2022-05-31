package FandomActivity;

/**
 * Reacts whenever a recent change or a post happened.
 * @author Mika Thein
 * @see #FandomActivityAdapter()
 * @see #FandomActivityAdapter(String)
 */
public class FandomActivityAdapter implements FandomActivityListener {
	
	/**
	 * The earliest timestamp of any forum activity that should be able to trigger the listener.
	 * @see #FandomActivityAdapter(String)
	 */
	public String earliestTimestamp = "";
	
	/**
	 * Creates a new FandomActivityAdapter instance.
	 * @see #FandomActivityAdapter(String)
	 * @see #recentChangeHappened(FandomRecentChange)
	 * @see #postHappened(FandomPost)
	 * @see #somethingHappened(FandomActivityElement)
	 */
	public FandomActivityAdapter() {}
	
	/**
	 * Creates a new FandomActivityAdapter instance.
	 * @see #FandomActivityAdapter()
	 * @see #recentChangeHappened(FandomRecentChange)
	 * @see #postHappened(FandomPost)
	 * @see #somethingHappened(FandomActivityElement)
	 * @param earliestTimestamp the earliest timestamp of any forum activity that should be able to trigger the listener as ISO 8601
	 */
	public FandomActivityAdapter(String earliestTimestamp) {
		this.earliestTimestamp = earliestTimestamp;
	}
		
	@Override
	public void recentChangeHappened(FandomRecentChange recentChange) {
	}
	
	@Override
	public void postHappened(FandomPost post) {
	}
	
	@Override
	public void somethingHappened(FandomActivityElement activityElement) {
	}

}

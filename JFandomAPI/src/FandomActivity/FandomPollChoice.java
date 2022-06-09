package FandomActivity;

/**
 * Represents a Fandom poll choice.
 * @author Mika Thein
 * @see FandomPoll
 * @see FandomActivity#getRecentForumPosts(int)
 */
public class FandomPollChoice {
	
	/**
	 * The ID.
	 */
	public final long id;
	/**
	 * The text.
	 */
	public final String text;
	/**
	 * The amount of votes.
	 */
	public final long votes;
	/**
	 * The usernames of all voters (if available, might be {@code null}).
	 */
	public final String[] voters;
	/**
	 * The image URL (might be {@code null}).
	 */
	public final String imageUrl;
	/**
	 * The position.
	 */
	public final int position;
	
	/**
	 * Creates a new FandomPollChoice instance (should not be used manually!).<p>
	 * <b>Retrieve poll choices by {@link FandomPoll#choices} ({@link FandomActivity#getRecentForumPosts(int)}) instead.</b>
	 * @param id the ID
	 * @param text the text
	 * @param votes the amount of votes
	 * @param voters the usernames of all voters
	 * @param imageUrl the image URL
	 * @param position the position
	 */
	public FandomPollChoice(long id, String text, long votes, String[] voters, String imageUrl, int position) {
		this.id = id;
		this.text = text;
		this.votes = votes;
		this.voters = voters;
		this.imageUrl = imageUrl;
		this.position = position;
	}

}

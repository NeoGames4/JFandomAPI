package FandomActivity;

import Fandom.Fandom;
import Fandom.FandomArticle;
import Fandom.FandomImage;
import Fandom.FandomImage;
import Fandom.FandomPage;

/**
 * A Fandom activity element.
 * @author Mika Thein
 * @see FandomActivity
 */
public abstract class FandomActivityElement {
	
	/**
	 * The username of the user.
	 */
	final public String user;
	/**
	 * The title (might be {@code null}).
	 */
	final public String title;
	/**
	 * The comment.
	 */
	final public String comment;
	/**
	 * Timestamp as ISO8601:<br>
	 * {@code yyyy-MM-ddTHH:mm:ssZ}
	 * <p>Can be converted to epoch seconds by {@code Instant.parse(timestamp).getEpochSecond()}.
	 */
	final public String timestamp;
	/**
	 * The ID.
	 */
	final public long id;
	/**
	 * The Fandom.
	 */
	final public Fandom fandom;
	
	/**
	 * Creates a new FandomActivityElement instance.
	 * @param user the username of the user
	 * @param title the title
	 * @param comment the comment
	 * @param timestamp the timestamp as ISO8601
	 * @param id the ID
	 * @param fandom the Fandom
	 */
	public FandomActivityElement(String user, String title, String comment, String timestamp, long id, Fandom fandom) {
		this.user = user;
		this.title = title;
		this.comment = comment;
		this.timestamp = timestamp;
		this.id = id;
		this.fandom = fandom;
	}
	
	@Override
	public String toString() {
		return title;
	}

}

package FandomActivity;

import Fandom.Fandom;

/**
 * Represents a Fandom block (log).
 * @author Mika Thein
 * @see FandomLog
 */
public class FandomLogBlock extends FandomLog {
	
	/**
	 * The duration.
	 */
	public final String duration;
	/**
	 * The expiry.
	 */
	public final String expiry;
	/**
	 * The flags.
	 */
	public final String[] flags;
	
	/**
	 * Creates a new FandomLogBlock instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentChanges(Fandom.FandomUser, Fandom.FandomPage, int)} instead.</b>
	 * @param user the user
	 * @param title the title
	 * @param comment the comment
	 * @param tags the tags
	 * @param timestamp the timestamp
	 * @param duration the duration
	 * @param expiry the expiry
	 * @param flags the flags
	 * @param id the ID
	 * @param logId the log ID
	 * @param action the action
	 * @param revisionId the revision ID
	 * @param pageId the page ID
	 * @param fandom the Fandom
	 */
	public FandomLogBlock(String user, String title, String comment, String[] tags, String timestamp, String duration, String expiry, String[] flags, long id, long logId, int action, long revisionId, long pageId, Fandom fandom) {
		super(user, title, comment, tags, timestamp, id, logId, action, revisionId, pageId, fandom);
		this.duration = duration;
		this.expiry = expiry;
		this.flags = flags;
	}

}

package FandomActivity;

import Fandom.Fandom;

/**
 * Represents a Fandom move (log).
 * @author Mika Thein
 * @see FandomLog
 */
public class FandomLogMove extends FandomLog {
	
	/**
	 * The targeted title.
	 */
	public final String targetTitle;

	/**
	 * Creates a new FandomLogMove instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentChanges(Fandom.FandomUser, Fandom.FandomPage, int)} instead.</b>
	 * @param user the user
	 * @param title the title
	 * @param comment the comment
	 * @param tags the tags
	 * @param targetTitle the target title
	 * @param timestamp the timestamp
	 * @param id the ID
	 * @param logId the log ID
	 * @param action the action
	 * @param revisionId the revision ID
	 * @param pageId the page ID
	 * @param fandom the Fandom
	 */
	public FandomLogMove(String user, String title, String comment, String[] tags, String targetTitle, String timestamp, long id, long logId, int action, long revisionId, long pageId, Fandom fandom) {
		super(user, title, comment, tags, timestamp, id, logId, action, revisionId, pageId, fandom);
		this.targetTitle = targetTitle;
	}

}

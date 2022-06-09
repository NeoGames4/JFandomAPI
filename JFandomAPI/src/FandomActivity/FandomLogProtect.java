package FandomActivity;

import Fandom.Fandom;

/**
 * Represents a Fandom protect (log).
 * @author Mika Thein
 * @see FandomLog
 */
public class FandomLogProtect extends FandomLog {
	
	/**
	 * The protection details.
	 */
	public final FandomLogProtectDetail[] details;
	
	/**
	 * Creates a new FandomLogProtect instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentChanges(Fandom.FandomUser, Fandom.FandomPage, int)} instead.</b>
	 * @param user the user
	 * @param title the title
	 * @param description the description
	 * @param tags the tags
	 * @param details the details
	 * @param timestamp the timestamp
	 * @param id the ID
	 * @param logId the log ID
	 * @param action the action
	 * @param revisionId the revision ID
	 * @param pageId the page ID
	 * @param fandom the Fandom
	 */
	public FandomLogProtect(String user, String title, String description, String[] tags, FandomLogProtectDetail[] details, String timestamp, long id, long logId, int action, long revisionId, long pageId, Fandom fandom) {
		super(user, title, description, tags, timestamp, id, logId, action, revisionId, pageId, fandom);
		this.details = details;
	}

}

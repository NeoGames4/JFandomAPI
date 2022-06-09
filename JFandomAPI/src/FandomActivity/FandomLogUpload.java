package FandomActivity;

import Fandom.Fandom;

/**
 * Represents a Fandom upload (log).
 * @author Mika Thein
 * @see FandomLog
 */
public class FandomLogUpload extends FandomLog {
	
	/**
	 * The image sha1.
	 */
	public final String imgSha1;

	/**
	 * Creates a new FandomLogUpload instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentChanges(Fandom.FandomUser, Fandom.FandomPage, int)} instead.</b>
	 * @param user the user
	 * @param title the title
	 * @param comment the comment
	 * @param tags the tags
	 * @param imgSha1 the imgage sha1
	 * @param timestamp the timestamp
	 * @param id the ID
	 * @param logId the log ID
	 * @param action the action
	 * @param revisionId the revision ID
	 * @param pageId the page ID
	 * @param fandom the Fandom
	 */
	public FandomLogUpload(String user, String title, String comment, String[] tags, String imgSha1, String timestamp, long id, long logId, int action, long revisionId, long pageId, Fandom fandom) {
		super(user, title, comment, tags, timestamp, id, logId, action, revisionId, pageId, fandom);
		this.imgSha1 = imgSha1;
	}

}

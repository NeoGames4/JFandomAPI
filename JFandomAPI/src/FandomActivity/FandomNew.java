package FandomActivity;

import Fandom.Fandom;

/**
 * Represents a Fandom recent change where a new object was defined.
 * @author Mika Thein
 * @see FandomActivity#getRecentChanges(int)
 */
public class FandomNew extends FandomRecentChange {
	
	/**
	 * The length.
	 */
	public final long len;
	
	/**
	 * Creates a new FandomNew instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentChanges(Fandom.FandomUser, Fandom.FandomPage, int)} instead.</b>
	 * @param user the user
	 * @param title the title
	 * @param comment the comment
	 * @param tags the tags
	 * @param timestamp the timestamp
	 * @param len the length
	 * @param id the ID
	 * @param newRevisionId the new revision ID
	 * @param pageId the page ID
	 * @param fandom the Fandom
	 */
	public FandomNew(String user, String title, String comment, String[] tags, String timestamp, long len, long id, long newRevisionId, long pageId, Fandom fandom) {
		super(user, title, comment, tags, timestamp, id, newRevisionId, 0, pageId, fandom);
		this.len = len;
	}

}

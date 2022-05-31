package FandomActivity;

import Fandom.Fandom;

/**
 * Represents a Fandom edit.
 * @author Mika Thein
 * @see FandomRecentChange
 * @see FandomActivity#getRecentChanges(int)
 */
public class FandomEdit extends FandomRecentChange {

	/**
	 * The old length.
	 */
	public final long oldLen;
	/**
	 * The new length.
	 */
	public final long newLen;
	
	/**
	 * Creates a new FandomEdit instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentChanges(int)} instead.</b>
	 * @param user the user
	 * @param title the title
	 * @param comment the comment
	 * @param tags the tags
	 * @param timestamp the timestamp as ISO 8601
	 * @param oldLen the old length
	 * @param newLen the new length
	 * @param id the ID
	 * @param newRevisionId the new revision ID
	 * @param oldRevisionId the old revision ID
	 * @param pageId the page ID
	 * @param fandom the Fandom
	 */
	public FandomEdit(String user, String title, String comment, String[] tags, String timestamp, long oldLen, long newLen, long id, long newRevisionId, long oldRevisionId, long pageId, Fandom fandom) {
		super(user, title, comment, tags, timestamp, id, newRevisionId, oldRevisionId, pageId, fandom);
		this.oldLen = oldLen;
		this.newLen = newLen;
	}

}

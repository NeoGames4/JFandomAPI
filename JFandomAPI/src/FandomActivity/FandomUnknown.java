package FandomActivity;

import Fandom.Fandom;

/**
 * Represents any recent change of an unknown type.
 * @author Mika Thein
 * @see FandomActivity#getRecentChanges(int)
 */
public class FandomUnknown extends FandomRecentChange {
	
	/**
	 * Creates a FandomUnknown instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentChanges(Fandom.FandomUser, Fandom.FandomPage, int)} instead.</b>
	 * @param user the username
	 * @param title the title
	 * @param comment the comment
	 * @param tags the tags
	 * @param timestamp the timestamp
	 * @param id the ID
	 * @param newRevisionId the new revision ID
	 * @param oldRevisionId the old revision ID
	 * @param pageId the page ID
	 * @param fandom the Fandom
	 */
	public FandomUnknown(String user, String title, String comment, String[] tags, String timestamp, long id, long newRevisionId, long oldRevisionId, long pageId, Fandom fandom) {
		super(user, title, comment, tags, timestamp, id, newRevisionId, oldRevisionId, pageId, fandom);
	}

}

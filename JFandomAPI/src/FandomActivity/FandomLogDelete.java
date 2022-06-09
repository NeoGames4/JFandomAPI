package FandomActivity;

import Fandom.Fandom;
import Fandom.FandomParser;

/**
 * Represents a Fandom delete (log).
 * @author Mika Thein
 * @see FandomLog
 */
public class FandomLogDelete extends FandomLog {
	
	/**
	 * Creates a new FandomLogDelete instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentChanges(Fandom.FandomUser, Fandom.FandomPage, int)} instead.</b>
	 * @param user the user
	 * @param title the title
	 * @param comment the comment
	 * @param tags the tags
	 * @param timestamp the timestamp
	 * @param id the ID
	 * @param logId the log ID
	 * @param action the action
	 * @param pageId the page ID
	 * @param fandom the Fandom
	 */
	public FandomLogDelete(String user, String title, String comment, String[] tags, String timestamp, long id, long logId, int action, long pageId, Fandom fandom) {
		super(user, title, comment, tags, timestamp, id, logId, action, 0, pageId, fandom);
	}
	
	@Override
	public String getUrl() {
		return "https://" + super.fandom + "/wiki/" + FandomParser.toURL(super.title, true);
	}
	
	@Override
	public String toString() {
		return getUrl();
	}

}

package FandomActivity;

import Fandom.Fandom;

/**
 * Represents a Fandom log.
 * @author Mika Thein
 * @see FandomActivity#getRecentChanges(int)
 */
public abstract class FandomLog extends FandomRecentChange {
	
	public static final int TYPE_UPLOAD = 0;
	public static final int TYPE_MOVE = 1;
	public static final int TYPE_DELETE = 2;
	public static final int TYPE_PROTECT = 3;
	public static final int TYPE_BLOCK = 4;
	
	public static final int ACTION_UPLOAD = 0;
	public static final int ACTION_MOVE = 1;
	public static final int ACTION_DELETE = 2;
	public static final int ACTION_PROTECT = 3;
	public static final int ACTION_BLOCK = 4;
	public static final int ACTION_OVERWRITE = 5;
	public static final int ACTION_REVERT = 6;
	public static final int ACTION_MODIFY = 7;
	
	/**
	 * The log ID.
	 */
	public final long logId;
	/**
	 * The action type.
	 * @see #ACTION_UPLOAD
	 * @see #ACTION_MOVE
	 * @see #ACTION_DELETE
	 * @see #ACTION_PROTECT
	 * @see #ACTION_BLOCK
	 * @see #ACTION_OVERWRITE
	 * @see #ACTION_REVERT
	 * @see #ACTION_MODIFY
	 * @see #toActionInt(String)
	 */
	public final int action;
	
	/**
	 * Creates a new FandomLog instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentChanges(Fandom.FandomUser, Fandom.FandomPage, int)} instead.</b>
	 * @param user the user
	 * @param title the title
	 * @param comment the comment
	 * @param tags the tags
	 * @param timestamp the timestamp
	 * @param id the ID
	 * @param logId the log ID
	 * @param action the action type
	 * @param revisionId the revision ID
	 * @param pageId the page ID
	 * @param fandom the Fandom
	 */
	public FandomLog(String user, String title, String comment, String[] tags, String timestamp, long id, long logId, int action, long revisionId, long pageId, Fandom fandom) {
		super(user, title, comment, tags, timestamp, id, revisionId, 0, pageId, fandom);
		this.logId = logId;
		this.action = action;
	}
	
	/**
	 * Converts an action type name to its ID.
	 * @param action the action
	 * @see #ACTION_UPLOAD
	 * @see #ACTION_MOVE
	 * @see #ACTION_DELETE
	 * @see #ACTION_PROTECT
	 * @see #ACTION_BLOCK
	 * @see #ACTION_OVERWRITE
	 * @see #ACTION_REVERT
	 * @see #ACTION_MODIFY
	 * @see #toTypeInt(String)
	 * @return the ID
	 */
	public static int toActionInt(String action) {
		switch(action) {
			case "upload": return ACTION_UPLOAD;
			case "move": return ACTION_MOVE;
			case "delete": return ACTION_DELETE;
			case "protect": return ACTION_PROTECT;
			case "block": return ACTION_BLOCK;
			case "overwrite": return ACTION_OVERWRITE;
			case "revert": return ACTION_REVERT;
			case "modify": return ACTION_MODIFY;
		} return -1;
	}
	
	/**
	 * Converts a type name to its ID.
	 * @param type the type
	 * @see #TYPE_UPLOAD
	 * @see #TYPE_MOVE
	 * @see #TYPE_DELETE
	 * @see #TYPE_PROTECT
	 * @see #TYPE_BLOCK
	 * @see #toActionInt(String)
	 * @return the ID
	 */
	public static int toTypeInt(String type) {
		switch(type) {
			case "upload": return TYPE_UPLOAD;
			case "move": return TYPE_MOVE;
			case "delete": return TYPE_DELETE;
			case "protect": return TYPE_PROTECT;
			case "block": return TYPE_BLOCK;
		} return -1;
	}

}

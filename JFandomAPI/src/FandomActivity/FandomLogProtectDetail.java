package FandomActivity;

/**
 * Represents protection details of a Fandom protect (log).
 * @author Mika Thein
 * @see FandomLogProtect
 * @see FandomLog
 */
public class FandomLogProtectDetail {

	public static final int TYPE_UPLOAD = FandomLog.TYPE_UPLOAD;
	public static final int TYPE_MOVE = FandomLog.TYPE_MOVE;
	public static final int TYPE_DELETE = FandomLog.TYPE_DELETE;
	public static final int TYPE_PROTECT = FandomLog.TYPE_PROTECT;
	public static final int TYPE_BLOCK = FandomLog.TYPE_BLOCK;
	
	/**
	 * The type.
	 * @see #TYPE_UPLOAD
	 * @see #TYPE_MOVE
	 * @see #TYPE_DELETE
	 * @see #TYPE_PROTECT
	 * @see #TYPE_BLOCK
	 */
	public final int type;
	/**
	 * The level.
	 */
	public final String level;
	/**
	 * The expiry.
	 */
	public final String expiry;
	
	/**
	 * Creates a FandomLogProtectDetail instance (should not be used manually!).<p>
	 * <b>Use {@link FandomLogProtect#details} ({@link FandomActivity#getRecentChanges(Fandom.FandomUser, Fandom.FandomPage, int)}) instead.</b>
	 * @param type the type
	 * @param level the level
	 * @param expiry the expiry
	 */
	public FandomLogProtectDetail(int type, String level, String expiry) {
		this.type = type;
		this.level = level;
		this.expiry = expiry;
	}
	
	/**
	 * @param type the type name
	 * @return the ID of a type
	 * @see #TYPE_UPLOAD
	 * @see #TYPE_MOVE
	 * @see #TYPE_DELETE
	 * @see #TYPE_PROTECT
	 * @see #TYPE_BLOCK
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

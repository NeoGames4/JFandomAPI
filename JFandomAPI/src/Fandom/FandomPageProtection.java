package Fandom;

public class FandomPageProtection {
	
	public final static String EDIT = "edit", MOVE = "move", CREATION = "creation", UPLOAD = "upload";
	
	/**
	 * The type of protection.
	 * @see #EDIT
	 * @see #MOVE
	 * @see #CREATION
	 * @see #UPLOAD
	 */
	public final String type;
	/**
	 * The level of protection.
	 */
	public final String level;
	/**
	 * The expiry of protection.
	 */
	public final String expiry;
	/**
	 * The page.
	 */
	public final FandomPage page;
	
	/**
	 * Creates a new FandomPageProtection instance (should not be used manually!).<p>
	 * <b>Use {@link FandomPage#protections} instead.</b>
	 * @param type the type
	 * @param level the level
	 * @param expiry the expiry
	 * @param page the page
	 */
	public FandomPageProtection(String type, String level, String expiry, FandomPage page) {
		this.type = type;
		this.level = level;
		this.expiry = expiry;
		this.page = page;
		if(!type.equals(EDIT) && !type.equals(MOVE) && !type.equals(CREATION) && !type.equals(UPLOAD)) {
			Fandom.warn("Page protection type \"" + type + "\" may not exist.");
		}
	}

}

package FandomActivity;

import Fandom.Fandom;
import Fandom.FandomUser;

/**
 * Represents a Fandom post author.
 * @author Mika Thein
 * @see FandomPost
 */
public class FandomPostAuthor {
	
	/**
	 * The username.
	 */
	public final String name;
	/**
	 * The user ID.
	 */
	public final long id;
	/**
	 * The avatar URL (might be {@code null}).
	 */
	public final String avatarUrl;
	
	/**
	 * Creates a new FandomPostAuthor instance.<p>
	 * @param name the username
	 * @param id the user ID
	 * @param avatarUrl the avatar URL
	 */
	public FandomPostAuthor(String name, long id, String avatarUrl) {
		this.name = name;
		this.id = id;
		this.avatarUrl = avatarUrl;
	}
	
	/**
	 * Returns the author as {@link FandomUser}.
	 * @param fandom the Fandom
	 * @return the author as FandomUser
	 */
	public FandomUser getAsFandomUser(Fandom fandom) {
		return new FandomUser(name, fandom);
	}

}

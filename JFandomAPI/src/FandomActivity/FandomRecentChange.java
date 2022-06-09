package FandomActivity;
import Fandom.Fandom;
import Fandom.FandomArticle;
import Fandom.FandomImage;
import Fandom.FandomPage;

/**
 * Represents a recent change.
 * @author Mika Thein
 * @see FandomActivity#getRecentChanges(int)
 */
public abstract class FandomRecentChange extends FandomActivityElement {
	
	/**
	 * The old revision ID.
	 * @see #newRevisionId
	 */
	public final long oldRevisionId;
	/**
	 * The new revision ID.
	 * @see #oldRevisionId
	 */
	public final long newRevisionId;
	/**
	 * The page ID.
	 */
	public final long pageId;
	/**
	 * The tags.
	 */
	public final String[] tags;
	
	/**
	 * Creates a new FandomRecentChange instance (should not be used manually!).<p>
	 * <b>Use {@link FandomActivity#getRecentChanges(Fandom.FandomUser, FandomPage, int)} instead.</b>
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
	public FandomRecentChange(String user, String title, String comment, String[] tags, String timestamp, long id, long newRevisionId, long oldRevisionId, long pageId, Fandom fandom) {
		super(user, title, comment, timestamp, id, fandom);
		this.newRevisionId = newRevisionId;
		this.oldRevisionId = oldRevisionId;
		this.pageId = pageId;
		this.tags = tags;
	}
	
	
	/**
	 * @return the page
	 */
	public FandomPage getPage() {
		return fandom.getPage(pageId);
	}
	
	/**
	 * @return the page as article
	 */
	public FandomArticle getAsArticle() {
		return fandom.getArticle(pageId);
	}
	
	/**
	 * @return the page as image
	 */
	public FandomImage getAsImage() {
		return fandom.getImage(pageId);
	}
	
	/**
	 * @return the URl
	 */
	public String getUrl() {
		return "https://" + super.fandom.baseUrl + "/wiki/Special:Diff/" + newRevisionId;
	}
	
	@Override
	public String toString() {
		return getUrl();
	}
	
}

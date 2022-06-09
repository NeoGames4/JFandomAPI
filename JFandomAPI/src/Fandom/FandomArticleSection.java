package Fandom;
import org.json.JSONObject;

public class FandomArticleSection {
	
	/**
	 * The section's title.
	 */
	public final String title;
	/**
	 * The section's index.
	 */
	public final int index;
	/**
	 * The section's anchor title.
	 */
	public final String anchor;
	/**
	 * The section's TOC level.
	 */
	public final int tocLevel;
	/**
	 * The section's level.
	 */
	public final int level;
	/**
	 * The section's number.
	 */
	public final String number;
	/**
	 * The section's offset in bytes.
	 */
	public final long byteOffset;
	/**
	 * The article.
	 */
	public final FandomArticle article;
	private String text;
	private String wikitextText;
	private String htmlText;
	
	/**
	 * Creates a new FandomArticleSection instance (shouldn't be used manually!).<p>
	 * <b>Use {@link FandomArticle#getSections()} instead.</b>
	 * @param title the title
	 * @param index the index
	 * @param tocLevel the TOC-level
	 * @param level the level
	 * @param number the number
	 * @param byteOffset the offset in bytes
	 * @param anchor the anchor title
	 * @param article the article
	 */
	public FandomArticleSection(String title, int index, int tocLevel, int level, String number, long byteOffset, String anchor, FandomArticle article) {
		this.title = title;
		this.index = index;
		this.anchor = anchor != null ? anchor : title;
		this.tocLevel = tocLevel;
		this.level = level;
		this.number = number;
		this.byteOffset = byteOffset;
		this.article = article;
	}
	
	/**
	 * @return the content as plain text
	 */
	public String getPlainText() {
		if(text == null) {
			JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + article.fandom.baseUrl + "/api.php?action=parse&pageid=" + article.id + "&prop=wikitext&section=" + index + "&format=json")).getJSONObject("parse");
			text = FandomParser.parse(jsonObject.getJSONObject("wikitext").getString("*"));
		} return text;
	}
	
	/**
	 * @return the content as wikitext
	 */
	public String getWikitextText() {
		if(wikitextText == null) {
			JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + article.fandom.baseUrl + "/api.php?action=parse&pageid=" + article.id + "&prop=wikitext&section=" + index + "&format=json")).getJSONObject("parse");
			wikitextText = jsonObject.getJSONObject("wikitext").getString("*");
		} return wikitextText;
	}
	
	/**
	 * @return the content as HTML
	 */
	public String getHtmlText() {
		if(htmlText == null) {
			JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + article.fandom.baseUrl + "/api.php?action=parse&pageid=" + article.id + "&prop=text&section=" + index + "&format=json")).getJSONObject("parse");
			htmlText = jsonObject.getJSONObject("text").getString("*");
		} return htmlText;
	}
	
	/**
	 * @return the URL
	 */
	public String getUrl() {
		return article.url + "#" + FandomParser.toURL(anchor, true);
	}
	
	@Override
	public String toString() {
		return getUrl();
	}

}

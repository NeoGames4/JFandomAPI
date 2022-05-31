package Fandom;

import org.json.JSONObject;

/**
 * Represents any Fandom page (articles, files, categories, user pages, ...).
 * @author Mika Thein
 * @see FandomArticle
 * @see FandomImage
 * @see FandomCategory
 * @see FandomUser
 * @see Fandom
 */
public class FandomPage {
	
	/**
	 * The title.
	 * @see #displayTitle
	 * @see #id
	 */
	public final String title;
	/**
	 * The display title.
	 * @see #title
	 * @see #id
	 */
	public final String displayTitle;
	/**
	 * The ID.
	 * @see #title
	 */
	public final long id;
	/**
	 * The namespace.
	 */
	public final long namespace;
	public final String touched;
	/**
	 * The latest revision ID.
	 */
	public final long latestRevisionId;
	/**
	 * The length.
	 */
	public final long length;
	/**
	 * The content model.
	 */
	public final String contentModel;
	/**
	 * The protections.
	 * @see #restrictionTypes
	 */
	public final FandomPageProtection[] protections;
	/**
	 * The restrictions
	 * @see #protections
	 */
	public final String[] restrictionTypes;
	/**
	 * The preload.
	 */
	public final String preload;
	/**
	 * The categories.
	 */
	public final FandomCategory[] categories;
	/**
	 * The URL.
	 * @see #canonicalUrl
	 */
	public final String url;
	/**
	 * The canonical URL.
	 * @see #url
	 */
	public final String canonicalUrl;
	/**
	 * The Fandom.
	 */
	public final Fandom fandom;
	
	/**
	 * Creates a new FandomPage instance.
	 * @param id the page ID
	 * @param fandom the Fandom
	 * @see #FandomPage(String, Fandom)
	 */
	public FandomPage(long id, Fandom fandom) {
		this.id = id;
		this.fandom = fandom;
		if(!exists(id, fandom)) throw new NotFoundException("There is no page with ID " + id);
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=query&pageids=" + id + "&prop=info|categories&inprop=url|displaytitle|protection|preload&format=json")).getJSONObject("query").getJSONObject("pages").getJSONObject(id + "");
		this.title = jsonObject.optString("title");
		this.namespace = jsonObject.optLong("ns");
		displayTitle = jsonObject.optString("displaytitle");
		touched = jsonObject.optString("touched");
		latestRevisionId = jsonObject.optLong("lastrevid");
		length = jsonObject.optLong("length");
		contentModel = jsonObject.optString("contentmodel");
		restrictionTypes = new String[jsonObject.getJSONArray("restrictiontypes").length()];
		for(int i = 0; i<restrictionTypes.length; i++) restrictionTypes[i] = jsonObject.getJSONArray("restrictiontypes").getString(i);
		protections = new FandomPageProtection[jsonObject.getJSONArray("protection").length()];
		for(int i = 0; i<protections.length; i++) {
			JSONObject p = jsonObject.getJSONArray("protection").getJSONObject(i);
			protections[i] = new FandomPageProtection(p.getString("type"), p.getString("level"), p.getString("expiry"), this);
		}
		preload = jsonObject.optString("preload");
		if(jsonObject.has("categories") && namespace != Fandom.NAMESPACE_CATEGORY) {
			categories = new FandomCategory[jsonObject.getJSONArray("categories").length()];
			for(int i = 0; i<categories.length; i++) categories[i] = new FandomCategory(jsonObject.getJSONArray("categories").getJSONObject(i).optString("title"), fandom);
		} else categories = new FandomCategory[0];
		canonicalUrl = jsonObject.optString("canonicalurl");
		url = jsonObject.optString("fullurl");
	}
	
	/**
	 * Creates a new FandomPage instance.
	 * @param title the page title
	 * @param fandom the Fandom
	 * @see #FandomPage(long, Fandom)
	 */
	public FandomPage(String title, Fandom fandom) {
		this(getIdFromTitle(title, fandom), fandom);
	}
	
	/**
	 * Returns the page ID of page with title {@code title}.
	 * @param title the page title
	 * @param fandom the Fandom
	 * @return the page ID
	 * @throws NotFoundException if the given page does not exist
	 */
	static public long getIdFromTitle(String title, Fandom fandom) {
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=parse&page=" + FandomParser.toURL(title, true) + "&prop=displaytitle&format=json"));
		try {
			return jsonObject.getJSONObject("parse").getLong("pageid");
		} catch(Exception e) {
			throw new NotFoundException("There is no page with title \"" + title + "\"\nFANDOM error info: " + jsonObject.getJSONObject("error").getString("info"));
		}
	}
	
	/**
	 * Whether a page with the ID {@code id} exists.
	 * @param id the page ID
	 * @param fandom the Fandom
	 * @see #exists(String, Fandom)
	 * @return whether the given page exists
	 */
	static public boolean exists(long id, Fandom fandom) {
		try {
			new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=parse&pageid=" + id + "&prop=displaytitle&format=json")).getJSONObject("parse").getString("title");
			return true;
		} catch(Exception e) {
			if(Fandom.DEBUG) e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Whether a page with the title {@code title} exists.
	 * @param title the page title
	 * @param fandom the Fandom
	 * @see #exists(long, Fandom)
	 * @return whether the given page exists
	 */
	static public boolean exists(String title, Fandom fandom) {
		try {
			new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=parse&page=" + title + "&prop=displaytitle&format=json")).getJSONObject("parse").getString("title");
			return true;
		} catch(Exception e) {
			if(Fandom.DEBUG) e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public String toString() {
		return url;
	}

}

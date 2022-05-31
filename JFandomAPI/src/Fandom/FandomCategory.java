package Fandom;

import org.json.JSONArray;
import org.json.JSONObject;

public class FandomCategory extends FandomPage {
	
	/**
	 * The size of the category.
	 * @see #getPages()
	 */
	public final int size;
	/**
	 * The amount of pages within that category.
	 * @see #getPages()
	 */
	public final int pages;
	/**
	 * The amount of files within that category.
	 * @see #getPages()
	 */
	public final int files;
	/**
	 * The amount of subcats within that category.
	 * @see #getPages()
	 */
	public final int subcats;
	
	/**
	 * Creates a new FandomCategory instance.
	 * @param title the title (don't forget the "Category:"-suffix)
	 * @param fandom the fandom
	 * @see #getPages()
	 */
	public FandomCategory(String title, Fandom fandom) {
		super(title, fandom);
		if(super.namespace != Fandom.NAMESPACE_CATEGORY) Fandom.warn(title + " might not be a category");
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=query&pageids=" + super.id + "&prop=categoryinfo&format=json")).getJSONObject("query").getJSONObject("pages").getJSONObject(super.id + "").getJSONObject("categoryinfo");
		size = jsonObject.optInt("size");
		pages = jsonObject.optInt("pages");
		files = jsonObject.optInt("files");
		subcats = jsonObject.optInt("subcats");
	}
	
	/**
	 * Returns all pages of that category as {@link Element}, where the key is equals to the page title and the value is equal to the page ID (long).
	 * @return all pages of that category
	 */
	public Element[] getPages() {
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=query&list=categorymembers&cmpageid=" + super.id + "&format=json"));
		if(jsonObject.has("error")) throw new ReadingException("Can't read categories: " + jsonObject.getJSONObject("error").getString("info"));
		JSONArray jsonArray = jsonObject.getJSONObject("query").getJSONArray("categorymembers");
		Element[] pages = new Element[jsonArray.length()];
		for(int i = 0; i<pages.length; i++) pages[i] = new Element(jsonArray.getJSONObject(i).getString("title"), jsonArray.getJSONObject(i).getLong("pageid"));
		return pages;
	}

}

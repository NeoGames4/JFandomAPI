package Fandom;
import org.json.JSONObject;

/**
 * A Fandom article.
 * @author Mika Thein
 * @see Fandom
 */
public class FandomArticle extends FandomPage {
	
	public static final int NAMESPACE = Fandom.NAMESPACE_MAIN;
	
	/**
	 * The article description as wikitext.
	 */
	public final String wikitextDescription;
	/**
	 * The article description in plain text.
	 */
	public final String description;
	/**
	 * The article description as HTML.
	 */
	public final String htmlDescription;
	private String[] images;
	private FandomArticleSection[] sections;
	private FandomArticleLanguage[] languages;
	/**
	 * Latest revision id.
	 */
	public final long revid;
	
	/**
	 * If DO_NOT_BUILD has been chosen as build option, images, sections and languages won't be generated.
	 * @see #BUILD
	 * @see #build()
	 * @see #FandomArticle(long, Fandom, int)
	 * @see #FandomArticle(String, Fandom, int)
	 */
	static public final int DO_NOT_BUILD = 0;
	/**
	 * If BUILD has been chosen as build option, images, sections and languages will be generated.
	 * @see #DO_NOT_BUILD
	 * @see #build()
	 * @see #FandomArticle(long, Fandom, int)
	 * @see #FandomArticle(String, Fandom, int)
	 */
	static public final int BUILD = 1;
	
	/**
	 * Creates a new FandomArticle instance (with {@link DO_NOT_BUILD} as build option).
	 * @param title the article's title
	 * @param fandom the fandom
	 * @see #FandomArticle(long, Fandom)
	 * @see #FandomArticle(String, Fandom, int)
	 * @see #FandomArticle(long, Fandom, int)
	 */
	public FandomArticle(String title, Fandom fandom) {
		this(getIdFromTitle(title, fandom), fandom, DO_NOT_BUILD);
	}
	
	/**
	 * Creates a new FandomArticle instance.
	 * @param title the article's title
	 * @param fandom the fandom
	 * @param build whether images, sections and languages should be generated
	 * @see #BUILD
	 * @see #DO_NOT_BUILD
	 * @see #build()
	 * @see #FandomArticle(long, Fandom, int)
	 */
	public FandomArticle(String title, Fandom fandom, int build) {
		this(getIdFromTitle(title, fandom), fandom, build);
	}
	
	/**
	 * Creates a new FandomArticle instance (with {@link DO_NOT_BUILD} as build option).
	 * @param id the article's ID
	 * @param fandom the fandom
	 * @see #FandomArticle(String, Fandom)
	 * @see #FandomArticle(long, Fandom, int)
	 * @see #FandomArticle(String, Fandom, int)
	 */
	public FandomArticle(long id, Fandom fandom) {
		this(id, fandom, DO_NOT_BUILD);
	}
	
	/**
	 * Creates a new FandomArticle instance.
	 * @param id the article's ID
	 * @param fandom the fandom
	 * @param build whether images, sections and languages should be generated
	 * @see #BUILD
	 * @see #DO_NOT_BUILD
	 * @see #build()
	 * @see #FandomArticle(String, Fandom, int)
	 */
	public FandomArticle(long id, Fandom fandom, int build) {
		super(id, fandom);
		JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=parse&pageid=" + id + "&prop=wikitext|text|revid&section=0&format=json")).getJSONObject("parse");
		revid = jsonObject.getLong("revid");
		wikitextDescription = jsonObject.getJSONObject("wikitext").getString("*");
		htmlDescription = jsonObject.getJSONObject("text").getString("*");
		description = FandomParser.parse(wikitextDescription);
		if(super.namespace != Fandom.NAMESPACE_MAIN && super.namespace != Fandom.NAMESPACE_USER) System.err.println("FandomPage with ID " + id + " and title \"" + title + "\" might not be a FandomArticle (namespace " + namespace + " is unequal to Fandom.NAMESPACE_MAIN " + Fandom.NAMESPACE_MAIN + " and Fandom.NAMESPACE_USER " + Fandom.NAMESPACE_USER + ").");
		if(build == BUILD) build();
	}
	
	/**
	 * Loads images, sections and languages.<p>
	 * This method is only necessary if {@link #DO_NOT_BUILD} has been chosen as build option.
	 * @see #BUILD
	 * @see #DO_NOT_BUILD
	 * @see #FandomArticle(long, Fandom, int)
	 * @see #getImages()
	 * @see #getSections()
	 * @see #getLanguages()
	 * @return this FandomArticle
	 */
	public FandomArticle build() {
		try {
			String text = Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=parse&pageid=" + id + "&prop=sections|displaytitle|images|langlinks&format=json");
			JSONObject jsonObject = new JSONObject(text).getJSONObject("parse");
			
			images = new String[jsonObject.getJSONArray("images").length()];
			for(int i = 0; i<images.length; i++) {
				images[i] = jsonObject.getJSONArray("images").getString(i);
			}
			
			sections = new FandomArticleSection[jsonObject.getJSONArray("sections").length()];
			for(int i = 0; i<sections.length; i++) {
				JSONObject section = jsonObject.getJSONArray("sections").getJSONObject(i);
				sections[i] = new FandomArticleSection(section.getString("line"), Integer.parseInt(section.getString("index")), section.getInt("toclevel"), Integer.parseInt(section.getString("level")), section.getString("number"), section.getLong("byteoffset"), section.getString("anchor"), this);
			}
			
			languages = new FandomArticleLanguage[jsonObject.getJSONArray("langlinks").length()];
			for(int i = 0; i<languages.length; i++) {
				JSONObject language = jsonObject.getJSONArray("langlinks").getJSONObject(i);
				languages[i] = new FandomArticleLanguage(this, language.getString("lang"), language.getString("url"), language.getString("langname"), language.getString("autonym"), language.getString("*"));
			}
			
			return this;
		} catch(Exception e) {
			if(Fandom.DEBUG) e.printStackTrace();
		} throw new ReadingException("Can't read the content of " + title + " (set Fandom.DEBUG to true to print the exact exception stack trace)");
	}
	
	/**
	 * Returns all image names.<p>
	 * <b>Warning:</b> Might return {@code null} if {@link #build()} has not been executed and {@link #DO_NOT_BUILD} has been chosen as build option.
	 * @see #getImage(int)
	 * @see #getSections()
	 * @see #getLanguages()
	 * @return all image names
	 */
	public String[] getImages() {
		return images;
	}
	
	/**
	 * Returns a specific image as {@link FandomImage}.<p>
	 * <b>Warning:</b> Might return {@code null} if {@link #build()} has not been executed and {@link #DO_NOT_BUILD} has been chosen as build option.
	 * @param index the index
	 * @see #getImages()
	 * @return the image with the given index
	 */
	public FandomImage getImage(int index) {
		return new FandomImage(images[index], fandom);
	}
	
	/**
	 * Returns all sections.<p>
	 * <b>Warning:</b> Might return {@code null} if {@link #build()} has not been executed and {@link #DO_NOT_BUILD} has been chosen as build option.
	 * @see #getSection(int)
	 * @see #getSection(String)
	 * @see #getSections(int)
	 * @see #getImages()
	 * @see #getLanguages()
	 * @return all sections
	 */
	public FandomArticleSection[] getSections() {
		return sections;
	}
	
	/**
	 * Returns {@code max} sections.<p>
	 * <b>Warning:</b> Might return {@code null} if {@link #build()} has not been executed and {@link #DO_NOT_BUILD} has been chosen as build option.
	 * @param max the maximum of sections
	 * @see #getSection(int)
	 * @see #getSection(String)
	 * @see #getSections()
	 * @see #getImages()
	 * @see #getLanguages()
	 * @return {@code max} sections
	 */
	public FandomArticleSection[] getSections(int max) {
		if(max >= sections.length) return sections;
		FandomArticleSection[] c = new FandomArticleSection[max];
		for(int i = 0; i<c.length; i++) {
			c[i] = sections[i];
		} return c;
	}
	
	/**
	 * Returns a specific section.<p>
	 * <b>Warning:</b> Might return {@code null} if {@link #build()} has not been executed and {@link #DO_NOT_BUILD} has been chosen as build option.
	 * @param name the name of the section
	 * @see #getSection(int)
	 * @see #getSections(int)
	 * @see #getSections()
	 * @see #getImages()
	 * @see #getLanguages()
	 * @return the section named {@code name}
	 */
	public FandomArticleSection getSection(String name) {
		for(FandomArticleSection c : sections) {
			if(c.title.trim().equals(name)) return c;
		} throw new NotFoundException("Can't find section \"" + name + "\" in " + title);
	}
	
	/**
	 * Returns a specific section.<p>
	 * <b>Warning:</b> Might return {@code null} if {@link #build()} has not been executed and {@link #DO_NOT_BUILD} has been chosen as build option.
	 * @param index the index
	 * @see #getSection(String)
	 * @see #getSections(int)
	 * @see #getSections()
	 * @see #getImages()
	 * @see #getLanguages()
	 * @return
	 */
	public FandomArticleSection getSection(int index) {
		return sections[index];
	}
	
	/**
	 * Returns all languages.<p>
	 * <b>Warning:</b> Might return {@code null} if {@link #build()} has not been executed and {@link #DO_NOT_BUILD} has been chosen as build option.
	 * @see #getLanguage(String)
	 * @see #getImages()
	 * @see #getSections()
	 * @return all languages
	 */
	public FandomArticleLanguage[] getLanguages() {
		return languages;
	}
	
	/**
	 * Returns a specific language.<p>
	 * <b>Warning:</b> Might return {@code null} if {@link #build()} has not been executed and {@link #DO_NOT_BUILD} has been chosen as build option.
	 * @param lang the language short name (like {@code en} or {@code de})
	 * @see #getLanguages()
	 * @see #getImages()
	 * @see #getSections()
	 * @return the language with short form {@code lang}
	 */
	public FandomArticleLanguage getLanguage(String lang) {
		for(FandomArticleLanguage l : languages) {
			if(l.lang.trim().equalsIgnoreCase(lang.trim())) return l;
		} throw new NotFoundException("Can't find language \"" + lang + "\" in " + title);
	}

}

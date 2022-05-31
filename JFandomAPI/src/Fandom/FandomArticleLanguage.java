package Fandom;

public class FandomArticleLanguage {
	
	/**
	 * The original article.
	 */
	public final FandomArticle article;
	/**
	 * The short form of the language name (e. g. {@code en} or {@code de}).
	 */
	public final String lang;
	/**
	 * The URL of the article.
	 */
	public final String url;
	/**
	 * The full name of the language.
	 */
	public final String langName;
	/**
	 * The full name of the language in its native language.
	 */
	public final String originalLangName;
	/**
	 * The article name.
	 */
	public final String articleName;
	
	/**
	 * Creates a new FandomArticleLanguage instance (should not be used manually!).<p>
	 * <b>Use {@link FandomArticle#getLanguages()} instead.</b>
	 * @param article the original article
	 * @param lang the language short form
	 * @param url the url
	 * @param languageName the name of the language
	 * @param originalLanguageName the original name of the language
	 * @param articleName the article's name
	 */
	public FandomArticleLanguage(FandomArticle article, String lang, String url, String languageName, String originalLanguageName, String articleName) {
		this.article = article;
		this.lang = lang;
		this.url = url;
		this.langName = languageName;
		this.originalLangName = originalLanguageName;
		this.articleName = articleName;
	}
	
	/**
	 * Returns the Fandom of the article.
	 * @return the fandom
	 */
	public Fandom getLangFandom() {
		return new Fandom(article.fandom.root + "/" + lang);
	}
	
	/**
	 * Returns the article.
	 * @return the article
	 */
	public FandomArticle getLangArticle() {
		return getLangFandom().getArticle(articleName);
	}

}

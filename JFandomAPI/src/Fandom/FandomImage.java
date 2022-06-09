package Fandom;

import org.json.JSONObject;

public class FandomImage extends FandomPage {
	
	public static final int NAMESPACE = Fandom.NAMESPACE_FILE;
	
	private long size;
	private int width;
	private int height;
	private String timestamp;
	private long userId;
	private String comment;
	private String rawImageUrl;
	private String sha1;
	private String mime;
	private String mediaType;
	private int bitDepth;
	
	/**
	 * Creates a new FandomImage instance.<p>
	 * <b>Please note:</b> in order to receive image information you'd need to {@link #build()} first.
	 * @param fileName the file's name (don't forget the "Image:"-suffix)
	 * @param fandom the fandom
	 * @see #FandomImage(long, Fandom)
	 * @see #build()
	 */
	public FandomImage(String fileName, Fandom fandom) {
		this(getIdFromTitle(fileName, fandom), fandom);
	}
	
	/**
	 * Creates a new FandomImage instance.<p>
	 * <b>Please note:</b> in order to receive image information you'd need to {@link #build()} first.
	 * @param id the file's ID
	 * @param fandom the fandom
	 * @see #FandomImage(String, Fandom)
	 * @see #build()
	 */
	public FandomImage(long id, Fandom fandom) {
		super(id, fandom);
		if(super.namespace != Fandom.NAMESPACE_FILE) System.err.println("FandomPage with ID " + id + " and title \"" + title + "\" might not be a FandomImage (namespace " + namespace + " is unequal to Fandom.NAMESPACE_FILE " + Fandom.NAMESPACE_FILE + ").\nA Fandom.ReadingException might be thrown.");
	}
	
	/**
	 * Loads image information.
	 * @return this FandomImage
	 */
	public FandomImage build() {
		try {
			JSONObject jsonObject = new JSONObject(Navigator.receiveTextFromWebsite("https://" + fandom.baseUrl + "/api.php?action=query&pageids=" + id + "&prop=imageinfo&iiprop=url|timestamp|userid|comment|canonicaltitle|size|dimensions|sha1|mime|mediatype|metadata|bitdepth&format=json")).getJSONObject("query").getJSONObject("pages").getJSONObject(id + "");
			jsonObject = jsonObject.getJSONArray("imageinfo").getJSONObject(0);
			size = jsonObject.getLong("size");
			width = jsonObject.getInt("width");
			height = jsonObject.getInt("height");
			timestamp = jsonObject.getString("timestamp");
			userId = jsonObject.getLong("userid");
			comment = jsonObject.getString("comment");
			rawImageUrl = jsonObject.getString("url");
			sha1 = jsonObject.getString("sha1");
			mime = jsonObject.getString("mime");
			mediaType = jsonObject.getString("mediatype");
			bitDepth = jsonObject.getInt("bitdepth");
		} catch(Exception e) {
			if(Fandom.DEBUG) e.printStackTrace();
			throw new ReadingException("Can't read image information (set static Fandom.DEBUG to true to print the exact exception stack trace)\n(This exception might occur if the page is not really an image page.)");
		}
		return this;
	}
	
	/**
	 * Returns the image size.<p>
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @return the size
	 * @see #getWidth()
	 * @see #getHeight()
	 */
	public long getSize() {
		return size;
	}
	
	/**
	 * Returns the image width.<p>
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @return the width
	 * @see #getHeight()
	 * @see #getSize()
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns the image height.<p>
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @return the height
	 * @see #getWidth()
	 * @see #getSize()
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the timestamp.<p>
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Returns the comment.<p>
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Returns the uploader's user ID.<p>
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @see #getUser()
	 * @return the user ID
	 */
	public long getUserId() {
		return userId;
	}
	
	/**
	 * Returns the uploader as {@link FandomUser}.<p>
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @see #getUserId()
	 * @return the user
	 */
	public FandomUser getUser() {
		return new FandomUser(userId, fandom);
	}
	
	/**
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @return sha1
	 */
	public String getSha1() {
		return sha1;
	}
	
	/**
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @return mime
	 */
	public String getMime() {
		return mime;
	}
	
	/**
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @return the media type
	 */
	public String getMediaType() {
		return mediaType;
	}
	
	/**
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @return the bit depth
	 */
	public int getBitDepth() {
		return bitDepth;
	}
	
	/**
	 * <b>Warning:</b> might return {@code null} or a NullPointerException if {@link #build()} has not been executed.
	 * @return the raw URL
	 */
	public String getRawUrl() {
		return rawImageUrl;
	}

}

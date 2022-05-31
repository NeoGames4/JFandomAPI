package Fandom;

/**
 * Can be used to set some search options.
 * @author Mika Thein
 * @see FandomSearch
 * @see #FandomSearchOptions()
 * @see #setNamespaces(String)
 * @see #setLimit(int)
 * @see #setOffset(int)
 * @see #setType(int)
 * @see #setSortPriority(int)
 */
public class FandomSearchOptions {
	
	private String namespaces;		// standard: 0, all: *
	private int limit = 500;		// max 500
	private int offset = 0;			// offset
	public static final int TITLE = 0;
	public static final int TEXT = 1;
	public static final int NEARMATCH = 2;
	private int what = -1;
	public static final int RELEVANCE = 0;
	private int sort = -1;			// "relevance"
	
	/**
	 * {@link #FandomSearchOptions()} can be used to set preferences for a {@link FandomSearch}.
	 * @see #setNamespaces(String)
	 * @see #setLimit(int)
	 * @see #setOffset(int)
	 * @see #setType(int)
	 * @see #setSortPriority(int)
	 */
	public FandomSearchOptions() {}
	
	/**
	 * Sets the namespaces the search algorithm is supposed to be looking for.
	 * <p>Example:<br>
	 * {@code 0|1|2}<br>
	 * (for namespace 0, 1 and 2)
	 * <p>Use "{@code *}" for all namespaces.
	 * @param namespaces the namespaces
	 * @return this instance
	 */
	public FandomSearchOptions setNamespaces(String namespaces) {
		this.namespaces = namespaces;
		return this;
	}
	
	/**
	 * Sets the limit of results (0 to 500).
	 * @param limit the limit of search results
	 * @return this instance
	 */
	public FandomSearchOptions setLimit(int limit) {
		this.limit = Math.max(limit, 0);
		return this;
	}
	
	/**
	 * Sets the offset.
	 * @param offset the offset
	 * @return this instance
	 */
	public FandomSearchOptions setOffset(int offset) {
		this.offset = Math.max(offset, 0);
		return this;
	}
	
	/**
	 * Sets what the algorithm is supposed to search.
	 * @param type the type (please use one of the constants below)
	 * @see #TITLE
	 * @see #TEXT
	 * @see #NEARMATCH
	 * @return this instance
	 */
	public FandomSearchOptions setType(int type) {
		what = type;
		return this;
	}
	
	/**
	 * Sets the priority of order.
	 * @param priority the priority (please use one of the constants below)
	 * @see #RELEVANCE
	 * @return this instance
	 */
	public FandomSearchOptions setSortPriority(int priority) {
		sort = priority;
		return this;
	}
	
	/**
	 * @see #setNamespaces(String)
	 * @return the namespaces
	 */
	public String getNamespaces() {
		return namespaces;
	}
	
	/**
	 * @see #setLimit(int)
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @see #setOffset(int)
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	
	/**
	 * @see #setType(int)
	 * @return the type
	 */
	public int getType() {
		return what;
	}
	
	/**
	 * @see #setSortPriority(int)
	 * @return the priority of order
	 */
	public int getSortPriority() {
		return sort;
	}
	
}

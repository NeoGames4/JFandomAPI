package Fandom;

/**
 * The element class can easily store values in relation to a key.
 * @author Mika Thein
 * @version 1.0
 */
public class Element {
	
	public final String key;
	public final Object value;
	
	/**
	 * Creates a new element instance.
	 * @param key the key
	 * @param value the value
	 */
	public Element(String key, Object value) {
		this.key = key;
		this.value = value;
	}

}

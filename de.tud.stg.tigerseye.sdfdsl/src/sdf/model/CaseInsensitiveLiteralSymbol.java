package sdf.model;

/**
 * Internal helper class required by the SDF DSL.
 * 
 * <p>(Otherwise case sensitive and case insensitive literals cannot be distinguished by the
 * generated grammar.)
 * 
 * <p><b>Do not use this class manually.</b> Instead use {@link LiteralSymbol} directly.
 * 
 * @author Pablo Hoch
 * @see LiteralSymbol
 *
 */
public class CaseInsensitiveLiteralSymbol extends LiteralSymbol {

	public CaseInsensitiveLiteralSymbol(String text) {
		super(getStringContents(text), false);
	}
	
	public CaseInsensitiveLiteralSymbol(String text, String label) {
		super(getStringContents(text), false, label);
	}
	
	/**
	 * Returns the string inside the quotation marks.
	 * Since this class is used by the type handler, the text contains
	 * both the quotation marks around the string as well as escape codes.
	 * E.g. for the case-insensitive literal 'hello\'world', this class
	 * would be created with text = "'hello\\'world'".
	 * This method removes the quotation marks around the string and
	 * processes the escapes. For the example above, it would return "hello'world".
	 * 
	 * @param input
	 * @return
	 */
	private static String getStringContents(String input) {
		// strip quotation marks
		String result = input.substring(1, input.length() - 1);
		// replace escapes
		result = result.replace("\\'", "'");
		result = result.replace("\\\\", "\\");
		return result;
	}

}

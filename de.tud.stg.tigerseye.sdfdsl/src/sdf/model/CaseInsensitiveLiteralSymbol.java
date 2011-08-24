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
		super(stripQuotationMarks(text), false);
	}
	
	public CaseInsensitiveLiteralSymbol(String text, String label) {
		super(stripQuotationMarks(text), false, label);
	}
	
	private static String stripQuotationMarks(String input) {
		return input.substring(1, input.length() - 1);
	}

}

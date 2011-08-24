package sdf.model;

import java.util.ArrayList;

/**
 * Specifies a list of context-free start symbols.
 *
 * <p>SDF Syntax:
 * <pre>
 * context-free start-symbols
 *   <i>Symbol*</i>
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.startsymbols">SDF Documentation</a>
 *
 */
public class ContextFreeStartSymbols extends StartSymbols {

	public ContextFreeStartSymbols(ArrayList<Symbol> symbols) {
		super(symbols);
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitContextFreeStartSymbols(this, o);
	}

}

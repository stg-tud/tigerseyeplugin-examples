package sdf.model;

import java.util.ArrayList;

/**
 * Specifies a list of lexical start symbols.
 *
 * <p>SDF Syntax:
 * <pre>
 * lexical start-symbols
 *   <i>Symbol*</i>
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.startsymbols">SDF Documentation</a>
 *
 */
public class LexicalStartSymbols extends StartSymbols {

	public LexicalStartSymbols(ArrayList<Symbol> symbols) {
		super(symbols);
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitLexicalStartSymbols(this, o);
	}

}

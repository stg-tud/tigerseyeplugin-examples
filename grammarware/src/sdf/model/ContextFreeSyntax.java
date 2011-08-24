package sdf.model;

import java.util.ArrayList;

/**
 * A context-free syntax specification consists of a list of productions.
 * Productions in a context-free syntax are processed so that the <tt>LAYOUT?</tt> symbol
 * is inserted between all symbols on the left-hand side of productions.
 * 
 * <p>SDF Syntax:
 * <pre>
 * context-free syntax
 *   <i>Production*</i>
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.contextfreesyntax">SDF Documentation</a>
 * @see LexicalSyntax
 *
 */
public class ContextFreeSyntax extends Syntax {

	public ContextFreeSyntax(ArrayList<Production> productions) {
		super(productions);
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitContextFreeSyntax(this, o);
	}
	

}

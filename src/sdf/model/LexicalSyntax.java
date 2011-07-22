package sdf.model;

import java.util.ArrayList;

/**
 * A lexical syntax specification consists of a list of productions.
 * 
 * <p>SDF Syntax:
 * <pre>
 * lexical syntax
 *   <i>Production*</i>
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.lexicalsyntax">SDF Documentation</a>
 * @see ContextFreeSyntax
 *
 */
public class LexicalSyntax extends Syntax {

	public LexicalSyntax(ArrayList<Production> productions) {
		super(productions);
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitLexicalSyntax(this, o);
	}

}

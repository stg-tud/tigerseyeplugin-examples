package sdf.model;

import java.util.ArrayList;

/**
 * Superclass for lexical and context-free syntax definitions.
 * Syntax definitions consist of a number of productions.
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.lexicalandcontextfreesyntax">SDF Documentation</a>
 * @see LexicalSyntax
 * @see ContextFreeSyntax
 *
 */
public abstract class Syntax extends GrammarElement {
	ArrayList<Production> productions;

	public Syntax(ArrayList<Production> productions) {
		super();
		this.productions = productions;
	}

	public ArrayList<Production> getProductions() {
		return productions;
	}

	public void setProductions(ArrayList<Production> productions) {
		this.productions = productions;
	}
	
	
}

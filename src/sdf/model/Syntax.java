package sdf.model;

import java.util.ArrayList;

/**
 * Superclass for lexical and context-free syntax definitions.
 * Syntax definitions consist of a number of productions.
 * 
 * @author Pablo Hoch
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

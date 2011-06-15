package sdf.model;

import java.util.ArrayList;

/**
 * Superclass for lexical and context-free start symbols.
 * Specifies a list of start symbols.
 * 
 * @author Pablo Hoch
 *
 */
public abstract class StartSymbols extends GrammarElement {

	ArrayList<Symbol> symbols;

	public StartSymbols(ArrayList<Symbol> symbols) {
		super();
		this.symbols = symbols;
	}

	public ArrayList<Symbol> getSymbols() {
		return symbols;
	}

	public void setSymbols(ArrayList<Symbol> symbols) {
		this.symbols = symbols;
	}

	
	
}

package sdf.model;

import java.util.ArrayList;

/**
 * A sorts declaration declares a list of {@link SortSymbol sort symbols}.
 * 
 * <p>SDF Syntax:
 * <pre>
 * sorts
 *   <i>SortSymbol*</i>
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.sorts">SDF Documentation</a>
 * @see SortSymbol
 *
 */
public class Sorts extends GrammarElement {
	ArrayList<SortSymbol> symbols;

	public Sorts(ArrayList<SortSymbol> symbols) {
		super();
		this.symbols = symbols;
	}

	public ArrayList<SortSymbol> getSymbols() {
		return symbols;
	}

	public void setSymbols(ArrayList<SortSymbol> symbols) {
		this.symbols = symbols;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitSorts(this, o);
	}
	
	
}

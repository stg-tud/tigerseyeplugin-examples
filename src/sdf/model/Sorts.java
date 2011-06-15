package sdf.model;

import java.util.ArrayList;

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

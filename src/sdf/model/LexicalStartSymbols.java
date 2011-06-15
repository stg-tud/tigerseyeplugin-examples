package sdf.model;

import java.util.ArrayList;

public class LexicalStartSymbols extends StartSymbols {

	public LexicalStartSymbols(ArrayList<Symbol> symbols) {
		super(symbols);
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitLexicalStartSymbols(this, o);
	}

}

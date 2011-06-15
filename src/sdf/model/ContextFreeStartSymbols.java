package sdf.model;

import java.util.ArrayList;

public class ContextFreeStartSymbols extends StartSymbols {

	public ContextFreeStartSymbols(ArrayList<Symbol> symbols) {
		super(symbols);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitContextFreeStartSymbols(this, o);
	}

}

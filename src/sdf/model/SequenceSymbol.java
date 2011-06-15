package sdf.model;

import java.util.ArrayList;

public class SequenceSymbol extends Symbol {

	ArrayList<Symbol> symbols;

	public SequenceSymbol(ArrayList<Symbol> symbols) {
		super();
		this.symbols = symbols;
	}

	public ArrayList<Symbol> getSymbols() {
		return symbols;
	}

	public void setSymbols(ArrayList<Symbol> symbols) {
		this.symbols = symbols;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitSequenceSymbol(this, o);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		
		boolean first = true;
		for (Symbol sym : symbols) {
			if (!first) sb.append(" ");
			
			sb.append(sym.toString());
			
			first = false;
		}
		
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbols == null) ? 0 : symbols.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SequenceSymbol other = (SequenceSymbol) obj;
		if (symbols == null) {
			if (other.symbols != null)
				return false;
		} else if (!symbols.equals(other.symbols))
			return false;
		return true;
	}
	
	
}

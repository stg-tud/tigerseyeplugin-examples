package sdf.model;

import java.util.ArrayList;

/**
 * A sequence symbol consists of a list of symbols that must occur in the given order.
 * The list can also be empty.
 * 
 * <p>SDF Syntax:
 * <pre>
 * (<i>Symbol*</i>)
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.sequence">SDF Documentation</a>
 *
 */
public class SequenceSymbol extends Symbol {

	ArrayList<Symbol> symbols;

	public SequenceSymbol(ArrayList<Symbol> symbols) {
		super();
		this.symbols = symbols;
	}
	
	public SequenceSymbol(ArrayList<Symbol> symbols, String label) {
		this(symbols);
		this.setLabel(label);
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

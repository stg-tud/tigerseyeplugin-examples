package sdf.model;

import java.util.ArrayList;

/**
 * The tuple operator describes the grouping of a sequence of symbols of a fixed
 * length into a tuple. The notation for tuples is {@code < , , >}, i.e., a
 * comma-separated list of elements enclosed in angle brackets. For example,
 * {@code <Bool, Int, Id>} describes a tuple with three elements consisting of a
 * Bool, an Int and an Id (in that order). For instance, {@code <true, 3, x>} is
 * a valid example of such a tuple.
 * 
 * <p>
 * Tuple is one of the few symbols that actually introduce a fixed syntax, i.e.
 * the angular brackets. You may consider them as an arbitrary shorthand.
 * 
 * <p>
 * For {@code <A,B>} SDF generates:
 * 
 * <pre>{@code "<" A "," B ">" -> <A,B>}</pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.sdf-tuples">SDF Documentation</a>
 * 
 */
public class TupleSymbol extends Symbol {

	ArrayList<Symbol> symbols;

	public TupleSymbol(ArrayList<Symbol> symbols) {
		super();
		this.symbols = symbols;
	}

	public TupleSymbol(ArrayList<Symbol> symbols, String label) {
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
		return visitor.visitTupleSymbol(this, o);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");

		boolean first = true;
		for (Symbol sym : symbols) {
			if (!first)
				sb.append(",");

			sb.append(sym.toString());

			first = false;
		}

		sb.append(">");
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
		TupleSymbol other = (TupleSymbol) obj;
		if (symbols == null) {
			if (other.symbols != null)
				return false;
		} else if (!symbols.equals(other.symbols))
			return false;
		return true;
	}

}

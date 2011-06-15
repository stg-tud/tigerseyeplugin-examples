package sdf.model;

/**
 * An optional symbol may occur 0 or 1 times.
 * 
 * SDF syntax:
 * <code>
 * &lt;Symbol&gt;?
 * </code>
 * 
 * @author Pablo Hoch
 *
 */
public class OptionalSymbol extends Symbol {
	Symbol symbol;

	public OptionalSymbol(Symbol symbol) {
		super();
		this.symbol = symbol;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitOptionalSymbol(this, o);
	}
	
	@Override
	public String toString() {
		return symbol + "?";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		OptionalSymbol other = (OptionalSymbol) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	
	
	
}

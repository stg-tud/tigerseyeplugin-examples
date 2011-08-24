package sdf.model;

/**
 * The repetition operators applied to a symbol match an arbitrary number of
 * occurences of the inner symbol (at least one occurence may be required
 * depending on the value of <tt>atLeastOnce</tt>).
 * 
 * <p>SDF Syntax (at least 0 occurences):
 * <pre>
 * <i>Symbol</i>*
 * </pre>
 *
 * <p>SDF Syntax (at least 1 occurence):
 * <pre>
 * <i>Symbol</i>+
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.lists">SDF Documentation</a>
 * @see ListSymbol
 * @see OptionalSymbol
 * 
 */
public class RepetitionSymbol extends Symbol {
	Symbol symbol;
	boolean atLeastOnce;

	public RepetitionSymbol(Symbol symbol, boolean atLeastOnce) {
		super();
		this.symbol = symbol;
		this.atLeastOnce = atLeastOnce;
	}

	public RepetitionSymbol(Symbol symbol, boolean atLeastOnce, String label) {
		this(symbol, atLeastOnce);
		this.setLabel(label);
	}
	
	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public boolean isAtLeastOnce() {
		return atLeastOnce;
	}

	public void setAtLeastOnce(boolean atLeastOnce) {
		this.atLeastOnce = atLeastOnce;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitRepetitionSymbol(this, o);
	}

	@Override
	public String toString() {
		return symbol + (atLeastOnce ? "+" : "*");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (atLeastOnce ? 1231 : 1237);
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
		RepetitionSymbol other = (RepetitionSymbol) obj;
		if (atLeastOnce != other.atLeastOnce)
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	
	
}

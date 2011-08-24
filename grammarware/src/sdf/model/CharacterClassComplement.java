package sdf.model;

/**
 * Character class complement operator.
 * Accepts all characters not in the inner character class.
 * 
 * <p>SDF Syntax:
 * <pre>
 * ~<i>CharacterClass</i>
 * </pre>
 * 
 * <p>Example:
 * <pre>
 * ~[0-9]
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.characters">SDF Documentation</a>
 *
 */
public class CharacterClassComplement extends CharacterClass {

	CharacterClass symbol;

	public CharacterClassComplement(CharacterClass symbol) {
		super();
		this.symbol = symbol;
	}
	
	public CharacterClassComplement(CharacterClass symbol, String label) {
		this(symbol);
		this.setLabel(label);
	}

	public CharacterClass getSymbol() {
		return symbol;
	}

	public void setSymbol(CharacterClass symbol) {
		this.symbol = symbol;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitCharacterClassComplement(this, o);
	}
	
	@Override
	public String toString() {
		return "~" + symbol;
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
		CharacterClassComplement other = (CharacterClassComplement) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	@Override
	public String getRegexpPattern() {
		return "^" + symbol.getRegexpPattern();
	}

	
}

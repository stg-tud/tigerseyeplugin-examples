package sdf.model;

/**
 * A sort symbol is a non-terminal.
 * Sort names always start with a capital letter and may be followed by letters and/or digits. Hyphens (-) may be embedded in a sort name.
 * 
 * <p>Example:
 * <pre>
 * Statement
 * </pre>
 * 
 * Note that the sort symbol named <tt>LAYOUT</tt> is special symbol used to define layout characters.
 * It is automatically inserted between symbols on the left-hand side of context-free productions (as <tt>LAYOUT?</tt>).
 * It has to be defined by the user in a lexical syntax section (e.g. {@code [\ \t\n] -> LAYOUT}).
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.sort">SDF Documentation</a>
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.layout">LAYOUT Symbol</a>
 * 
 */
public class SortSymbol extends Symbol {
	String name;
	
	public SortSymbol(String name) {
		super();
		this.name = name;
	}
	
	public SortSymbol(String name, String label) {
		this(name);
		this.setLabel(label);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitSortSymbol(this, o);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SortSymbol other = (SortSymbol) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	// TODO: parameters

}

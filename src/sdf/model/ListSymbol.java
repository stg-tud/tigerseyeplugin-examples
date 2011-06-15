package sdf.model;

public class ListSymbol extends Symbol {

	Symbol element;
	Symbol seperator;
	boolean atLeastOnce;

	public ListSymbol(Symbol element, Symbol seperator, boolean atLeastOnce) {
		super();
		this.element = element;
		this.seperator = seperator;
		this.atLeastOnce = atLeastOnce;
	}

	public Symbol getElement() {
		return element;
	}

	public void setElement(Symbol element) {
		this.element = element;
	}

	public Symbol getSeperator() {
		return seperator;
	}

	public void setSeperator(Symbol seperator) {
		this.seperator = seperator;
	}

	public boolean isAtLeastOnce() {
		return atLeastOnce;
	}

	public void setAtLeastOnce(boolean atLeastOnce) {
		this.atLeastOnce = atLeastOnce;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitListSymbol(this, o);
	}

	@Override
	public String toString() {
		return "{" + element + " " + seperator + "}" + (atLeastOnce ? "+" : "*");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (atLeastOnce ? 1231 : 1237);
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		result = prime * result
				+ ((seperator == null) ? 0 : seperator.hashCode());
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
		ListSymbol other = (ListSymbol) obj;
		if (atLeastOnce != other.atLeastOnce)
			return false;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		if (seperator == null) {
			if (other.seperator != null)
				return false;
		} else if (!seperator.equals(other.seperator))
			return false;
		return true;
	}
	
	
}

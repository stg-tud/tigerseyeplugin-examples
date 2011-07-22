package sdf.model;

/**
 * A literal symbol is a terminal that matches the given string.
 * Literal symbols can be either case sensitive or case insensitive.
 * 
 * <p>Example (case sensitive):
 * <pre>
 * "test"
 * </pre>
 * 
 * <p>Example (case insensitive):
 * <pre>
 * 'test'
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.literals">SDF Documentation</a>
 *
 */
public class LiteralSymbol extends Symbol {
	String text;
	boolean caseSensitive;

	public LiteralSymbol(String text, boolean caseSensitive) {
		this.text = text;
		this.caseSensitive = caseSensitive;
	}

	public LiteralSymbol(String text, boolean caseSensitive, String label) {
		this(text, caseSensitive);
		this.setLabel(label);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitLiteralSymbol(this, o);
	}

	@Override
	public String toString() {
		// TODO: escape
//		String escaped = text.replaceAll("\\", "\\\\").replaceAll("\"", "\\\"").replaceAll("'", "\\'");
		String escaped = text;
		if (caseSensitive) {
			return "\"" + escaped + "\"";
		} else {
			return "'" + escaped + "'";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (caseSensitive ? 1231 : 1237);
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LiteralSymbol))
			return false;
		LiteralSymbol other = (LiteralSymbol) obj;
		if (caseSensitive != other.caseSensitive)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
}

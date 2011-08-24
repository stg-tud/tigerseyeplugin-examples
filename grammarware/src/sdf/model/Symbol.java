package sdf.model;

/**
 * Superclass for all SDF symbols
 * All symbols can have an optional label (SDF syntax: <tt><i>label</i>:<i>Symbol</i></tt>).
 * Symbols can be compared using the {@link #equals(Object) equals} method.
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.symbols">SDF Documentation</a>
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.labels">Labelled symbols</a>
 *
 */
public abstract class Symbol extends SdfElement {
	/**
	 * Label for the symbol. Can be null (default) or a String.
	 * Example (left and right are labels for the sort symbols):
	 * <pre>left:Expr "+" right:Expr -&gt; Expr</pre>
	 */
	String label;

	@Override
	public abstract boolean equals(Object other);
	
	@Override
	public abstract int hashCode();

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}

package sdf.model;

/**
 * An alias declaration, contained inside of an {@link Aliases aliases section}, declares a new name for a complex symbol.
 * 
 * <p>SDF Syntax:
 * <pre>
 * <i>Symbol</i> -&gt; <i>Symbol</i>
 * </pre>
 * 
 * <p>Example (BracketOpen is declared as a new name for the symbol on the left):
 * <pre>
 * aliases
 *   ("{" | "&lt;:") -&gt; BracketOpen
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.aliases">SDF Documentation</a>
 * @see Aliases
 *
 */
public class Alias extends SdfElement {

	Symbol original;
	Symbol aliasName;

	public Alias(Symbol original, Symbol aliasName) {
		super();
		this.original = original;
		this.aliasName = aliasName;
	}

	public Symbol getOriginal() {
		return original;
	}

	public void setOriginal(Symbol original) {
		this.original = original;
	}

	public Symbol getAliasName() {
		return aliasName;
	}

	public void setAliasName(Symbol aliasName) {
		this.aliasName = aliasName;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitAlias(this, o);
	}

}

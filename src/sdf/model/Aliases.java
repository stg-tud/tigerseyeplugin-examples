package sdf.model;

import java.util.ArrayList;

/**
 * An aliases section defines alias names for more complex symbols.
 * It consists of a list of {@link Alias alias definitions}.
 * 
 * <p>SDF Syntax:
 * <pre>
 * aliases
 *   <i>Alias*</i>
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.aliases">SDF Documentation</a>
 * @see Alias
 *
 */
public class Aliases extends GrammarElement {

	ArrayList<Alias> aliasList;

	public Aliases(ArrayList<Alias> aliasList) {
		super();
		this.aliasList = aliasList;
	}

	public ArrayList<Alias> getAliasList() {
		return aliasList;
	}

	public void setAliasList(ArrayList<Alias> aliasList) {
		this.aliasList = aliasList;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitAliases(this, o);
	}

}

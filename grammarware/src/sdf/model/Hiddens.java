package sdf.model;

import java.util.ArrayList;

/**
 * A hiddens section contains a list of {@link GrammarElement grammar elements}.
 * Hiddens sections are <b>not</b> copied to modules that import the module containing the section.
 * 
 * <p>SDF Syntax:
 * <pre>
 * hiddens <i>GrammarElement*</i>
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.hiddensandexports">SDF Documentation</a>
 * @see ExportOrHiddenSection
 * @see GrammarElement
 *
 */
public class Hiddens extends ExportOrHiddenSection {

	public Hiddens(ArrayList<GrammarElement> grammarElements) {
		super(grammarElements);
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitHiddens(this, o);
	}

}

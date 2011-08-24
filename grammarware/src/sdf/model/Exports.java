package sdf.model;

import java.util.ArrayList;

/**
 * An exports section contains a list of {@link GrammarElement grammar elements}.
 * Exports sections are copied to modules that import the module containing the section.
 * 
 * <p>SDF Syntax:
 * <pre>
 * exports <i>GrammarElement*</i>
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.hiddensandexports">SDF Documentation</a>
 * @see ExportOrHiddenSection
 * @see GrammarElement
 *
 */
public class Exports extends ExportOrHiddenSection {

	public Exports(ArrayList<GrammarElement> grammarElements) {
		super(grammarElements);
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitExports(this, o);
	}

}

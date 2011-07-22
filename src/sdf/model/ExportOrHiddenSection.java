package sdf.model;

import java.util.ArrayList;

/**
 * Superclass for {@link Exports exports} and {@link Hiddens hiddens} sections.
 * Contains a list of {@link GrammarElement grammar elements}.
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.hiddensandexports">SDF Documentation</a>
 * @see Exports
 * @see Hiddens
 *
 */
public abstract class ExportOrHiddenSection extends SdfElement {
	ArrayList<GrammarElement> grammarElements;

	public ExportOrHiddenSection(ArrayList<GrammarElement> grammarElements) {
		super();
		this.grammarElements = grammarElements;
	}

	public ArrayList<GrammarElement> getGrammarElements() {
		return grammarElements;
	}

	public void setGrammarElements(ArrayList<GrammarElement> grammarElements) {
		this.grammarElements = grammarElements;
	}
	
	
}

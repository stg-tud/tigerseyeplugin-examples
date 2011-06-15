package sdf.model;

import java.util.ArrayList;

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

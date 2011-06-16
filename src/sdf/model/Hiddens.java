package sdf.model;

import java.util.ArrayList;

public class Hiddens extends ExportOrHiddenSection {

	public Hiddens(ArrayList<GrammarElement> grammarElements) {
		super(grammarElements);
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitHiddens(this, o);
	}

}

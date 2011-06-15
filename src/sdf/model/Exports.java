package sdf.model;

import java.util.ArrayList;

public class Exports extends ExportOrHiddenSection {

	public Exports(ArrayList<GrammarElement> grammarElements) {
		super(grammarElements);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitExports(this, o);
	}

}

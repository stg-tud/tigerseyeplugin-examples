package sdf.model;

import java.util.ArrayList;

public class ContextFreeSyntax extends Syntax {

	public ContextFreeSyntax(ArrayList<Production> productions) {
		super(productions);
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitContextFreeSyntax(this, o);
	}
	

}

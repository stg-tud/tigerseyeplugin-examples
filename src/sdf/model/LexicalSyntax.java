package sdf.model;

import java.util.ArrayList;

public class LexicalSyntax extends Syntax {

	public LexicalSyntax(ArrayList<Production> productions) {
		super(productions);
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitLexicalSyntax(this, o);
	}

}

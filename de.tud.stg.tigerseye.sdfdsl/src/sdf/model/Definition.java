package sdf.model;

import java.util.ArrayList;

public class Definition extends SdfElement {
	// Module+
	// TODO: HashMap?
	ArrayList<Module> modules;

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitDefinition(this, o);
	}
}

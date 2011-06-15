package sdf.model;

import java.util.ArrayList;

public class Imports extends GrammarElement {

	ArrayList<Import> importList;

	public Imports(ArrayList<Import> importList) {
		super();
		this.importList = importList;
	}

	public ArrayList<Import> getImportList() {
		return importList;
	}

	public void setImportList(ArrayList<Import> importList) {
		this.importList = importList;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitImports(this, o);
	}

}

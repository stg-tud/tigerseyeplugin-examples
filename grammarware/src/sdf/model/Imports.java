package sdf.model;

import java.util.ArrayList;

/**
 * An imports statement, containing an arbitrary number of {@link Import imports}.
 * 
 * <p>SDF Syntax:
 * <pre>
 * imports <i>Import*</i>
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.imports">SDF Documentation</a>
 * @see Import
 *
 */
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

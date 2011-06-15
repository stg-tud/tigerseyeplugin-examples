package sdf.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Import extends SdfElement {

	String moduleName;
	ArrayList<Symbol> parameters;
	HashMap<Symbol, Symbol> renamings;

	public Import(String moduleName) {
		super();
		this.moduleName = moduleName;
		this.parameters = new ArrayList<Symbol>();
		this.renamings = new HashMap<Symbol, Symbol>();
	}

	public Import(String moduleName, ArrayList<Symbol> parameters,
			HashMap<Symbol, Symbol> renamings) {
		super();
		this.moduleName = moduleName;
		this.parameters = parameters;
		this.renamings = renamings;
	}

	public Import(String moduleName, ArrayList<Symbol> parameters) {
		super();
		this.moduleName = moduleName;
		this.parameters = parameters;
		this.renamings = new HashMap<Symbol, Symbol>();
	}

	public Import(String moduleName, HashMap<Symbol, Symbol> renamings) {
		super();
		this.moduleName = moduleName;
		this.parameters = new ArrayList<Symbol>();
		this.renamings = renamings;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public ArrayList<Symbol> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<Symbol> parameters) {
		this.parameters = parameters;
	}

	public HashMap<Symbol, Symbol> getRenamings() {
		return renamings;
	}

	public void setRenamings(HashMap<Symbol, Symbol> renamings) {
		this.renamings = renamings;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitImport(this, o);
	}

	@Override
	public String toString() {
		return "Import [moduleName=" + moduleName + ", parameters="
				+ parameters + ", renamings=" + renamings + "]";
	}
	
	

}

package sdf.model;

import java.util.ArrayList;

/**
 * Represents an SDF module.
 * 
 * SDF Syntax:
 * 
 * <code>
 * module &lt;ModuleName&gt;
 * &lt;ImportSection&gt;*
 * &lt;ExportOrHiddenSection&gt;*
 * </code>
 * 
 * @author Pablo Hoch
 * 
 */
public class Module extends SdfElement {
	String name;
	ArrayList<Symbol> parameters;
	ArrayList<Imports> importSections;
	ArrayList<ExportOrHiddenSection> exportOrHiddenSections;

	public Module(ModuleId name) {
		this(name.toString());
	}
	
	public Module(String name) {
		this.name = name;
		this.parameters = new ArrayList<Symbol>();
		this.importSections = new ArrayList<Imports>();
		this.exportOrHiddenSections = new ArrayList<ExportOrHiddenSection>();
	}

	public String getName() {
		return name;
	}
	
	public ArrayList<Symbol> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<Symbol> parameters) {
		this.parameters = parameters;
	}

	public ArrayList<Imports> getImportSections() {
		return importSections;
	}

	public void setImportSections(ArrayList<Imports> importSections) {
		this.importSections = importSections;
	}

	public ArrayList<ExportOrHiddenSection> getExportOrHiddenSections() {
		return exportOrHiddenSections;
	}

	public void setExportOrHiddenSections(
			ArrayList<ExportOrHiddenSection> exportOrHiddenSections) {
		this.exportOrHiddenSections = exportOrHiddenSections;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitModule(this, o);
	}

}

package sdf.model;

public class ModuleId {
	private String name;

	public ModuleId(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}

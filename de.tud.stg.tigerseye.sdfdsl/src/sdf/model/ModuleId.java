package sdf.model;

/**
 * A module name, without parameters.
 * Parameters in module declarations are specified in {@link Module Module}.
 * 
 * @author Pablo Hoch
 * @see Module
 *
 */
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

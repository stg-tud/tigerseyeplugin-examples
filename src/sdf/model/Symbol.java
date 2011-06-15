package sdf.model;

/**
 * Superclass for all SDF symbols
 * 
 * @author Pablo Hoch
 *
 */
public abstract class Symbol extends SdfElement {
	

	@Override
	public abstract boolean equals(Object other);
	
	@Override
	public abstract int hashCode();
}

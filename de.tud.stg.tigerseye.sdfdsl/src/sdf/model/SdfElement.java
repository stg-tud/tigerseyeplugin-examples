package sdf.model;

/**
 * Superclass for all elements of a SDF grammar.
 * 
 * @author Pablo Hoch
 *
 */
public abstract class SdfElement {
	public abstract Object visit(Visitor visitor, Object o);
}

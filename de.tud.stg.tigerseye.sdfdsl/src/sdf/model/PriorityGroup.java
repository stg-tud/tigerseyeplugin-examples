package sdf.model;

import java.util.ArrayList;

import aterm.ATerm;

/**
 * Specifies a priority group, consisting of one or more productions
 * and an optional associativity attribute.
 * 
 * <p>See {@link Priorities Priorities} for a detailled explanation on how prioritiy sections
 * are modelled.
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/2-sdf/sdf.html#section.priorities">SDF Documentation</a>
 * @see Priorities
 *
 */
public class PriorityGroup extends SdfElement {

	ArrayList<Production> productions;
	ATerm associativity;
	boolean transitive;
	
	public PriorityGroup(ArrayList<Production> productions) {
		this(productions, null, true);
	}

	public PriorityGroup(ArrayList<Production> productions,
			ATerm associativity, boolean transitive) {
		super();
		this.productions = productions;
		this.associativity = associativity;
		this.transitive = transitive;
	}

	public ArrayList<Production> getProductions() {
		return productions;
	}

	public void setProductions(ArrayList<Production> productions) {
		this.productions = productions;
	}

	public ATerm getAssociativity() {
		return associativity;
	}

	public void setAssociativity(ATerm associativity) {
		this.associativity = associativity;
	}

	public boolean isTransitive() {
		return transitive;
	}

	public void setTransitive(boolean transitive) {
		this.transitive = transitive;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitPriorityGroup(this, o);
	}
	
	
}

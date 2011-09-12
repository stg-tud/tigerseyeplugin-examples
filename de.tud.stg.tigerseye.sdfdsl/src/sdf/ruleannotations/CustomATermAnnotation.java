package sdf.ruleannotations;

import aterm.ATerm;
import de.tud.stg.parlex.core.IRuleAnnotation;

/**
 * This annotation is added to rules that are created from productions
 * containing custom ATerm attributes.
 * 
 * For example, given an SDF production {@code foo -> bar {cons("test")}}, the generated
 * rule would have a CustomATermAnnotation with the ATerm {@code cons("test")}.
 * An annotation is added for each custom ATerm attribute.
 * 
 * Note that the standard SDF attributes (e.g. left, right, prefer, avoid...)
 * do not result in the addition of a CustomATermAnnotation. For the standard
 * attributes, standard parlex annotations are used instead.
 * 
 * @author Pablo Hoch
 *
 */
public class CustomATermAnnotation implements IRuleAnnotation {

	private ATerm aterm;

	public CustomATermAnnotation(ATerm aterm) {
		super();
		this.aterm = aterm;
	}

	public ATerm getAterm() {
		return aterm;
	}

	public void setAterm(ATerm aterm) {
		this.aterm = aterm;
	}

	@Override
	public String toString() {
		return "CustomATermAnnotation [aterm=" + aterm + "]";
	}
	
}

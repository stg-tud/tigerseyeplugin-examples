package sdf.model;

import java.util.ArrayList;

/**
 * Specifies a lexical priorities section.
 * 
 * <p>See {@link Priorities Priorities} for a detailled explanation on how prioritiy sections
 * are modelled.
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/2-sdf/sdf.html#section.priorities">SDF Documentation</a>
 * @see Priorities
 *
 */
public class LexicalPriorities extends Priorities {

	public LexicalPriorities(ArrayList<Priority> priorities) {
		super(priorities);
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitLexicalPriorities(this, o);
	}

}

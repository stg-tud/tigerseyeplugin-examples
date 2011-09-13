package sdf.model;

import java.util.ArrayList;

/**
 * Superclass for lexical and context-free priority sections.
 * This represents a priorities section, which contains a number of
 * priorities. Priorities then consist of one or more priority groups.
 * 
 * <p>Although this seperation is not very clear from the SDF documentation,
 * it is based on the SDF syntax definition written in SDF.
 * 
 * <p>The following should explain how a priority section is represented:
 * <ul>
 * <li>{@code Priorities} represents a priority section, i.e. a {@code context-free priorities}
 *     or {@code lexical priorities} section.</li>
 * <li>Each priority section can contain a number of independent priority declarations,
 *     which are seperated by commas in SDF. Such a priority declaration is represented
 *     by {@link Priority Priority}.</li>
 * <li>A priority declaration consists of at least one group declaration. If there is just one
 *     group declaration, this can be used to specify associativity. Generally, more than one
 *     group declaration is present. In this case, the groups are seperated by "&gt;" characters
 *     in SDF, meaning that all rules in the first group have a higher priority than those in the
 *     following groups and so on.</li>
 * <li>Groups are represented by {@link PriorityGroup PriorityGroup}. A group contains at least
 *     one production. Groups may additionally have an associativity attribute, which applies to
 *     all productions in the group. In SDF syntax, groups with just one production look just like
 *     a production. Groups with more than one production are wrapped in curly brackets,
 *     optionally having an associativity attribute at the beginning inside the brackets
 *     (e.g. <code>{left: foo -&gt; bar, foo baz -&gt; bar}</code>).</li>
 * </ul>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/2-sdf/sdf.html#section.priorities">SDF Documentation</a>
 * @see LexicalPriorities
 * @see ContextFreePriorities
 * @see Priority
 * @see PriorityGroup
 *
 */
public abstract class Priorities extends GrammarElement {

	ArrayList<Priority> priorities;

	public Priorities(ArrayList<Priority> priorities) {
		super();
		this.priorities = priorities;
	}

	public ArrayList<Priority> getPriorities() {
		return priorities;
	}

	public void setPriorities(ArrayList<Priority> priorities) {
		this.priorities = priorities;
	}

}

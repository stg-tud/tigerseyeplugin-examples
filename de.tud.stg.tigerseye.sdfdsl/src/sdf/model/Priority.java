package sdf.model;

import java.util.ArrayList;

/**
 * Specifies a single priority declaration inside a priorities section.
 * 
 * <p>
 * See {@link Priorities Priorities} for a detailled explanation on how
 * prioritiy sections are modelled.
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/2-sdf/sdf.html#section.priorities">SDF Documentation</a>
 * @see Priorities
 * 
 */
public class Priority extends SdfElement {

	/**
	 * Priority groups inside this priority declaration.
	 * They are sorted in order of descending priority, i.e. the first element in the list
	 * has the highest priority and so on. (This corresponds to the way the priority
	 * declarations are written in SDF)
	 */
	ArrayList<PriorityGroup> groups;

	public Priority(ArrayList<PriorityGroup> groups) {
		super();
		this.groups = groups;
	}

	public ArrayList<PriorityGroup> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<PriorityGroup> groups) {
		this.groups = groups;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitPriority(this, o);
	}

}

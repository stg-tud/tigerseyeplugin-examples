package sdf.model;

/**
 * Superclass for character classes.
 * This includes simple character classes (e.g. [0-9a-z]) and character classes combined with operators
 * (e.g. ~[ \n] or [a-z] / [x-y])
 * 
 * @author Pablo Hoch
 *
 */
public abstract class CharacterClass extends Symbol {

	public abstract String getRegexpPattern();

}

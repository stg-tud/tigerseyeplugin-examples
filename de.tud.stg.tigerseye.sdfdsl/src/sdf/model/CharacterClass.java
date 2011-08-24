package sdf.model;

/**
 * Superclass for character classes.
 * This includes simple character classes (e.g. {@code [0-9a-z]}) and character classes combined with operators
 * (e.g. {@code ~[ \n]} or {@code [a-z] / [x-y]})
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.characters">SDF Documentation</a>
 * @see CharacterClassSymbol
 * @see CharacterClassComplement
 * @see CharacterClassDifference
 * @see CharacterClassIntersection
 * @see CharacterClassUnion
 *
 */
public abstract class CharacterClass extends Symbol {

	public abstract String getRegexpPattern();

}

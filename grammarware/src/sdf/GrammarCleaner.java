package sdf;

import java.util.HashSet;
import java.util.Iterator;
import de.tud.stg.parlex.core.*;

/**
 * GrammarCleaner removes unused categories and rules from a Grammar.
 * 
 * @author Pablo Hoch
 * @see de.tud.stg.parlex.core.Grammar
 *
 */
public class GrammarCleaner {

	/**
	 * Returns a new Grammar that is equivalent to the supplied grammar, but only contains required
	 * categories and rules.
	 * 
	 * <p>The original grammar is not modified, however Rule and Category objects are reused.
	 * 
	 * @param g	the Grammar to clean
	 * @return	a new Grammar without unused rules
	 */
	public static Grammar clean(Grammar g) {
		
		// TODO: another optimization would be to remove non-terminals that can only be expanded to epsilon
		// i.e. if there is only a rule LAYOUT? -> (empty), then LAYOUT? can be removed everywhere on the RHS
	
		Grammar cleaned = new Grammar();
		HashSet<ICategory<String>> usedCats = new HashSet<ICategory<String>>();
		HashSet<IRule<String>> remainingRules = new HashSet<IRule<String>>(g.getRules());

		// categories in start rule are used
		IRule<String> startRule = g.getStartRule();
		usedCats.add(startRule.getLhs());
		usedCats.addAll(startRule.getRhs());
		// add start rule
		cleaned.addRule(startRule);
		cleaned.setStartRule(startRule);
		
		// add all required rules + categories
		boolean changed = true;
		while (changed) {
			
			changed = false;
			
			for (Iterator<IRule<String>> it = remainingRules.iterator(); it.hasNext(); ) {
				IRule<String> rule = it.next();
				
				if (usedCats.contains(rule.getLhs())) {
					// required rule, add to cleaned grammar
					cleaned.addRule(rule);
					usedCats.addAll(rule.getRhs());
					it.remove();
					changed = true;
				}
			}
			
		}
		
		// add required categories to grammar
		for (ICategory<String> cat : usedCats) {
			cleaned.addCategory(cat);
		}
		
		return cleaned;
	}
	
}

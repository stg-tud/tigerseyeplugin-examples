package sdf;

import java.util.HashMap;

import de.tud.stg.parlex.core.Grammar;
import de.tud.stg.parlex.core.Rule;

public class GeneratedGrammar {

	private Grammar grammar;
	private HashMap<Rule,ProductionMapping> productionMappings;
	
	public GeneratedGrammar(Grammar grammar,
			HashMap<Rule, ProductionMapping> productionMappings) {
		this.grammar = grammar;
		this.productionMappings = productionMappings;
	}

	public Grammar getGrammar() {
		return grammar;
	}

	public HashMap<Rule, ProductionMapping> getProductionMappings() {
		return productionMappings;
	}
	
	public ProductionMapping getProductionMapping(Rule rule) {
		return productionMappings.get(rule);
	}

	public void setGrammar(Grammar grammar) {
		this.grammar = grammar;
	}

	public void setProductionMappings(
			HashMap<Rule, ProductionMapping> productionMappings) {
		this.productionMappings = productionMappings;
	}
	
}

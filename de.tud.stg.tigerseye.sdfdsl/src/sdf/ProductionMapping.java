package sdf;

import de.tud.stg.parlex.core.Rule;
import sdf.model.Production;

public class ProductionMapping {

	private Production production;
	private Rule generatedRule;
	private String[] symbolLabels;
	
	public ProductionMapping(Production production, Rule generatedRule) {
		this.production = production;
		this.generatedRule = generatedRule;
		this.symbolLabels = new String[generatedRule.getRhs().size()];
	}

	public Production getProduction() {
		return production;
	}

	public Rule getGeneratedRule() {
		return generatedRule;
	}
	
	public String getLabelForCategoryAtPosition(int index) {
		return symbolLabels[index];
	}
	
	public void setLabelForCategoryAtPosition(int index, String label) {
		symbolLabels[index] = label;
	}
}

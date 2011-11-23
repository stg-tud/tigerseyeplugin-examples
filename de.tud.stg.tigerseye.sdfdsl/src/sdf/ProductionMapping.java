package sdf;

import java.util.ArrayList;
import java.util.List;

import de.tud.stg.parlex.core.Rule;
import sdf.model.Production;

public class ProductionMapping {

	private Production production;
	private Rule generatedRule;
	private List<String> symbolLabels;
	
	public ProductionMapping(Production production, Rule generatedRule) {
		this.production = production;
		this.generatedRule = generatedRule;
		this.symbolLabels = new ArrayList<String>(generatedRule.getRhs().size());
	}

	public Production getProduction() {
		return production;
	}

	public Rule getGeneratedRule() {
		return generatedRule;
	}
	
	public String getLabelForCategoryAtPosition(int index) {
		try {
			return symbolLabels.get(index);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	public void setLabelForCategoryAtPosition(int index, String label) {
		symbolLabels.set(index, label);
	}
}

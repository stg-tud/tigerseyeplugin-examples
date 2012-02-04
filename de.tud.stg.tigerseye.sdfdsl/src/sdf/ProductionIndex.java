package sdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sdf.model.Production;

public class ProductionIndex {
	private List<Production> productions;
	private Map<Production,Integer> indices;
	
	public ProductionIndex() {
		this.productions = new ArrayList<Production>();
		this.indices = new HashMap<Production,Integer>();
	}
	
	public int getIndex(Production pro) {
		Integer index = indices.get(pro);
		if (index == null) {
			index = productions.size();
			productions.add(pro);
			indices.put(pro, index);
		}
		return index;
	}
	
	public Production getProduction(int index) {
		if (index >= 0 && index < productions.size()) {
			return productions.get(index);
		} else {
			return null;
		}
	}
	
	@Deprecated
	public List<Production> getList() {
		return productions;
	}
}
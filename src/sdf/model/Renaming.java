package sdf.model;

public class Renaming {
	private Symbol oldSymbol;
	private Symbol newSymbol;
	
	public Renaming(Symbol oldSymbol, Symbol newSymbol) {
		super();
		this.oldSymbol = oldSymbol;
		this.newSymbol = newSymbol;
	}

	public Symbol getOldSymbol() {
		return oldSymbol;
	}

	public void setOldSymbol(Symbol oldSymbol) {
		this.oldSymbol = oldSymbol;
	}

	public Symbol getNewSymbol() {
		return newSymbol;
	}

	public void setNewSymbol(Symbol newSymbol) {
		this.newSymbol = newSymbol;
	}
	
	@Override
	public String toString() {
		return oldSymbol + " => " + newSymbol;
	}
	
}

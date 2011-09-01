package de.tud.stg.tigerseye.example.dzoneunits

import javax.measure.unit.Unit;
import org.jscience.physics.amount.Amount;

import de.tud.stg.popart.builder.core.annotations.DSLParameter;
import de.tud.stg.popart.builder.core.annotations.DSLMethod;
import de.tud.stg.popart.dslsupport.Interpreter
import de.tud.stg.popart.eclipse.core.debug.annotations.PopartType;
import de.tud.stg.popart.eclipse.core.debug.model.keywords.PopartOperationKeyword;
import de.tud.stg.tigerseye.eclipse.core.builder.transformers.AnnotationExtractor.DoubleElementHandler;

class UnitsDSL extends Interpreter{

	static{
		//useful in order to avoid defining arithmetic operations for the language as well
		GroovyDefinitionForUnitsDSL.enableUnits()
	}

	public Object eval(HashMap map, Closure cl) {
		cl.setDelegate(this);
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		return cl.call();
	}

	@DSLMethod(production="p0__kg")
	
	public Amount kilogram(Object n){
		return amountFor(n,"kg");
	}
	
	@DSLMethod(production="p0__g")
	
	public Amount gram(Object n){
		return amountFor(n,"g");
	}


	Amount amountFor(Object value, String unit){
		String strVal;
		if(value instanceof String)
			strVal = Double.valueOf(value.toString)
	    else if (value instanceof Number)
			strVal = ((Double) value).toString()
	    else
			throw new IllegalArgumentException("Unexpected type: " + value);
			
		return Amount.valueOf(Double.valueOf(strVal), Unit.valueOf(unit))
	}

	@DSLMethod(production="p0__cm")
	
	public Amount centimeter(Object n){
		return amountFor(n,"cm")
	}

	@DSLMethod(production="p0__km")
	
	public Amount kilometer(Object n){
		return amountFor(n,"km")
	}

	@DSLMethod(production="p0__min")
	
	public Amount minutes(Object n){
		return amountFor(n,"min")
	}

	@DSLMethod(production="p0__m")
	
	public Amount meter(Object n){
		return amountFor(n,"m")
	}

	@DSLMethod(production="p0__h")
	
	public Amount hours(Object n){
		return amountFor(n,"h")
	}

	@DSLMethod(production="p0__s")
	
	public Amount seconds(Object n){
		return amountFor(n,"s")
	}

	@DSLMethod(production="p0__in")
	
	public Amount inch(Object n){
		return amountFor(n,"in")
	}

	@DSLMethod(production = "SELECT__p0__FROM__p1")
	
	public void selectFrom(String[] columns, String[] tables) {
		System.out.println("SimpleSqlDSL.selectFrom()"
				+ Arrays.toString(columns) + Arrays.toString(tables));
	}
}

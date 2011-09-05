package de.tud.stg.tigerseye.examples.simplesql;


import groovy.lang.Closure;

import java.util.Arrays;
import java.util.HashMap;

import de.tud.stg.popart.builder.core.annotations.DSLMethod;
import de.tud.stg.popart.builder.core.annotations.DSLParameter;

/**
 * {@link SimpleSqlDSL} is a small DSL modeling a very simple subset of SQL operations
 *
 * @author Kamil Erhard
 *
 */

public class SimpleSqlDSL implements de.tud.stg.popart.dslsupport.DSL {

	public Object eval(@SuppressWarnings("rawtypes") HashMap map, Closure cl) {
		cl.setDelegate(this);
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		return cl.call();
	}

	@DSLMethod(production = "SELECT__p0__FROM__p1")
	public void selectFrom(String[] columns, String[] tables) {
		System.out.println("SimpleSqlDSL.selectFrom()"
				+ Arrays.toString(columns) + Arrays.toString(tables));
	}

	@DSLMethod(production = "SELECT__p0__FROM__p1__WHERE__p2")	
	public void selectFromWhere(String[] columns, String[] tables, @DSLParameter(arrayDelimiter = "AND") String[] checks) {
		System.out.println("SimpleSqlDSL.selectFromWhere()"
				+ Arrays.toString(columns) + Arrays.toString(tables)
				+ Arrays.toString(checks));
	}
}

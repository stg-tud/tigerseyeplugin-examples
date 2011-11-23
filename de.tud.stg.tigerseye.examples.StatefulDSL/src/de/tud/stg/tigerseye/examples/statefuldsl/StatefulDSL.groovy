package de.tud.stg.tigerseye.examples.statefuldsl;

import groovy.lang.Closure

import java.util.HashMap
import java.util.Map

import de.tud.stg.tigerseye.dslsupport.annotations.DSLMethod;
import de.tud.stg.tigerseye.dslsupport.annotations.DSLMethod.DslMethodType;

/**
 * {@link StatefulDSL} is a small DSL showing the possibility of setting and retrieving variables.
 * 
 * @author Kamil Erhard
 * 
 */
public class StatefulDSL implements de.tud.stg.tigerseye.dslsupport.DSL {

	public Object eval(HashMap map, Closure cl) {
		cl.setDelegate(this);
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		return cl.call();
	}

	private Map<String, Object> variables = new HashMap<String, Object>();

	@DSLMethod(isUnicodeEncoding=true)
	public void set__p0__equals__p1(String key, Object value) {
		this.variables.put(key, value);
	}

	public Object get__p0(String key) {
		return this.variables.get(key);
	}

}

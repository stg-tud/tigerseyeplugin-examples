package de.tud.stg.tigerseye.examples.tinysql;

import java.util.List;
import java.util.Map;

import de.tud.stg.popart.eclipse.core.debug.annotations.PopartType;
import de.tud.stg.popart.eclipse.core.debug.model.keywords.PopartOperationKeyword;
import de.tud.stg.tigerseye.dslsupport.DSL;
import de.tud.stg.tigerseye.dslsupport.annotations.DSLClass;
import de.tud.stg.tigerseye.dslsupport.annotations.DSLMethod;

@DSLClass(whitespaceEscape=" ")
public interface ITinySQL extends DSL{

		@DSLMethod(production="SELECT p0 FROM p1")
		List<Map> selectFrom(String[] columns, String[] tables);

}

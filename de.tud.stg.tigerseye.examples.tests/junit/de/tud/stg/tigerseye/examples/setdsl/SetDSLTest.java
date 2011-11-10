package de.tud.stg.tigerseye.examples.setdsl;

import org.junit.ComparisonFailure;

import org.junit.Ignore;
import org.junit.Test;

import utilities.DSLTransformationTestBase;

import de.tud.stg.tigerseye.examples.setdsl.SetDSL;

public class SetDSLTest extends DSLTransformationTestBase {

	
	@Test
	public void shouldTransformNewSet() throws Exception {
		String setdsl = "setdslnew";
		doTest(setdsl);
	}

	private void doTest(String setdsl) throws Exception {
		assertTransformedDSLEqualsExpected(setdsl, SetDSL.class);
	}
	
	//worked after oracle activation
	@Test
	public void shouldTransformNewSetWithoutSemicolon() throws Exception {
		doTest("setdslnewnosemicolon");
	}
	
	@Test
	public void shouldTransformUnion() throws Exception {
		doTest("setdslunion");
	}
	
	@Test(expected=ComparisonFailure.class) // Intersection element is not transformed
	public void shouldTransformIntersection() throws Exception {
		doTest("setdslintersection");
	}
	
	//worked after oracle activation
	@Test
	public void shouldTransformNewSetAndUnion() throws Exception {
		doTest("setdslnewandunion");
	}
	
	//worked after oracle activation
	@Test
	public void shouldTransformSetVarAndUnion() throws Exception {
		doTest("setdslsetvarandunion");
	}
	
}

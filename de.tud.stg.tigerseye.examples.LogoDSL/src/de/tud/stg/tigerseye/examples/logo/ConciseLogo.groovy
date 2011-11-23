package de.tud.stg.tigerseye.examples.logo;

import de.tud.stg.tigerseye.*;
import de.tud.stg.tigerseye.dslsupport.annotations.DSLMethod;

import org.javalogo.*;
import java.awt.Color;

/**
 * This version of Logo defines shortcut keywords.
 */
public class ConciseLogo extends ExtendedLogo 
                         implements IConciseLogo {
	 
	public ConciseLogo() {
		super()
	}
	 
	/* Literals */

	/* Operations */
	@DSLMethod(production="fd__p0")
	public void fd(int n) { forward__p0(n); }
	@DSLMethod(production="bd__p0")
	public void bd(int n) { backward__p0(n); }
	@DSLMethod(production="rt__p0")
	public void rt(int n) { right__p0(n);	}
	@DSLMethod(production="lt__p0")
	public void lt(int n) { left__p0(n); }

	public void ts() { textscreen(); }	
	public void fs() { fullscreen(); }
	public void cs() { cleanscreen(); }
	
	public void ht() { hideturtle(); }
	public void st() { showturtle(); }
	
	@DSLMethod(production="setpc__p0")
	public void setpc(int n) { setpencolor__p0(n); }
	public void pu() { penup(); }
	public void pd() { pendown(); }
	
	/* Abstraction Operators */
}
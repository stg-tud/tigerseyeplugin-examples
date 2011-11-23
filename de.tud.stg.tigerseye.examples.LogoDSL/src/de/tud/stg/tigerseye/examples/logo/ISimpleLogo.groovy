package de.tud.stg.tigerseye.examples.logo;

import de.tud.stg.tigerseye.*;
import de.tud.stg.tigerseye.dslsupport.DSL;

import org.javalogo.*;
import java.awt.Color;

/**
 * This interface defines the logo toy language.
 */
interface ISimpleLogo extends DSL {
	/* Literals */
	int getBlack();
	int getBlue();
	int getRed();
	int getGreen();
	int getYellow();
	int getWhite();
		
	/* Operations */
	void forward__p0(int n);
	void backward__p0(int n);
	void right__p0(int n);
	void left__p0(int n);
	
	/* Abstraction Operators */
	void turtle(Map params, Closure coreography);
}
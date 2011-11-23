
package de.tud.stg.tigerseye.examples.logo;

import de.tud.stg.tigerseye.*;
import de.tud.stg.tigerseye.dslsupport.annotations.DSLMethod;

import org.javalogo.*;
import java.awt.Color;

/**
 * This class implements the logo toy language.
 */
public class UCBLogo extends ConciseLogo implements IUCBLogo {
	 
	public UCBLogo() {
		super();
	}
	
	/* Literals */

	/* Operations */
	
	/* Abstraction Operators */
	public void repeat__p0__p1(int _times, Closure choreography) {
        _times.times {
            choreography.call();
        }
    }
	
	void repeat(int _times, Closure choreography){
		repeat__p0__p1(_times, choreography);
	}	
}
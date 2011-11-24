package de.tud.stg.tigerseye.examples.logo;

import de.tud.stg.tigerseye.*;
import de.tud.stg.tigerseye.dslsupport.Interpreter;

import org.javalogo.*;
import java.awt.Color;

/**
 * This class implements a simplified version of the toy language Logo.
 */
public class SimpleLogo implements ISimpleLogo {
	 
	def DEBUG = false; 
	
	protected volatile TurtleGraphicsWindow myTurtleGraphicsWindow;
	protected Turtle turtle;
	
	public SimpleLogo() {
		myTurtleGraphicsWindow = new TurtleGraphicsWindow();
        myTurtleGraphicsWindow.setTitle("TurtleDSL (based on JavaLogo) "); //Set the windows title
        myTurtleGraphicsWindow.show(); //Display the window
		assert this.myTurtleGraphicsWindow != null;
		turtle();
	}
	
	public Turtle getTurtle() {
		return turtle;
	}
	
	public TurtleGraphicsWindow getCanvas() {
		return myTurtleGraphicsWindow;
	}
	
	/* Literals */
	public int getBlack() { return Color.BLACK.value; }
	public int getBlue() { return Color.BLUE.value; }
	public int getRed() { return Color.RED.value; }
	public int getGreen() { return Color.GREEN.value; }
	public int getYellow() { return Color.YELLOW.value; }
	public int getWhite() { return Color.WHITE.value; }

	/* Operations */
	public void forward__p0(int n) { turtle.forward(n);	}
	public void backward__p0(int n) { turtle.backward(n); }
	public void right__p0(int n) {
		if (DEBUG) println("Turtle.right($n) before turning, headings is $turtle.heading");
		turtle.right(n);	
		if (DEBUG) println("Turtle.right($n) after turning,headings is $turtle.heading");
	}
	public void left__p0(int n) { turtle.left(n); }

	/* Abstraction Operators */
	public void turtle(Map map, Closure cl) {
		turtle()
	}
	
	public void turtle() {
	  if (DEBUG) println("Abstraction operator: turtle");
		
	  String name = null//params["name"];
	  if (name == null) {
		  name = "Noname";
	  }
		
	  Integer color = null //params["color"];
	  if (color == null) {
		  color = Color.BLACK.value;
	  }
	  
      turtle = new Turtle(name,new Color(color)); //Create a turtle
	  println "setting turtle $turtle"
      myTurtleGraphicsWindow.add(turtle); //Put turtle in our window so bob has a place to draw
	}
	
	public setDebug(boolean debug){
		DEBUG = debug
	}
}
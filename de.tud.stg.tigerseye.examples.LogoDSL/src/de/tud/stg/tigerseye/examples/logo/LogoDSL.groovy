package de.tud.stg.tigerseye.examples.logo;
import static de.tud.stg.tigerseye.dslsupport.annotations.DSLMethod.DslMethodType.*;


import de.tud.stg.tigerseye.dslsupport.Interpreter;
import de.tud.stg.tigerseye.dslsupport.annotations.DSLMethod;
import de.tud.stg.popart.dslsupport.*;

import org.javalogo.*;
import java.awt.Color;
import javax.swing.text.MaskFormatter.LiteralCharacter;
/**
 * This class implements the Logo toy language.
 */
public class LogoDSL extends Interpreter implements ILogoDSL {

	def DEBUG = true;

	private TurtleGraphicsWindow myTurtleGraphicsWindow;
	private Turtle turtle;

	public LogoDSL() {
		if (myTurtleGraphicsWindow == null) {
			myTurtleGraphicsWindow = new TurtleGraphicsWindow();
			myTurtleGraphicsWindow.setTitle("TurtleDSL (based on JavaLogo) "); //Set the windows title
			myTurtleGraphicsWindow.show(); //Display the window
			turtle = new Turtle("Noname",java.awt.Color.BLACK); //Create a turtle
			myTurtleGraphicsWindow.add(turtle); //Put bob in our window so bob has a place to draw
		}

		turtle.setName("LittleLogo");
	}

	/* Literals */
	@DSLMethod(type=Literal)
	public int getBlack() {
		return Color.BLACK.value;
	}
	@DSLMethod(type=Literal)
	public int getBlue() {
		return Color.BLUE.value;
	}
	@DSLMethod(type=Literal)
	public int getRed() {
		return Color.RED.value;
	}
	@DSLMethod(type=Literal)
	public int getGreen() {
		return Color.GREEN.value;
	}
	@DSLMethod(type=Literal)
	public int getYellow() {
		return Color.YELLOW.value;
	}
	@DSLMethod(type=Literal)
	public int getWhite() {
		return Color.WHITE.value;
	}

	/* Operations */
	@DSLMethod()
	public void textscreen()   {
		throw new IllegalStateException("DSL Operation has not been implemented.")
	}
	@DSLMethod()
	public void ts() {
		textscreen();
	}
	@DSLMethod()
	public void fullscreen() {
		throw new IllegalStateException("DSL Operation has not been implemented.")
	}
	@DSLMethod()
	public void fs() {
		fullscreen();
	}
	@DSLMethod()
	public void home() {
		turtle.home();
	}
	@DSLMethod()
	public void clean() {
		myTurtleGraphicsWindow.clear();
	}
	@DSLMethod()
	public void cleanscreen() {
		clean(); home();
	}
	@DSLMethod()
	public void cs() {
		cleanscreen();
	}

	@DSLMethod()
	public void hideturtle() {
		turtle.hide();
	}
	@DSLMethod()
	public void ht() {
		hideturtle();
	}
	@DSLMethod()
	public void showturtle() {
		turtle.show();
	}
	@DSLMethod()
	public void st() {
		showturtle();
	}

	@DSLMethod(production="setpencolor_p0") public void setpencolor(int n) {
		turtle.setPenColor(new java.awt.Color(n));
	}
	@DSLMethod(production="setpc_p0") public void setpc(int n) {
		setpencolor(n);
	}
	@DSLMethod()
	public void penup() {
		turtle.penUp();
	}
	@DSLMethod()
	public void pu() {
		penup();
	}
	@DSLMethod()
	public void pendown() {
		turtle.penDown();
	}
	@DSLMethod()
	public void pd() {
		pendown();
	}
	@DSLMethod(production="forward__p0") public void forward(int n) {
		turtle.forward(n);
	}
	@DSLMethod(production="fd__p0") public void fd(int n) {
		forward(n);
	}	@DSLMethod(production="backward__p0") public void backward(int n) {
		turtle.backward(n);
	}
	@DSLMethod(production="bd__p0") public void bd(int n) {
		backward(n);
	}	@DSLMethod(production="right__p0") public void right(int n) {
		turtle.right(n);
	}
	@DSLMethod(production="rt__p0") public void rt(int n) {
		right(n);
	}	@DSLMethod(production="left__p0") public void left(int n) {
		turtle.left(n);
	}
	@DSLMethod(production="lt__p0") public void lt(int n) {
		left(n);
	}
}
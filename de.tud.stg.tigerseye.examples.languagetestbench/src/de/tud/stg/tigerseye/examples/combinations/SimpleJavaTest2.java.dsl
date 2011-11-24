package de.tud.stg.tigerseye.examples.combinations;
import java.util.Set;

@EDSL({"set", "sql"})
public class SimpleJavaTest2 {
	
	public SimpleJavaTest2() {
	
	}
	
	public void foo() { 
		
		Set s = {"k", "l"} ⋃ {"m", "n"};
		
		System.out.println(s);
		
		SELECT "id" FROM "students";
	}
	
	public static void main(String[] args) {
		new SimpleJavaTest2().foo();
	}
}

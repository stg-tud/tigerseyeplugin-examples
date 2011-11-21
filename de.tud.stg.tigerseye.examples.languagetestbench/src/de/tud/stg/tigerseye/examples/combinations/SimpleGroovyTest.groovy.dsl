package de.tud.stg.tigerseye.examples.combinations;
import java.util.Set;

@EDSL({"set", "sql"})
	
class SimpleGroovyTest {
	public void foo() { 
		
		def  s = {"k", "l"} ⋃ {"m", "n"}
		
		println "before intersection: $s"
		
		def t = s ⋂ { "k", "n" }
		
		println "after intersection $t" 
		
		SELECT "id" FROM "students"
	}
	
	public static void main(String[] args) {
		new SimpleGroovyTest().foo()
	}
}
package de.tud.stg.tigerseye.examples.map


/**
 * Tigerseye language: de.tud.stg.tigerseye.examples.mapdsl.MapDSL
 *
 * Declared keywords:
 *  Entry buildEntry(Object, Object)
 *  Object eval(HashMap, Closure)
 */


//needs manual import of dependency that will be necessary after transformation
import de.tud.stg.tigerseye.examples.mapdsl.Entry



map(name:'MapTest'){	

	//Currently this map implementation does not work, some changes necessary for SDF DSL broke Map
	/* 
	def hanspeter = [Integer,String: 1="hans"]
	
	
	println hanspeter
	
	def doubleint = [Integer , Integer : 0=0]
	println doubleint

	def hans = [String, Object : "name"="Hans", "lastname"="Häuser", "age"=21 , "married"=false]	
	println "hans ist $hans"
	
	def peter = [String, Object : "name"= "Peter", "lastname"="Bauer", "age"=45, "married"=true]	
	println "peter ist $peter"
	
	hans.putAll(peter) 
	assert hans.equals(peter)
	*/
	
}

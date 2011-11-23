package de.tud.stg.tigerseye.examples.sql


/**
 * Tigerseye language: de.tud.stg.tigerseye.examples.dsldefinitions.simplesqldsl.SimpleSqlDSL
 */
sql(name:'sqlblock'){

	def q12 = SELECT "NAME2","AGE" FROM "PERSONS"
		
	def q22 = SELECT "NAME4","AGE" FROM "PERSONS" WHERE "AGE>20"
	
	def q11 = selectFrom( ["NAME1","AGE"] as String[] , ["PERSONS"]  as String[] )
	def q21 = selectFromWhere(["NAME3","AGE"] as String[] , ["PERSONS"]  as String[], ["AGE>20"] as String[])
	
}


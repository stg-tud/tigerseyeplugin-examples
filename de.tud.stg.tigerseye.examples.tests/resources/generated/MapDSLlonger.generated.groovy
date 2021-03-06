def hanspeter = buildMap(
Integer.class,
String.class,
[
buildEntry(
1,
"hans"),
buildEntry(
2,
"peter")
] as Entry[])    
    
    println hanspeter
    
    def doubleint = buildMap(
Double.class,
Integer.class,
[
buildEntry(
0,
0),
buildEntry(
1,
1),
buildEntry(
2,
2),
buildEntry(
3,
3),
buildEntry(
4,
4),
buildEntry(
5,
5),
buildEntry(
6,
6),
buildEntry(
7,
7),
buildEntry(
8,
8),
buildEntry(
9,
9),
buildEntry(
10,
10)
] as Entry[])
    println doubleint
 
    def hans = buildMap(
String.class,
Object.class,
[
buildEntry(
"name",
"Hans"),
buildEntry(
"lastname",
"Häuser"),
buildEntry(
"age",
21),
buildEntry(
"married",
false)
] as Entry[])    
    println "hans ist $hans"
    
    def peter = buildMap(
String.class,
Object.class,
[
buildEntry(
"name",
"Peter"),
buildEntry(
"lastname",
"Bauer"),
buildEntry(
"age",
45),
buildEntry(
"married",
true)
] as Entry[])    
    println "peter ist $peter"
    
    hans.putAll(peter) 
    assert hans.equals(peter)
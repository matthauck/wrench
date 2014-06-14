Wrench
======

Wrench is a simple tool for java that grabs databases and maps them into objects. 

* No annotations or reflection
* No schema creation. Use something like [Flyway](http://flywaydb.org/) for that
* No attempt to be or do everything

Basic setup:

1. Create a database schema 
2. Create a model for your tables
3. Define the columns you care about on those tables
4. Implement association interfaces if you care to
5. Instaniate one `DB` object and go to town!

*see test code for examples*

Building
--------

1. Install [Gradle](http://www.gradle.org/)
1. `gradle build` will assemble the jar and run the tests.

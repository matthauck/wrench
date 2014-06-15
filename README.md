Wrench
======

Wrench is a simple tool for java that grabs databases and maps them into objects. 

*Wrench is very much still a work in progress...*

Story and Principles
--------------------

I feel a bit of a need to justify the existence of wrench, what with all the ORMs
that are out there nowadays. I hope this will help you understand where this project
is coming from and figure out whether it will be useful to you or not.  I had begun to 
play with [play](www.playframework.com) in Scala and got frustrated quickly with the 
various ORMs out there. I [tried](http://sorm-framework.org/) a [few](http://slick.typesafe.com/) 
[of](http://squeryl.org/) [them](http://www.playframework.com/documentation/2.1.0/ScalaAnorm).
My day job is in Java land and we use JPA/[hibernate](http://hibernate.org/orm/).

The pain is especially sharp as I longingly remember working in Rails using 
awesome ORMs of legend like [Sequel](http://sequel.jeremyevans.net/) and 
[ActiveRecord](https://github.com/rails/rails/tree/master/activerecord). These libraries
leave every Java ORM I've seen in the dust for their simplicity and power of expression. 
Part of this is probably due to the language, but I think Java can do better than it has thus far.

I variously found the following weaknesses:

* Not enough control over the SQL. *SQL is our friend*
* Difficulty mapping results to objects
* Difficulty working with auto-incrementing private keys
* Difficulty integrating into web frameworks
* Proliferation of DAO and manager objects 
* Associations should be fetched exactly when you want them to be

Cool things to note
-------------------

### Lambdas

I chose to base this project off java 8 so as to take advantage of lambda expressions. 
This has paid dividends already, I think, in the way the column definitions work. 
It is a bit more boilerplate code to define a column schema, when we could just use reflection 
and enforce a particular convention. But the great benefit of defining the columns is type-safety!

*more to come...?*

Usage
-----

**Basic setup:**

1. Create a database schema 
2. Create a model for your tables
3. Define the columns you care about on those tables
4. Implement association interfaces if you care to
5. Instantiate one `DB` object and go to town!

*See [test code](https://github.com/matthauck/wrench/blob/master/src/test/java/wrench/orm/DBTest.java) for now for examples*

Development
-----------

1. Install [Gradle](http://www.gradle.org/)
1. `gradle build` will assemble the jar and run the tests.


License
-------

Wrench is released under the MIT license:

[www.opensource.org/licenses/MIT](http://www.opensource.org/licenses/MIT)




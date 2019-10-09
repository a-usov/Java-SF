
# Advisor Meeting 09/10/2019

## Done

* Completed initial contextual analysis - repeated named classes/methods/fields, type checking between expressions and methods
* Added support for defining classes and methods in any order like in java
* Bool type added as found missing in last meeting
* ~12 hours spent on project in last week. 

## To be done

* Add extra necessary contextual analysis such as circular dependancy
* Start and complete code generation -  difficult to assign time estimate
* From last meeting - Investigate why single parameter methods

## Questions

* are mathematical expressions valid as field assignments
* Addressing issue found in last meeting, is a Void type actually useful/needed in a pure functional subset? Used internally but not part of the language? 
* Similarly with null values of fields, should they be allowed? or should a constructor initialise all field values?
* Again similarly, is an Empty Set class type useful/needed. If empty set is type of method parameter, its a method that can never be called and cant be combined meanigfully with a boolean type connective, so useless?

## Minutes

* Worked out that expressions that are passed to the constructor *call* but in the constructor method, they are bound to a local variable with specific type, of which the expression has to match. 
* All fields should be initialised in the constructor method, to avoid null values, inline with normal functional languages.
* The intersection between the empty set and a type does result in a type that can never be satisfied, however when the empty set is combined with not or union operator, it does provide potentially useful connective types, therefore it should be included.

## Additional tasks identified
* When implementing connective types, add the union operator as an ease of use feature. 
* Add contextual analysis checking to make sure all fields are set in the constructor of a class.

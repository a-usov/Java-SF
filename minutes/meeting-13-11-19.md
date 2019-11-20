# Advisor Meeting 13/11/2019

## Done
* Created typing prototype from the subtype map idea discussed in previous meeting
* The subtyping relation is instanciated for all base types, and then any class types are added by analysing its field and methods 

## Questions from last week addressed 

## To be done
* Implement the Polygon example in the short paper by adding the boolean connectives to the grammer and integrating the typing prototype into the contextual analysis.

## Questions

## Minutes
* Given my explanation of how I would see subtyping algorithm, the prototype allowed me to more clearly explain the intuition behind the idea, which on closer inspection, does match the set theoretic ideas of semantic subtyping.
* We discussed also how to implement the typing system for allowing to create subtyping sets by using boolean operations on method or field names.
* Initially I was unsure on how to do this, but a suggestion given was to use the set of all classes to dynamically  generate sets of classes which are in the boolean condition by analysing each class and including the classes which satisfy the condition. 

## Additional tasks identified
* A lot of the subtyping algorithms appear to be squared in their complexity, which could be a problem if programs with large amounts of classes are compiled, for example if there is a large amount of classes in a hypothetical standard library, this would increase compilation times quite a bit. 

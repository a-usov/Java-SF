# Advisor Meeting 13/11/2019

## Done
* Having done the reading and understood the theory and set theoretic intuition, I was able to come up with an idea
for a subtyping algorithm which given a program, builds a map of subtypes for each type in the language. Given an 
    expression and an expected type, the subtyping algorithm can be applied by seeing if the type of the expression exists
    in the set of subtypes for the entry of the expected type in the map.

## Questions from last week addressed 

## To be done
* Try to implement a prototype of this idea to see if it would work.

## Questions


## Minutes
* The subtyping idea seems to work on paper, so we discussed to try to implement a prototype.
* However given that we define rules for the base types, this seems to not follow the set theoretic idea of types and its values
  as its not much different than syntactic subtyping where we also establish rules about how to subtype. 

## Additional tasks identified
* The email to be sent to Alain Frisch, it was discovered he is no longer in academia so I should rewrite the email to another
  contributer of the paper to get an idea of their implementation and see if mine makes sense.

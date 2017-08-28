# Kotlin Effects 

This library provides two types: `Eff` and `Maybe`. Both represent common coding 
idioms taking from the pure functional programming paradigm. They provide a way to 
deal with side-effects *compositionally*. Both types are based on mathematical 
structures called Functors, Applicative Functors, and Monads. These structures are 
notoriously difficult to understand, but it's mostly hype. They are simple, but *very*
abstract. I try to present them here with concrete examples, so you can learn to use 
them intuitively. 

Many functional programming libraries written in non-pure functional programming 
languages often try to mimic exactly the APIs or style of programming seen in 
languages such as Haskell or OCaml. This library is an attempt to take the best and 
core parts of side-effect handling in those languages and implement them in a way 
that is natural and intuitive in Kotlin.

**Table of Contents**

- [Maybe](#maybe)
  - [Two Simple Examples](#two-simple-examples)
- [Eff](#eff)


## Maybe

### Two Simple Examples

Let's get motivated to use the `Maybe` type. In these examples we mock the functions 
to keep things simple. Later, we will write some real programs. When learning how to 
use this library, remember to focus on the types. Those are important. The data is 
generic -- it could be anything. How we create and compose the types is what makes 
these functions interesting. 

Note that this library allows us to avoid exception handling in many cases. Sometimes, 
exceptions are necessary, but often we can write cleaner and more compositional code 
with the `Maybe` type.

#### Independent Effects (Applicative Style)

In this example, we have a simple program that depends on a few data sources. All of 
the data sources are independent (the success of one does not depend on another). 

#### Dependent Effects (Monad Style)


## Eff

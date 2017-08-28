# Kotlin Effects 

This library provides two types: `Eff` and `Maybe`. Both represent common coding 
idioms taking from the pure functional programming paradigm. They provide a way to 
deal with side-effects *compositionally*. Both types are based on mathematical 
structures called Functors, Applicative Functors, and Monads. These structures are 
notoriously difficult to understand, but it's mostly hype. They are simple, but *very*
abstract. I try to present them here with concrete examples, so you can learn to use 
them intuitively. 

Many functional programming libraries written in non-pure functional programming 
languages often try to mimick exactly the APIs or style of programming seen in 
langauges such as Haskell or OCaml. This library is an attempt to take the best and 
core parts of side-effect handling in those languages and implement in a way that is 
natural and intuitive in Kotlin.

**Table of Contents**

- [Usage Examples](#usage-examples)
  - [Parsing](#parsing)
    - [Simple Record Type](#simple-record-type)
    - [Wrapped Types](#wrapped-types)
    - [Arrays](#arrays)
    - [Sum Types](#sum-types)
    - [Optional Types](#optional-types)
  - [Encoding](#encoding)
    - [Simple Record Type](#simple-record-type-1)
    - [Sum Types](#sum-types-1)
    - [Optional Types](#optional-types-1)
    - [Nested Types](#nested-types-1)
- [More Examples](#examples)


## Usage Examples

Good code examples are the most efficient way to learn to use a library. Here are examples for most 
use cases. There are some more (and larger) examples in the tests.

### Parsing

#### Simple Record Type

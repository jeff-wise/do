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


common case, want to write functions that operate on data that is there.
when applying the functions, we deal with the failure cases.

first thing I miss when leaving Haskell. also hard to do in haskell the right way.
MTL takes some type to appreciate and use proficiently (or confidently)

interface is important 

**Table of Contents**

- [Installation](#installation)
- [Why Use Kotlin Effects?](#why-use-kotlin-effects?)
- [The Types](#the-types)
  - [Maybe](#maybe)
    - [Two Simple Examples](#two-simple-examples)
  - [Eff](#eff)

## Installation

## Why Use Kotlin Effects?

## The Types

### Maybe

#### Two Simple Examples

Let's get motivated to use the `Maybe` type. In these examples we mock the functions 
to keep things simple. Later, we will write some real programs. When learning how to 
use this library, remember to focus on the types. Those are important. The data is 
generic -- it could be anything. How we create and compose the types is what makes 
these functions interesting. 

Note that this library allows us to avoid exception handling in many cases. Sometimes, 
exceptions are necessary, but often we can write cleaner and more compositional code 
with the `Maybe` type.

##### Independent Effects (Applicative Style)

In this example, we have a simple program that depends on a few data sources. All of 
the data sources are independent (the success of one does not depend on another). 

Normally, in these scenarios, we end up with a lot of nested code and if statements 
that are hard to read. `apply` with the Maybe type allows us to write functions as if 
errors did not occur i.e. with the assumption that the inputs are non-null. It handles 
checking each input for us and failing if any of the inputs do not exist.

On a small-scale, this ensures code that is easy to read. On a large scale, as we will 
see later, it becomes even more useful because it allows us to write a bunch of 
functions which may fail, and easily combine them with other functions that may fail, 
all at an arbitrary level of nesting.

```kotlin

// The file operations our program supports.
// When using algebraic data types in a functional language, it's common to 
// define types for everything. Explicit data always makes programs easier 
// to read and maintain.
sealed class FileCommand

class DeleteFile : FileCommand()

class DeleteLine(val lineNumber : Int) : FileCommand()


// Read some file. Could fail for many reasons e.g. doesn't exist, no read privileges
fun readFile() : Maybe<String> = Nothing()

fun writeFile(fileString : String) { }

// Parse a command from the user. Could fail if command is typed incorrectly.
fun parseUserCommand() : Maybe<FileCommand> = Just(DeleteLine(4))

// Given a file string and a command, perform an edit operation to the file. Note: The
// parameters cannot be null.
fun modifyFile(fileString : String, command : FileCommand) : String = ""

// Apply the modify function to inputs which may or may not exist. This implies that the
// result may or may not exist. We will have to check that, but we do not have to
// verify if the parameters exist. If any parameter is missing, the function will fail.
val newFileString : Maybe<String> = apply(::modifyFile, readFile(), parseUserCommand())

when (newFileString) {
    // A result exists, we can access it with .value
    is Just    -> writeFile(newFileString.value)
    // No result exists, one of the inputs was Nothing. Note that we don't know which
    // one. For better error messages, the Eff type is more useful.
    is Nothing -> System.out.println("An error occurred.")
}
```

##### Dependent Effects (Monad Style)


### Eff

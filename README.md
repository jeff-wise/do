# Do

Do provides compositional error-handling in Kotlin. It allows you to write 
software as if nothing could go wrong. Do handles the errors for you, 
plumbing them beneath your core application logic.

By removing the mental burden on the programmer of always deciding how to 
handle errors in each function and how to combine those fallible functions 
with other functions which may or may not fail, we end up with more 
concise, more readable, and more extensible code. Errors are handled 
consistently and explicitly, helping the programmer to reason about a program's 
behavior or debug it when something unexpected happens. In fact, with Do, 
we won't need to keep expecting the unexpected, but instead only inspect 
the expected exceptions expecting to find an exception.

Many (or most) functional programming libraries written for languages that aren't 
purely functional such as Kotlin, Swift, or Typescript try to mimic the exact APIs 
or programming patterns used in pure functional languages such as Haskell or OCaml. 
But those interfaces and patterns do not usually translate well on two levels: 
the language level and the user level. These newer hybrid programming languages may 
have sum types and parametric polymorphism, but in practice they are still far 
less expressive than Haskell at both the value and type level. Therefore, the 
translated interfaces are also less expressive and lose a lot of the benefits they had. 
Secondly, the average Haskell programmer has a decent understanding of type systems and 
algebraic structures like monoids and functors, as well as general skills in programming 
with immutability, higher-order functions, and lack of side-effects, but the 
average programmer does not.

This library is an attempt to take the best and core parts of error handling (and a 
few other things) in pure functional languages and implement them in a way 
that is natural and intuitive in Kotlin.


**Table of Contents**

- [Installation](#installation)
- [Just Do It](#just-do-it)
- [The Types](#the-types)
  - [Maybe](#maybe)
    - [Independent Effects](#two-simple-examples)
    - [Dependent Effects](#two-simple-examples)
  - [Eff](#eff)

# Installation

### Gradle

```
allprojects {
  repositories {
      jcenter()
      maven { url "https://jitpack.io" }
  }
}

dependencies {
    compile 'com.github.jeff-wise:do:0.7.0
}

```

## Just Do It

This library provides two types: `Eff` and `Maybe`. Both represent common coding 
idioms taking from the pure functional programming paradigm. They provide a way to 
deal with side-effects *compositionally*. Both types are based on mathematical 
structures called Functors, Applicative Functors, and Monads. These structures are 
notoriously difficult to understand, but 
monads provide a way to abstract over function application. there are
a lot of ways to do that e.g. list, state, read, etc... bullet pointis
this library we mostly just care about apply functions that may fail.

(low level)


common case, want to write functions that operate on data that is there.
when applying the functions, we deal with the failure cases.

first thing I miss when leaving Haskell. also hard to do in haskell the right way.
MTL takes some type to appreciate and use proficiently (or confidently)

interface is important 


## Examples

See Culebra for good usage example.


## The Types

### Maybe

Let's get motivated to use the `Maybe` type. In these examples we mock the functions 
to keep things simple. Later, we will write some real programs. 

When learning how to use this library, remember to focus on the types. How we create 
and compose the types is what makes these functions interesting.
Thinking about types is what makes Haskell difficult, especially when
dealing with nested and/or parameterized types. 

Note that this library allows us to avoid exception handling in many cases. Sometimes, 
exceptions are necessary, but often we can write cleaner and more compositional code 
with the `Maybe` type.

#### Independent Effects (Applicative Style)

In this example, we have a simple program that depends on a few data sources. All of 
the data sources are independent (the success of one does not depend on another). 


The data sources may fail. what if we could write our program in
a maigcal world where data souces never fail. then we have a special
machine which takes our "pure" program and runs it in the real world.
Now, we need to make sure we can take this program and combine it with
other "pure" programs which actually cannot fail. Because other
programs cannot assume our program will fail. to guaratnee this
doesn't ahppen, need to give our program a special type. 

how do we implement apply? what shoudl it do? 


Normally, in these scenarios, we end up with a lot of nested code and if statements 
that are hard to read. `apply` with the Maybe type allows us to write functions as if 
errors did not occur i.e. with the assumption that the inputs are non-null. It handles 
checking each input for us and failing if any of the inputs do not exist.

On a small-scale, this ensures that our code is easy to read. On a large
scale, it means we can build programs that are more maintainable. This
is simply because we are reducing the mental burden on the programmer
to worry about values which may be null.  We can write our programs as if 
the values could never be null. The trade-off is that those programs
return values into a nullable context. 

abstract -> concrete

show apply implementation


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

#### Dependent Effects (Monad Style)

Go back to special programs exampl.e gave a special type to programs
which look pure but can fail. what we if have many of these and want
ot combine them. We need a way 

abstract -> concrete

show apply implementation

### Eff


adding error types instead of Nothing

do notation


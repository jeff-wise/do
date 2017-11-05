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
the language level and the user level. 

Newer hybrid programming languages may 
have sum types and parametric polymorphism, but in practice they are still far 
less expressive than Haskell at both the value and type level. Therefore, the 
translated interfaces are also less expressive and lose a lot of the benefits. 
Secondly, the average Haskell programmer has a decent understanding of type systems and 
algebraic structures like monoids and functors, as well as general skills in programming 
with immutability, higher-order functions, and lack of side-effects, but the 
average programmer does not, so the same structures and interfaces do not work 
in other languages simply because no one will be able to use them.

This library is an attempt to take the best and core parts of error handling (and a 
few other things) in pure functional languages and implement them in a way 
that is natural and intuitive in Kotlin.


- [Installation](#installation)
- [How It Works](#how-it-works)
- [The Types](#the-types)
  - [Maybe](#maybe)
    - [Independent Effects](#two-simple-examples)
    - [Dependent Effects](#two-simple-examples)
  - [Eff](#eff)

## Installation

#### Gradle

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

## How It Works

In this tutorial, we will focus on understanding a few concepts, but not how 
those concepts are applied. There are so many programming languages used in 
modern software development and many tools, frameworks and design patterns 
used within each language. The concepts discussed here are universal because 
they are both simple and very general. You can adapt them to whichever 
language and tools you are using whether it is enterprise OOP or client-side 
javascript (or better understand how they have already been adapted, because 
they definitely have). Of course, this library is intended for use in Kotlin, 
but even within Kotlin it will be up to you exactly when and how to use *Do*.

In an ideal world, we would write our programs very simply. We only need 
three things<sup>1</sup>:

 * **Data/Objects:** A = Int, B = String, C = MyClass, etc...
 * **Functions:** F = A -> B, G = B -> C, etc...
 * **Function Application:** F(A), G(B), etc...

Of course, those three things may look very different depending on the programming 
language. For example, in OOP, data is represented with classes, and functions 
are methods, and both are used in the context of inheritance, static/dynamic 
polymorphism, accessibility modifiers, and static modifiers. Perhaps the main appeal 
of functional programming is that it has a simple model. Data is just data (typically 
with algebraic data types )and functions are just functions.

Many programming libraries deal either with data or functions. For example, and HTTP 
client library may provide *functions* for communicating with HTTP servers. It will 
also provide *data types* for representing the requests, responses, and other 
parts of the interface. What libraries have you used that extend function application? 
Perhaps a [functional] reactive programming library? These libraries in some way 
manage function application, usually to control when it occurs (such as in response 
to some event like a model update).  

Do modifies the semantics of function application in a way that allows us to 
write functions which assume valid input and then apply them over data which 
may or not be valid. First, let's just think about other ways in which we 
can modify the way that function application occurs i.e. what additional 
semantics could we add?

 * **Non-Determinism:** Every time we apply a function, it is applied in every 
     way possible. This usually assumes that we are applying a function to a list. For 
     example, if we have a list of values `[1, 2, 3]` and a function `isOdd` our 
     non-deterministic application would result in `[True, False, True]`. We take 
     each branch and then typically do something interesting with the results (such as 
     the `any` function in this case). 
 * **Logging:** Every time we apply a function, we also write a value to a log. 
 * **Dependency:** Every time we apply a function, we also write a value to a log. 

These additional semantics are very convincing in pure functional programming languages 
like Haskell, but only some of them are fundamentally useful in Kotlin. Those are the 
ones implemented by Do, namely Either and State. I'm considering adding logging as well.

Now if you haven't caught on, what I'm really talking about are algebraic structures 
called Functors, Applicative Functors, and Monads. 

Work in Progress...


## Examples

See [Culebra](https://github.com/jeff-wise/culebra) for an application of the library 
in YAML parsing.

## The Types

### Maybe

The `Maybe` type represents values that may or may not exist. It is a parameterized 
sum type with two cases: `Just` represents a value, and `Nothing` represents no 
value or null. *Parameterized* refers to the fact that `Maybe` takes a type parameter 
that sets the type of value contained in the Maybe such as `Maybe<Int>` or `Maybe<String>`.

```kotlin
sealed class Maybe<A>

data class Just<A>(val value : A) : Maybe<A>()

class Nothing<A> : Maybe<A>()
```

#### Independent Effects (Applicative Style)

In this example, we have a simple program that depends on a few data sources. All of 
the data sources are independent (the success of one does not depend on another), and 
each data source may fail to provide data when requested.

The goal is to write our function as if the data sources were assumed be successful. 
It's easier to program without the mental burden of worrying whether each value 
is actually present or not. It also makes the code cleaner because we don't have 
to constantly check our data. We just assume that nothing is null. Let's call this 
our *naive* function.

We will use the `apply` function to apply our naive function over the data sources (that 
may fail). The `apply` function will manage validating the input data and only call 
the naive function when all of the data is actually present. `apply` abstracts away 
null values so we can write functions that assume non-null inputs and run them 
on data which may be null.

Let's write the `apply` function. But first, here is our *naive* function:

```kotlin
fun naive(fileData : String) : String = processFile(fileData)
```

`processFile` isn't defined. It's just there to represent what we want to do with 
the file data.

The problem we have to deal with now is that `fileData` is not marked as nullable (`String?`) 
in the function, but it actually is. We need to write the `apply` function that will run 
our `naive` function over nullable data. That is, the `apply` function will take our 
function that can't fail and run it for us in an environment where it may fail. 

The `apply` function isn't too complicated. It takes our `naive` function and our 
nullable value `a`. We first need to check if `a` exists. If it doesn't -- if `a` is 
 `Nothing`, then we just return `Nothing` because that's literally all we can do. 
 The `naive` function cannot do anything without input. If `a` is present then we 
 extract the value from the `Just` and pass it into the `naive` function.

```kotlin
fun <A,T> apply(naive : (A) -> T, a : Maybe<A>) : Maybe<T>
{
    val aValue = when (a) {
        is Just    -> a.value
        is Nothing -> return Nothing()
    }

    return Just(naive(aValue))
}
```

Now we can put everything together with a larger example:

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

#### Dependent Effects (Monadic Style)

In the previous section, the `readFile` and `parseUserCommand` functions returned `Maybe` 
results. Those functions were run independently and their results were used by the `modifyFile` 
function only when both the results were non-null. 

What if the `parseUserCommand` function depends on the result of the `readFile` function? Can 
you think about how that would complicate the program? Let's suppose we have a slightly 
different set of functions:

```kotlin
object MyData

fun readFile(filepath : String) : Maybe<String> = Nothing()

fun parseFile(fileString : String) : Maybe<MyData> = Nothing()

// Now apply doesn't work! We need to first run readFile, and then parseFile 
// on the result. In addition, we need to check if readFile fails and if it doesn not
// fail we can run parseFile, and then check if parseFile failed.
apply(::modifyFile, readFile("/data/filepath"), parseUserCommand(??))
```

Work in Progress...

### Eff

Work in Progress...


#### Footnotes
1. See the [Simply Typed Lambda Calculus](https://en.wikipedia.org/wiki/Simply_typed_lambda_calculus)
2. See [System F](https://en.wikipedia.org/wiki/System_F) 


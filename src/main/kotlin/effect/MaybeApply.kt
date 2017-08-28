
package effect



fun <A,T> apply(f: (A) -> T, a: Maybe<A>) : Maybe<T>
{
    val aValue = when (a) {
        is Just    -> a.value
        is Nothing -> return Nothing()
    }

    return Just(f(aValue))
}



fun <A,B,Z> apply(f: (A,B) -> Z,
                  a: Maybe<A>,
                  b: Maybe<B>) : Maybe<Z>
{

    val aValue = when (a) {
        is Just    -> a.value
        is Nothing -> return Nothing()
    }

    val bValue = when (b) {
        is Just    -> b.value
        is Nothing -> return Nothing()
    }


    return Just(f(aValue, bValue))
}


fun <A,B,C,Z> apply(f: (A,B,C) -> Z,
                    a: Maybe<A>,
                    b: Maybe<B>,
                    c: Maybe<C>) : Maybe<Z>
{

    val aValue = when (a) {
        is Just    -> a.value
        is Nothing -> return Nothing()
    }

    val bValue = when (b) {
        is Just    -> b.value
        is Nothing -> return Nothing()
    }

    val cValue = when (c) {
        is Just    -> c.value
        is Nothing -> return Nothing()
    }

    return Just(f(aValue, bValue, cValue))
}


fun <A,B,C,D,Z> apply(f: (A,B,C,D) -> Z,
                      a: Maybe<A>,
                      b: Maybe<B>,
                      c: Maybe<C>,
                      d: Maybe<D>) : Maybe<Z>
{

    val aValue = when (a) {
        is Just    -> a.value
        is Nothing -> return Nothing()
    }

    val bValue = when (b) {
        is Just    -> b.value
        is Nothing -> return Nothing()
    }

    val cValue = when (c) {
        is Just    -> c.value
        is Nothing -> return Nothing()
    }

    val dValue = when (d) {
        is Just    -> d.value
        is Nothing -> return Nothing()
    }

    return Just(f(aValue, bValue, cValue, dValue))
}


fun <A,B,C,D,E,Z> apply(f: (A,B,C,D,E) -> Z,
                        a: Maybe<A>,
                        b: Maybe<B>,
                        c: Maybe<C>,
                        d: Maybe<D>,
                        e: Maybe<E>) : Maybe<Z>
{

    val aValue = when (a) {
        is Just    -> a.value
        is Nothing -> return Nothing()
    }

    val bValue = when (b) {
        is Just    -> b.value
        is Nothing -> return Nothing()
    }

    val cValue = when (c) {
        is Just    -> c.value
        is Nothing -> return Nothing()
    }

    val dValue = when (d) {
        is Just    -> d.value
        is Nothing -> return Nothing()
    }

    val eValue = when (e) {
        is Just    -> e.value
        is Nothing -> return Nothing()
    }

    return Just(f(aValue, bValue, cValue, dValue, eValue))
}

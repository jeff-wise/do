
package effect


import java.io.Serializable



/**
 * Eff Monad
 */


interface Monoid<A> {
    infix fun mappend(x : A) : A
}


class Identity : Monoid<Identity>
{
    override fun mappend(x: Identity): Identity = this
}


// ---------------------------------------------------------------------------------------------
// Eff
// ---------------------------------------------------------------------------------------------

sealed class Eff<E, W : Monoid<W>, A>(open val env : W) : Serializable
{

    infix fun <T> ap(f : (A) -> Eff<E,W,T>) : Eff<E,W,T> = when (this)
    {
        is Val -> {
            val nextEffect = f(this.value)
            val nextEnv    = this.env mappend nextEffect.env
            when (nextEffect) {
                is Val -> Val(nextEffect.value, nextEnv)
                is Err -> Err(nextEffect.error, nextEnv)
            }
        }
        is Err -> Err(this.error, this.env)
    }


    infix fun apDo(f : (A) -> Unit) : Unit
    {
        when (this) {
            is Val -> f(this.value)
        }
    }


    infix fun <T> apply(f : (A) -> Eff<E,W,T>) : Eff<E,W,T> = this.ap(f)


    /**
     * Gives the same flexibility as 'do-notation', but with a more conventional interface so that it is
     * easy and intuitive to use in Kotlin. If you look at the source code, especially for the implementations
     * with more than one 'with' argument, you can see that it literally implements do-notation expansion.
     */
    fun <B,T> applyWith(f : (A,B) -> Eff<E,W,T>, bEff : Eff<E,W,B>) : Eff<E,W,T> = when (this)
    {
        is Val -> {
            val nextEffect = bEff ap { b -> f(this.value, b)}
            val nextEnv    = this.env mappend nextEffect.env
            when (nextEffect) {
                is Val -> Val(nextEffect.value, nextEnv)
                is Err -> Err(nextEffect.error, nextEnv)
            }
        }
        is Err -> Err(this.error, this.env)
    }


    /**
     * Gives the same flexibility as 'do-notation', but with a more conventional interface so that it is
     * easy and intuitive to use in Kotlin. If you look at the source code, especially for the implementations
     * with more than one 'with' argument, you can see that it literally implements do-notation expansion.
     */
    fun <B,C,T> applyWith(f : (A,B,C) -> Eff<E,W,T>,
                          bEff : Eff<E,W,B>,
                          cEff : Eff<E,W,C>) : Eff<E,W,T> = when (this)
    {
        is Val -> {
            val nextEffect = bEff ap { b -> cEff ap { c -> f(this.value, b, c)} }
            val nextEnv    = this.env mappend nextEffect.env
            when (nextEffect) {
                is Val -> Val(nextEffect.value, nextEnv)
                is Err -> Err(nextEffect.error, nextEnv)
            }
        }
        is Err -> Err(this.error, this.env)
    }


    /**
     * Gives the same flexibility as 'do-notation', but with a more conventional interface so that it is
     * easy and intuitive to use in Kotlin. If you look at the source code, especially for the implementations
     * with more than one 'with' argument, you can see that it literally implements do-notation expansion.
     */
    fun <B,C,D,T> applyWith(f : (A,B,C,D) -> Eff<E,W,T>,
                            bEff : Eff<E,W,B>,
                            cEff : Eff<E,W,C>,
                            dEff : Eff<E,W,D>) : Eff<E,W,T> = when (this)
    {
        is Val -> {
            val nextEffect = bEff ap {
                                b -> cEff ap {
                                c -> dEff ap {
                                d -> f(this.value, b, c, d)} } }
            val nextEnv    = this.env mappend nextEffect.env
            when (nextEffect) {
                is Val -> Val(nextEffect.value, nextEnv)
                is Err -> Err(nextEffect.error, nextEnv)
            }
        }
        is Err -> Err(this.error, this.env)
    }


    /**
     * Gives the same flexibility as 'do-notation', but with a more conventional interface so that it is
     * easy and intuitive to use in Kotlin. If you look at the source code, especially for the implementations
     * with more than one 'with' argument, you can see that it literally implements do-notation expansion.
     */
    fun <B,C,D,F,T> applyWith(f : (A,B,C,D,F) -> Eff<E,W,T>,
                                bEff : Eff<E,W,B>,
                                cEff : Eff<E,W,C>,
                                dEff : Eff<E,W,D>,
                                eEff : Eff<E,W,F>) : Eff<E,W,T> = when (this)
    {
        is Val -> {
            val nextEffect = bEff ap {
                                b -> cEff ap {
                                c -> dEff ap {
                                d -> eEff ap {
                                e -> f(this.value, b, c, d, e)
                                }} } }
            val nextEnv    = this.env mappend nextEffect.env
            when (nextEffect) {
                is Val -> Val(nextEffect.value, nextEnv)
                is Err -> Err(nextEffect.error, nextEnv)
            }
        }
        is Err -> Err(this.error, this.env)
    }
}


// Eff > Value
// ---------------------------------------------------------------------------------------------

data class Val<E, W : Monoid<W>, A>(val value : A, override val env : W) : Eff<E,W,A>(env)


fun <E,A> effValue(value : A) : Eff<E,Identity,A> =
        Val(value, Identity())


// Eff > Err
// ---------------------------------------------------------------------------------------------

data class Err<E, W : Monoid<W>, A>(val error : E, override val env : W) : Eff<E,W,A>(env)


fun <E,A> effError(error : E) : Eff<E,Identity,A> =
        Err(error, Identity())



// ---------------------------------------------------------------------------------------------
// COLLECTIONS
// ---------------------------------------------------------------------------------------------

fun <E,A> List<Eff<E,Identity,A>>.sequenceI() : Eff<E,Identity,List<A>>
{
    val results = mutableListOf<A>()

    for (eff in this.listIterator())
    {
        when (eff)
        {
            is Val -> results.add(eff.value)
            is Err -> return Err(eff.error, Identity())
        }
    }

    return Val(results, Identity())
}



// Broken with empty list because we don't have monoid empty function
// TODO create version that takes in default env value?
fun <E,W:Monoid<W>,A,B> List<A>.mapM(f : (A) -> Eff<E,W,B>) : Eff<E,W,List<B>>
{

    val newElems = mutableListOf<B>()
    var env : W? = null

    for (elem in this)
    {
        val eff = f(elem)
        when (eff)
        {
            is Val ->
            {
                newElems.add(eff.value)
                if (env != null)
                    env = env.mappend(eff.env)
                else
                    env = eff.env
            }
            is Err ->
            {
                return Err(eff.error, env ?: eff.env)
            }
        }
    }

    return Val(newElems, env!!)
}


fun <E,W:Monoid<W>,A,B> Set<A>.mapM(f : (A) -> Eff<E,W,B>) : Eff<E,W,Set<B>>
{

    val newElems = mutableSetOf<B>()
    var env : W? = null

    for (elem in this)
    {
        val eff = f(elem)
        when (eff)
        {
            is Val ->
            {
                newElems.add(eff.value)
                if (env != null)
                    env = env.mappend(eff.env)
                else
                    env = eff.env
            }
            is Err ->
            {
                return Err(eff.error, env ?: eff.env)
            }
        }
    }

    return Val(newElems, env!!)
}


fun <E,A,B> List<A>.mapApply(f : (A) -> Eff<E,Identity,B>) : Eff<E,Identity,List<B>> = mapMI(f)


fun <E,A,B> List<A>.mapMI(f : (A) -> Eff<E,Identity,B>) : Eff<E,Identity,List<B>>
{

    val newElems = mutableListOf<B>()

    for (elem in this)
    {
        val eff = f(elem)
        when (eff)
        {
            is Val ->
            {
                newElems.add(eff.value)
            }
            is Err ->
            {
                return Err(eff.error, Identity())
            }
        }
    }

    return Val(newElems, Identity())
}


fun <E,A,B> Set<A>.mapMI(f : (A) -> Eff<E,Identity,B>) : Eff<E,Identity,Set<B>>
{

    val newElems = mutableSetOf<B>()

    for (elem in this)
    {
        val eff = f(elem)
        when (eff)
        {
            is Val ->
            {
                newElems.add(eff.value)
            }
            is Err ->
            {
                return Err(eff.error, Identity())
            }
        }
    }

    return Val(newElems, Identity())
}


// ---------------------------------------------------------------------------------------------
// UTILITIES
// ---------------------------------------------------------------------------------------------

fun <E,A> note(value  : A?, error : E) : Eff<E,Identity,A> =
    if (value != null)
        effValue<E,A>(value)
    else
        effError(error)


@Suppress("UNCHECKED_CAST")
fun <E,R:Monoid<R>,A,T> split(a : Eff<E,R,Maybe<A>>,
                              f : Eff<E,R,T>,
                              g : (A) -> Eff<E,R,T>) : Eff<E,R,T> = when (a)
{
    is Val -> when (a.value)
    {
        is Just    -> g(a.value.value)
        is Nothing -> f
    }
    is Err -> a as Eff<E,R,T>
}


fun <E,W:Monoid<W>,A> valueOf(eff : Eff<E,W,A>?) : A? =  when (eff)
{
    is Val -> eff.value
    else   -> null
}


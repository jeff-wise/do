
import effect.apply
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import maybe.Just
import maybe.Nothing
import maybe.apply


class MaybeTests : StringSpec()
{

    init
    {

        fun add(x : Int, y : Int) : Int = x + y


        "Adding two Just values gives a Just result" {
            val justSum = maybe.apply(::add, Just(2), Just(2))
            justSum shouldBe Just(4)
        }


        "Adding a Nothing and a Just number gives a Nothing result" {
            val nothing = apply(::add, Nothing(), Just(2))
            nothing shouldBe Nothing<Int>()
        }


        "Adding a Just number and a Nothing gives a Nothing result" {
            val nothing = apply(::add, Just(2), Nothing())
            nothing shouldBe Nothing<Int>()
        }


        "Adding a Nothing and a Nothing gives a Nothing result" {
            val nothing = apply(::add, Nothing(), Nothing())
            nothing shouldBe Nothing<Int>()
        }
    }
}


// Plans
// Start with Maybe / Alternative
// Basic tests
// data project with maybe, hashmap, data model with optional values
//   then composing those equals maybes of maybes.
//   computation with ap maybe manipulate property of Model in sequence.
// most common use of maybe, doing functions over data that may or may not be there
//  but it is allowed to not be there. don't want to convolute logic writing ifs when you
// could just write as normal

// function which takes 4 args and one or two coudl be null

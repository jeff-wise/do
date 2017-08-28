

import data.MyEff
import data.MyError
import effect.*
import io.kotlintest.matchers.beOfType
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.ShouldSpec



class EffTests : ShouldSpec()
{

    init
    {


        "sequenceI" {

            should("return [2, 2]") {
                val effs : List<MyEff<Int>> = listOf(effValue(2), effValue(2))
                val numbersEff : MyEff<List<Int>> = effs.sequenceI()

                when (numbersEff)
                {
                    is Val -> numbersEff.value shouldBe listOf(2, 2)
                    is Err -> numbersEff should beOfType<Eff<MyError,Identity,List<Int>>>()
                }
            }

            should("return empty list when empty list is given") {
                val effs : List<MyEff<Int>> = listOf()
                val numbersEff : MyEff<List<Int>> = effs.sequenceI()

                when (numbersEff)
                {
                    is Val -> numbersEff.value shouldBe listOf<Int>()
                    is Err -> numbersEff should beOfType<Eff<MyError,Identity,List<Int>>>()
                }
            }

            should("return Error when last element is an Error") {
                val effs : List<MyEff<Int>> = listOf(effValue(2), effValue(2), effError(MyError))
                val numbersEff : MyEff<List<Int>> = effs.sequenceI()

                numbersEff should beOfType<Err<MyError,Identity,List<Int>>>()
            }

            should("return Error when first element is an Error") {
                val effs : List<MyEff<Int>> = listOf(effError(MyError), effValue(2), effValue(2))
                val numbersEff : MyEff<List<Int>> = effs.sequenceI()

                numbersEff should beOfType<Err<MyError,Identity,List<Int>>>()
            }
        }
    }
}

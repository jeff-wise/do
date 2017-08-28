
package data


import effect.Eff
import effect.Identity



/**
 * Simple Eff Types
 */


object MyError


typealias MyEff<A> = Eff<MyError, Identity, A>


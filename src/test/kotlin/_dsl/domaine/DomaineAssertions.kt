package _dsl.domaine

import arrow.core.Either
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

// ---------------------------------------------------------------------------
// DSL 1 — Domain assertions
//
// Built on Either — the central type of all domain operations.
// Use in FunSpec tests. Each function reads as a domain rule.
//
// Usage:
//   NomDeProduit.de("Vélo") estValide()
//   NomDeProduit.de("")     estRefusé avecErreur ErreurDeCatalog.NomVide
//   produit.deprecier()     produitUnEvenement EvenementDeCatalog.ProduitDeprecied::class
// ---------------------------------------------------------------------------

// --- Succès ---

fun <T> Either<*, T>.estValide(): T {
    this.shouldBeInstanceOf<Either.Right<T>>()
    return (this as Either.Right<T>).value
}

infix fun <A, B> Either<A, Pair<B, *>>.produitUnEvenement(type: kotlin.reflect.KClass<*>): B {
    val (valeur, evenement) = this.estValide()
    evenement.shouldBeInstanceOf(type)
    return valeur
}

infix fun <A, B, E> Either<A, Pair<B, E>>.etLEvenementEst(assertions: (E) -> Unit): B {
    val (valeur, evenement) = this.estValide()
    assertions(evenement)
    return valeur
}

// --- Échec ---

infix fun <A, B> Either<A, B>.estRefusé(erreurAttendue: A) {
    this shouldBe Either.Left(erreurAttendue)
}

// Alias for readability: `estRefusé avecErreur ErreurDeCatalog.NomVide`
infix fun <A, B> Either<A, B>.avecErreur(erreurAttendue: A) {
    this shouldBe Either.Left(erreurAttendue)
}

// --- État ---

infix fun <T, P : Any> Either<*, T>.etSonEtatEst(assertions: (T) -> P): P {
    val valeur = this.estValide()
    return assertions(valeur)
}

package catalog.domain

import arrow.core.Either
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import catalog.domain.entities.EtatDuProduit
import catalog.domain.entities.Produit
import catalog.domain.errors.ErreurDeCatalog
import catalog.domain.events.EvenementDeCatalog
import catalog.domain.valueObjects.NomDeProduit
import catalog.domain.valueObjects.Prix
import catalog.domain.valueObjects.Slug

class ProduitTest : FunSpec({

    val nomValide = NomDeProduit.de("Vélo électrique").getOrNull()!!
    val slugValide = Slug.de("velo-electrique").getOrNull()!!
    val prixValide = Prix.de(29900).getOrNull()!!

    test("ajouter un produit valide retourne un produit actif et un evenement") {
        val resultat = Produit.ajouter("id-1", nomValide, slugValide, prixValide)

        resultat.shouldBeInstanceOf<Either.Right<*>>()
        val (produit, evenement) = (resultat as Either.Right).value
        produit.etat shouldBe EtatDuProduit.ACTIF
        evenement.shouldBeInstanceOf<EvenementDeCatalog.ProduitAjoute>()
        evenement.produitId shouldBe "id-1"
    }

    test("ajouter un produit avec id vide retourne une erreur") {
        val resultat = Produit.ajouter("", nomValide, slugValide, prixValide)
        resultat shouldBe Either.Left(ErreurDeCatalog.NomVide)
    }

    test("deprecier un produit actif retourne un produit deprecie et un evenement") {
        val (produit, _) = Produit.ajouter("id-1", nomValide, slugValide, prixValide).getOrNull()!!

        val resultat = produit.deprecier()

        resultat.shouldBeInstanceOf<Either.Right<*>>()
        val (deprecie, evenement) = (resultat as Either.Right).value
        deprecie.etat shouldBe EtatDuProduit.DEPRECIE
        evenement.shouldBeInstanceOf<EvenementDeCatalog.ProduitDeprecied>()
    }

    test("deprecier un produit deja deprecie retourne une erreur") {
        val (produit, _) = Produit.ajouter("id-1", nomValide, slugValide, prixValide).getOrNull()!!
        val (deprecie, _) = produit.deprecier().getOrNull()!!

        val resultat = deprecie.deprecier()

        resultat shouldBe Either.Left(ErreurDeCatalog.ProduitDejaDeprecied)
    }

    test("un nom vide est refuse") {
        NomDeProduit.de("") shouldBe Either.Left(ErreurDeCatalog.NomVide)
        NomDeProduit.de("   ") shouldBe Either.Left(ErreurDeCatalog.NomVide)
    }

    test("un prix negatif est refuse") {
        Prix.de(-1) shouldBe Either.Left(ErreurDeCatalog.PrixInvalide)
    }

    test("un prix a zero est accepte") {
        Prix.de(0).shouldBeInstanceOf<Either.Right<*>>()
    }
})

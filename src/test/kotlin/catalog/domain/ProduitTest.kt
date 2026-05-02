package catalog.domain

import _dsl.domaine.*
import catalog.domain.entities.EtatDuProduit
import catalog.domain.entities.Produit
import catalog.domain.errors.ErreurDeCatalog
import catalog.domain.events.EvenementDeCatalog
import catalog.domain.valueObjects.NomDeProduit
import catalog.domain.valueObjects.Prix
import catalog.domain.valueObjects.Slug
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ProduitTest : FunSpec({

    val nom = NomDeProduit.de("Vélo électrique").getOrNull()!!
    val slug = Slug.de("velo-electrique").getOrNull()!!
    val prix = Prix.de(29900).getOrNull()!!

    // --- Invariants sur les value objects ---

    test("un nom vide est refusé") {
        NomDeProduit.de("") estRefuséParceQue ErreurDeCatalog.NomVide
        NomDeProduit.de("   ") avecErreur ErreurDeCatalog.NomVide
    }

    test("un prix négatif est refusé") {
        Prix.de(-1) estRefuséParceQue ErreurDeCatalog.PrixInvalide
    }

    test("un prix à zéro est accepté") {
        Prix.de(0).estValide()
    }

    // --- Invariants sur le cycle de vie du produit ---

    test("ajouter un produit valide crée un produit actif avec un événement ProduitAjoute") {
        Produit.ajouter("id-1", nom, slug, prix)
            .produitUnEvenement(EvenementDeCatalog.ProduitAjoute::class)
            .etat shouldBe EtatDuProduit.ACTIF
    }

    test("ajouter un produit avec id vide est refusé") {
        Produit.ajouter("", nom, slug, prix) estRefuséParceQue ErreurDeCatalog.NomVide
    }

    test("déprécier un produit actif le marque comme déprécié et produit l'événement attendu") {
        val (produit, _) = Produit.ajouter("id-1", nom, slug, prix).estValide()

        produit.deprecier()
            .etLEvenementEst { evenement ->
                evenement.shouldBe(EvenementDeCatalog.ProduitDeprecied("id-1", nom))
            }
            .etat shouldBe EtatDuProduit.DEPRECIE
    }

    test("déprécier un produit déjà déprécié est refusé") {
        val (produit, _) = Produit.ajouter("id-1", nom, slug, prix).estValide()
        val (deprecie, _) = produit.deprecier().estValide()

        deprecie.deprecier() estRefuséParceQue ErreurDeCatalog.ProduitDejaDeprecied
    }
})

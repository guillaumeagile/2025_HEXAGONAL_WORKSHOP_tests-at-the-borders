package catalog.application

import arrow.core.Either
import arrow.core.right
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import catalog.adapters.driven.persistence.fake.FakePourPersisterUnProduit
import catalog.application.commands.AjouterUnProduitCmd
import catalog.application.commands.DeprecierUnProduitCmd
import catalog.application.queries.ObtenirProduitQuery
import catalog.application.services.ServiceDeCatalog
import catalog.domain.entities.EtatDuProduit
import catalog.domain.errors.ErreurDeCatalog
import catalog.domain.events.EvenementDeCatalog
import catalog.domain.factories.PourGenererUnSlug
import catalog.domain.factories.UsineDeProduits
import catalog.domain.valueObjects.NomDeProduit
import catalog.domain.valueObjects.Prix
import catalog.domain.valueObjects.Slug
import catalog.ports.driven.PourPublierUnEvenement

class ServiceDeCatalogTest : FunSpec({

    val evenementsPublies = mutableListOf<EvenementDeCatalog>()

    fun construire(fake: FakePourPersisterUnProduit): ServiceDeCatalog {
        evenementsPublies.clear()
        return ServiceDeCatalog(
            produits = fake,
            evenements = PourPublierUnEvenement { evenementsPublies.add(it) },
            usine = UsineDeProduits(
                generateurId = { "id-fixe" },
                generateurSlug = PourGenererUnSlug { nom ->
                    Slug.de(nom.valeur.lowercase().replace(" ", "-"))
                }
            )
        )
    }

    test("ajouter un produit le persiste et publie un evenement") {
        val fake = FakePourPersisterUnProduit()
        val service = construire(fake)
        val cmd = AjouterUnProduitCmd(
            nom = NomDeProduit.de("Vélo électrique").getOrNull()!!,
            prix = Prix.de(29900).getOrNull()!!
        )

        val resultat = service.ajouterUnProduit(cmd)

        resultat.shouldBeInstanceOf<Either.Right<*>>()
        fake.obtenirTous().size shouldBe 1
        evenementsPublies.size shouldBe 1
        evenementsPublies.first().shouldBeInstanceOf<EvenementDeCatalog.ProduitAjoute>()
    }

    test("deprecier un produit existant publie un evenement ProduitDeprecied") {
        val fake = FakePourPersisterUnProduit()
        val service = construire(fake)
        service.ajouterUnProduit(
            AjouterUnProduitCmd(
                nom = NomDeProduit.de("Trottinette").getOrNull()!!,
                prix = Prix.de(15000).getOrNull()!!
            )
        )
        evenementsPublies.clear()

        val resultat = service.deprecierUnProduit(DeprecierUnProduitCmd("id-fixe"))

        resultat.shouldBeInstanceOf<Either.Right<*>>()
        (resultat as Either.Right).value.etat shouldBe EtatDuProduit.DEPRECIE
        evenementsPublies.size shouldBe 1
        evenementsPublies.first().shouldBeInstanceOf<EvenementDeCatalog.ProduitDeprecied>()
    }

    test("deprecier un produit inexistant retourne ProduitIntrouvable") {
        val service = construire(FakePourPersisterUnProduit())

        val resultat = service.deprecierUnProduit(DeprecierUnProduitCmd("inconnu"))

        resultat shouldBe Either.Left(ErreurDeCatalog.ProduitIntrouvable("inconnu"))
        evenementsPublies shouldBe emptyList()
    }

    test("obtenir un produit inexistant retourne ProduitIntrouvable") {
        val service = construire(FakePourPersisterUnProduit())

        val resultat = service.obtenirProduit(ObtenirProduitQuery("inconnu"))

        resultat shouldBe Either.Left(ErreurDeCatalog.ProduitIntrouvable("inconnu"))
    }
})

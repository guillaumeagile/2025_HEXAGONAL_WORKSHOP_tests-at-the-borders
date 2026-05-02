package _dsl.application

import arrow.core.Either
import catalog.adapters.driven.persistence.fake.FakePourPersisterUnProduit
import catalog.application.commands.AjouterUnProduitCmd
import catalog.application.commands.DeprecierUnProduitCmd
import catalog.application.queries.ObtenirProduitQuery
import catalog.application.services.ServiceDeCatalog
import catalog.domain.entities.EtatDuProduit
import catalog.domain.entities.Produit
import catalog.domain.errors.ErreurDeCatalog
import catalog.domain.events.EvenementDeCatalog
import catalog.domain.factories.PourGenererUnSlug
import catalog.domain.factories.UsineDeProduits
import catalog.domain.valueObjects.NomDeProduit
import catalog.domain.valueObjects.Prix
import catalog.domain.valueObjects.Slug
import catalog.ports.driven.PourPublierUnEvenement
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

// ---------------------------------------------------------------------------
// DSL 2 — Application scenario context for Catalog
//
// One instance per test (created fresh in each `given` block).
// Holds fakes + service. All actions and assertions are infix extensions on this class.
//
// Usage in BehaviorSpec:
//   given("un fournisseur veut ajouter un produit") {
//       `when`("le produit est valide") {
//           val scenario = ScenarioCatalog() ajouteUnProduit ("Vélo électrique" à 299.euros)
//           then("il est dans le catalogue") { scenario estDansLeCatalog "Vélo électrique" }
//           then("un événement est publié") { scenario aPublié unEvenementDuType<ProduitAjoute>() }
//       }
//   }
// ---------------------------------------------------------------------------

class ScenarioCatalog {
    private val fakeProduits = FakePourPersisterUnProduit()
    private val evenementsPublies = mutableListOf<EvenementDeCatalog>()
    private var dernierResultat: Either<ErreurDeCatalog, Produit>? = null

    val service = ServiceDeCatalog(
        produits = fakeProduits,
        evenements = PourPublierUnEvenement { evenementsPublies.add(it) },
        usine = UsineDeProduits(
            generateurId = { "id-fixe" },
            generateurSlug = PourGenererUnSlug { nom ->
                Slug.de(nom.valeur.lowercase().replace(Regex("[^a-z0-9]"), "-"))
            }
        )
    )

    // --- Actions ---

    suspend infix fun ajouteUnProduit(spec: ProduitSpec): ScenarioCatalog {
        val nom = NomDeProduit.de(spec.nom).getOrNull()
            ?: error("Nom invalide dans le scénario : '${spec.nom}'")
        dernierResultat = service.ajouterUnProduit(AjouterUnProduitCmd(nom, spec.prix))
        return this
    }

    suspend infix fun déprecieUnProduit(id: String): ScenarioCatalog {
        dernierResultat = service.deprecierUnProduit(DeprecierUnProduitCmd(id))
        return this
    }

    // --- Assertions ---

    suspend infix fun estDansLeCatalog(nom: String) {
        fakeProduits.obtenirTous().any { it.nom.valeur == nom } shouldBe true
    }

    infix fun aPublié(assertion: EvenementAssertion) =
        assertion.verifier(evenementsPublies)

    fun nAPubliéAucunEvenement() {
        evenementsPublies shouldBe emptyList()
    }

    infix fun leRésultatEstUneErreur(erreur: ErreurDeCatalog) {
        dernierResultat shouldBe Either.Left(erreur)
    }

    fun leProduitEstDéprécié() {
        dernierResultat!!.shouldBeInstanceOf<Either.Right<*>>()
        (dernierResultat as Either.Right).value.etat shouldBe EtatDuProduit.DEPRECIE
    }
}

// --- Helpers pour construire les specs ---

data class ProduitSpec(val nom: String, val prix: Prix)

infix fun String.à(prix: Prix): ProduitSpec = ProduitSpec(this, prix)

val Int.euros: Prix get() = Prix.de(this * 100).getOrNull()
    ?: error("Prix invalide : $this euros")

val Int.centimes: Prix get() = Prix.de(this).getOrNull()
    ?: error("Prix invalide : $this centimes")

// --- Assertions sur les événements ---

class EvenementAssertion(val verifier: (List<EvenementDeCatalog>) -> Unit)

inline fun <reified E : EvenementDeCatalog> unEvenementDuType(): EvenementAssertion =
    EvenementAssertion { evenements ->
        evenements.any { it is E } shouldBe true
    }

inline fun <reified E : EvenementDeCatalog> exactement(
    n: Int,
    crossinline assertions: (E) -> Unit = {}
): EvenementAssertion = EvenementAssertion { evenements ->
    val matches = evenements.filterIsInstance<E>()
    matches.size shouldBe n
    matches.forEach { assertions(it) }
}

package catalog.application

import _dsl.application.ScenarioCatalog
import _dsl.application.à
import _dsl.application.centimes
import _dsl.application.euros
import _dsl.application.exactement
import _dsl.application.unEvenementDuType
import catalog.domain.errors.ErreurDeCatalog
import catalog.domain.events.EvenementDeCatalog
import io.kotest.core.spec.style.BehaviorSpec

class ServiceDeCatalogTest : BehaviorSpec({

    given("un fournisseur veut ajouter un produit au catalogue") {
        `when`("le produit est valide") {
            val scenario = ScenarioCatalog()
            scenario ajouteUnProduit ("Vélo électrique" à 299.euros)
            then("le produit est dans le catalogue") {
                scenario estDansLeCatalog "Vélo électrique"
            }
            then("un événement ProduitAjoute est publié") {
                scenario aPublié unEvenementDuType<EvenementDeCatalog.ProduitAjoute>()
            }
        }
    }

    given("un produit actif est dans le catalogue") {
        `when`("on le déprécie") {
            val scenario = ScenarioCatalog()
            scenario ajouteUnProduit ("Trottinette" à 150.euros)
            scenario déprecieUnProduit "id-fixe"
            then("le produit est marqué comme déprécié") {
                scenario.leProduitEstDéprécié()
            }
            then("un seul événement ProduitDeprecied est publié") {
                scenario aPublié exactement<EvenementDeCatalog.ProduitDeprecied>(1)
            }
        }
    }

    given("un produit est exprimé en centimes") {
        `when`("le prix est saisi en centimes directement") {
            val scenario = ScenarioCatalog()
            scenario ajouteUnProduit ("Casque vélo" à 4999.centimes)
            then("le produit est dans le catalogue avec le bon prix") {
                scenario estDansLeCatalog "Casque vélo"
            }
        }
    }

    given("le catalogue est vide") {
        `when`("on tente de déprécier un produit inexistant") {
            val scenario = ScenarioCatalog()
            scenario déprecieUnProduit "inconnu"
            then("l'opération est refusée avec ProduitIntrouvable") {
                scenario leRésultatEstUneErreur ErreurDeCatalog.ProduitIntrouvable("inconnu")
            }
            then("aucun événement n'est publié") {
                scenario.nAPubliéAucunEvenement()
            }
        }
    }
})

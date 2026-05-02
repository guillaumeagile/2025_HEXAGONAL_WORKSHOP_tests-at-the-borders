package catalog.domain.events

import catalog.domain.valueObjects.NomDeProduit
import catalog.domain.valueObjects.Slug

sealed interface EvenementDeCatalog {
    val produitId: String

    data class ProduitAjoute(
        override val produitId: String,
        val nom: NomDeProduit,
        val slug: Slug
    ) : EvenementDeCatalog

    data class ProduitDeprecied(
        override val produitId: String,
        val nom: NomDeProduit
    ) : EvenementDeCatalog
}

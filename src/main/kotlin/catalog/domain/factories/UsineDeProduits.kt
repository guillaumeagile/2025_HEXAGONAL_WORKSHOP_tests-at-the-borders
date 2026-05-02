package catalog.domain.factories

import arrow.core.Either
import arrow.core.raise.either
import catalog.domain.entities.Produit
import catalog.domain.errors.ErreurDeCatalog
import catalog.domain.events.EvenementDeCatalog
import catalog.domain.valueObjects.NomDeProduit
import catalog.domain.valueObjects.Prix

fun interface PourGenererUnId {
    fun idSuivant(): String
}

fun interface PourGenererUnSlug {
    fun generer(nom: NomDeProduit): Either<ErreurDeCatalog, catalog.domain.valueObjects.Slug>
}

class UsineDeProduits(
    val generateurId: PourGenererUnId,
    val generateurSlug: PourGenererUnSlug
) {
    fun creer(
        nom: NomDeProduit,
        prix: Prix
    ): Either<ErreurDeCatalog, Pair<Produit, EvenementDeCatalog.ProduitAjoute>> = either {
        val id = generateurId.idSuivant()
        val slug = generateurSlug.generer(nom).bind()
        Produit.ajouter(id, nom, slug, prix).bind()
    }
}

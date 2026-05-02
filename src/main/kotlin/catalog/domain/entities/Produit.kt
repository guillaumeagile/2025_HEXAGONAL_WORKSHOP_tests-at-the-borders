package catalog.domain.entities

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.raise.either
import arrow.core.raise.ensure
import catalog.domain.errors.ErreurDeCatalog
import catalog.domain.events.EvenementDeCatalog
import catalog.domain.valueObjects.NomDeProduit
import catalog.domain.valueObjects.Prix
import catalog.domain.valueObjects.Slug

enum class EtatDuProduit { ACTIF, DEPRECIE }

class Produit private constructor(
    val id: String,
    val nom: NomDeProduit,
    val slug: Slug,
    val prix: Prix,
    val etat: EtatDuProduit
) {

    companion object {

        fun ajouter(
            id: String,
            nom: NomDeProduit,
            slug: Slug,
            prix: Prix
        ): Either<ErreurDeCatalog, Pair<Produit, EvenementDeCatalog.ProduitAjoute>> = either {
            ensure(id.isNotBlank()) { ErreurDeCatalog.NomVide }
            val produit = Produit(id, nom, slug, prix, EtatDuProduit.ACTIF)
            val evenement = EvenementDeCatalog.ProduitAjoute(id, nom, slug)
            Pair(produit, evenement)
        }

        // For adapter/test use only — skips invariant checks, trusts stored data
        fun reconstituer(
            id: String,
            nom: NomDeProduit,
            slug: Slug,
            prix: Prix,
            etat: EtatDuProduit
        ): Produit = Produit(id, nom, slug, prix, etat)
    }

    fun deprecier(): Either<ErreurDeCatalog, Pair<Produit, EvenementDeCatalog.ProduitDeprecied>> =
        if (etat == EtatDuProduit.DEPRECIE)
            ErreurDeCatalog.ProduitDejaDeprecied.left()
        else {
            val produit = Produit(id, nom, slug, prix, EtatDuProduit.DEPRECIE)
            val evenement = EvenementDeCatalog.ProduitDeprecied(id, nom)
            Pair(produit, evenement).right()
        }

    override fun equals(other: Any?): Boolean = other is Produit && id == other.id
    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String =
        "Produit(id=$id, nom=${nom.valeur}, slug=${slug.valeur}, prix=${prix.enEuros()}€, etat=$etat)"
}

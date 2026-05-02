package catalog.application.services

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import catalog.application.commands.AjouterUnProduitCmd
import catalog.application.commands.DeprecierUnProduitCmd
import catalog.application.queries.ObtenirProduitQuery
import catalog.domain.entities.Produit
import catalog.domain.errors.ErreurDeCatalog
import catalog.domain.factories.UsineDeProduits
import catalog.ports.driven.PourPersisterUnProduit
import catalog.ports.driven.PourPublierUnEvenement

class ServiceDeCatalog(
    private val produits: PourPersisterUnProduit,
    private val evenements: PourPublierUnEvenement,
    private val usine: UsineDeProduits
) {

    // --- Commands ---

    suspend fun ajouterUnProduit(cmd: AjouterUnProduitCmd): Either<ErreurDeCatalog, Produit> = either {
        val (produit, evenement) = usine.creer(cmd.nom, cmd.prix).bind()
        produits.enregistrer(produit)
        evenements.publier(evenement)
        produit
    }

    suspend fun deprecierUnProduit(cmd: DeprecierUnProduitCmd): Either<ErreurDeCatalog, Produit> = either {
        val produit = produits.obtenirParId(cmd.produitId)
            ?: raise(ErreurDeCatalog.ProduitIntrouvable(cmd.produitId))
        val (produitDeprecied, evenement) = produit.deprecier().bind()
        produits.enregistrer(produitDeprecied)
        evenements.publier(evenement)
        produitDeprecied
    }

    // --- Queries ---

    suspend fun obtenirProduit(query: ObtenirProduitQuery): Either<ErreurDeCatalog, Produit> =
        produits.obtenirParId(query.produitId)?.let { Either.Right(it) }
            ?: ErreurDeCatalog.ProduitIntrouvable(query.produitId).left()

    suspend fun tousLesProduits(): List<Produit> =
        produits.obtenirTous()
}

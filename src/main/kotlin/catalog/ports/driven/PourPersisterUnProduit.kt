package catalog.ports.driven

import catalog.domain.entities.Produit

interface PourPersisterUnProduit {
    suspend fun enregistrer(produit: Produit)
    suspend fun obtenirParId(id: String): Produit?
    suspend fun obtenirTous(): List<Produit>
}

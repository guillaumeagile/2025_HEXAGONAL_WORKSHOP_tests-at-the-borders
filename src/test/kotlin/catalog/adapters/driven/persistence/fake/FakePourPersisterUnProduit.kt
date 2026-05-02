package catalog.adapters.driven.persistence.fake

import catalog.domain.entities.Produit
import catalog.ports.driven.PourPersisterUnProduit

class FakePourPersisterUnProduit : PourPersisterUnProduit, Resettable {

    private val stockage = mutableMapOf<String, Produit>()

    override suspend fun enregistrer(produit: Produit) {
        stockage[produit.id] = produit
    }

    override suspend fun obtenirParId(id: String): Produit? = stockage[id]

    override suspend fun obtenirTous(): List<Produit> = stockage.values.toList()

    override fun reset() = stockage.clear()
}

interface Resettable {
    fun reset()
}

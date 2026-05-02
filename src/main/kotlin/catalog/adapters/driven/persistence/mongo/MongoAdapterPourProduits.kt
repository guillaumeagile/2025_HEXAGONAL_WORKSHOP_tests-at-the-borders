package catalog.adapters.driven.persistence.mongo

import catalog.domain.entities.EtatDuProduit
import catalog.domain.entities.Produit
import catalog.domain.valueObjects.NomDeProduit
import catalog.domain.valueObjects.Prix
import catalog.domain.valueObjects.Slug
import catalog.ports.driven.PourPersisterUnProduit

// TODO: implement with MongoDB driver
// DTOs are private — they never leave this class
class MongoAdapterPourProduits : PourPersisterUnProduit {

    private data class DocumentProduit(
        val id: String,
        val nom: String,
        val slug: String,
        val prixEnCentimes: Int,
        val etat: String
    )

    private fun Produit.toDocument() = DocumentProduit(
        id = id,
        nom = nom.valeur,
        slug = slug.valeur,
        prixEnCentimes = prix.enCentimes,
        etat = etat.name
    )

    private fun DocumentProduit.toDomain() = Produit.reconstituer(
        id = id,
        nom = NomDeProduit.reconstituer(nom),
        slug = Slug.reconstituer(slug),
        prix = Prix.reconstituer(prixEnCentimes),
        etat = EtatDuProduit.valueOf(etat)
    )

    override suspend fun enregistrer(produit: Produit): Unit = TODO("Implement with MongoDB driver")
    override suspend fun obtenirParId(id: String): Produit? = TODO("Implement with MongoDB driver")
    override suspend fun obtenirTous(): List<Produit> = TODO("Implement with MongoDB driver")
}

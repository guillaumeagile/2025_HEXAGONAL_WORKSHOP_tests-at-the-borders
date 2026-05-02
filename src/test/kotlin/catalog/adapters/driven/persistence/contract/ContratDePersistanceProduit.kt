package catalog.adapters.driven.persistence.contract

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.runBlocking
import catalog.domain.entities.EtatDuProduit
import catalog.domain.entities.Produit
import catalog.domain.valueObjects.NomDeProduit
import catalog.domain.valueObjects.Prix
import catalog.domain.valueObjects.Slug
import catalog.ports.driven.PourPersisterUnProduit

abstract class ContratDePersistanceProduit : AnnotationSpec() {

    abstract fun adapter(): PourPersisterUnProduit
    open fun reset() {}

    private val adapter by lazy { adapter() }

    @BeforeEach fun beforeEach() = reset()

    @Test
    fun `obtenir un produit inexistant retourne null`() = runBlocking {
        adapter.obtenirParId("inconnu") shouldBe null
    }

    @Test
    fun `enregistrer puis obtenir par id`() = runBlocking {
        val produit = unProduit("p1")
        adapter.enregistrer(produit)
        adapter.obtenirParId("p1") shouldNotBe null
        adapter.obtenirParId("p1")!!.id shouldBe "p1"
    }

    @Test
    fun `enregistrer deux produits puis obtenir tous`() = runBlocking {
        adapter.enregistrer(unProduit("p1"))
        adapter.enregistrer(unProduit("p2"))
        adapter.obtenirTous().size shouldBe 2
    }

    @Test
    fun `enregistrer deux fois le meme id ecrase le premier`() = runBlocking {
        adapter.enregistrer(unProduit("p1"))
        adapter.enregistrer(unProduit("p1"))
        adapter.obtenirTous().size shouldBe 1
    }

    @Test
    fun `enregistrer un produit deprecie puis le retrouver avec le bon etat`() = runBlocking {
        val deprecie = Produit.reconstituer(
            id = "p1",
            nom = NomDeProduit.reconstituer("Produit test"),
            slug = Slug.reconstituer("produit-test"),
            prix = Prix.reconstituer(1000),
            etat = EtatDuProduit.DEPRECIE
        )
        adapter.enregistrer(deprecie)
        adapter.obtenirParId("p1")!!.etat shouldBe EtatDuProduit.DEPRECIE
    }

    private fun unProduit(id: String): Produit = Produit.reconstituer(
        id = id,
        nom = NomDeProduit.reconstituer("Produit $id"),
        slug = Slug.reconstituer("produit-$id"),
        prix = Prix.reconstituer(999),
        etat = EtatDuProduit.ACTIF
    )
}

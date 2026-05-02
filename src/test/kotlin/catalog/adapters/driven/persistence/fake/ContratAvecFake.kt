package catalog.adapters.driven.persistence.fake

import catalog.adapters.driven.persistence.contract.ContratDePersistanceProduit
import catalog.ports.driven.PourPersisterUnProduit

class ContratAvecFake : ContratDePersistanceProduit() {
    private val fake = FakePourPersisterUnProduit()
    override fun adapter(): PourPersisterUnProduit = fake
    override fun reset() = fake.reset()
}

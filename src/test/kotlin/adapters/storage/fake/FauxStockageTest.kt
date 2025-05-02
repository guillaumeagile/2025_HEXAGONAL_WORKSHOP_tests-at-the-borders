package adapters.storage.fake

import adapters.storage.RepositorySharedTests
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import location.adapters.driven.storage.DTOs.TicketDto

class FauxStockageTest : FunSpec({

    val testTicket = TicketDto(1, 2)
    val fauxStockageFactory = { -> FauxStockage() }

    // zone de vérification du contrat par les tests partagés
    include(RepositorySharedTests.storageNoSaveAndCount(stockage = fauxStockageFactory()))

    include(RepositorySharedTests.storageSaveAndCount(stockage = fauxStockageFactory()))

    include(RepositorySharedTests.storageSaveAndRead(stockage = fauxStockageFactory()))

    include((RepositorySharedTests.storageSaveTwoAndRead(stockage = fauxStockageFactory())))

    include(RepositorySharedTests.storageCannotSaveTwoOfTheSameId(stockage = fauxStockageFactory()))

    test("saveTicket should add a ticket ") {
        val leStockage = fauxStockageFactory()
        leStockage.save(testTicket)
        leStockage.listeOrdonneeDesTickets.size shouldBe 1
    }

})


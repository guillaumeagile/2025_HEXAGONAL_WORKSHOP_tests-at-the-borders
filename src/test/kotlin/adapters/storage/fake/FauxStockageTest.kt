package adapters.storage.fake

import adapters.storage.StorageSharedTests
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import location.adapters.driven.storage.DTOs.TicketDto

class FauxStockageTest : FunSpec({

    val testTicket = TicketDto(1, 2)
    val fauxStockageFactory = { -> FauxStockage() }

    include(StorageSharedTests.storageNoSaveAndCount(stockage = fauxStockageFactory()))

    include(StorageSharedTests.storageSaveAndCount(stockage = fauxStockageFactory()))

    include(StorageSharedTests.storageSaveAndRead(stockage = fauxStockageFactory()))

    include((StorageSharedTests.storageSaveTwoAndRead(stockage = fauxStockageFactory())))

    include(StorageSharedTests.storageCannotSaveTwoOfTheSameId(stockage = fauxStockageFactory()))

    test("saveTicket should add a ticket ") {
        val leStockage = fauxStockageFactory()
        leStockage.save(testTicket)
        leStockage.listDesTickets.size shouldBe 1
    }

})


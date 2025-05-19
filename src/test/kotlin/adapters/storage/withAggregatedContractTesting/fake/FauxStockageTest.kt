package adapters.storage.withAggregatedContractTesting.fake

import adapters.FauxStockage
import adapters.storage.withAggregatedContractTesting.AggregatorOfContractTests
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import location.adapters.driven.storage.DTOs.TicketDto

class FauxStockageTest : FunSpec({

    val testTicket = TicketDto(1, 2)
    val fauxStockageFactory = { -> FauxStockage() }

    // zone de vérification du contrat par les tests partagés
    AggregatorOfContractTests.allTests.forEach {
        include(it(fauxStockageFactory()))
    }


    test("saveTicket should add a ticket ") {
        val leStockage = fauxStockageFactory()
        leStockage.save(testTicket)
        leStockage.listeOrdonneeDesTickets.size shouldBe 1
    }

})


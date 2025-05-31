package adapters.stockage.antiseche.withAggregatedContractTesting.fake

import adapters.stockage.antiseche.FauxStockage
import adapters.stockage.antiseche.withAggregatedContractTesting.AggregatorOfContractTests
import io.kotest.core.spec.style.FunSpec
import location.adapters.driven.storage.DTOs.TicketDto

class FauxStockageTest : FunSpec({

    val testTicket = TicketDto(1, 2)
    val fauxStockageFactory = { -> FauxStockage() }

    // zone de vérification du contrat par les tests partagés
    AggregatorOfContractTests.allTests.forEach {
        include(it(fauxStockageFactory()))
    }


})


package antiseche.adapters.driven.stockage.antiseche.withAggregatedContractTesting.fake

import adapters.driven.stockage.antiseche.XAdapter
import adapters.driven.stockage.antiseche.withAggregatedContractTesting.AggregatorOfContractTests
import io.kotest.core.spec.style.FunSpec

class FauxStockageTest : FunSpec({


    val fauxStockageFactory = { -> XAdapter() }

    // zone de vérification du contrat par les tests partagés
    AggregatorOfContractTests.allTests.forEach {
        include(it(fauxStockageFactory()))
    }


})


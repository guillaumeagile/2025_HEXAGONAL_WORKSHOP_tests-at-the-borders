package adapters.stockage.antiseche.withAggregatedContractTesting.mongoDB

import adapters.stockage.antiseche.withAggregatedContractTesting.AggregatorOfContractTests

import io.kotest.core.spec.style.FunSpec
import location.adapters.driven.storage.DTOs.TicketDto
import location.adapters.driven.storage.mongoDb.RepositoryMongoDb
import org.testcontainers.containers.MongoDBContainer

class MongoTests : FunSpec(
    {
        TicketDto(1, 200)

        beforeTest {
            println("Starting a test $it")
        }

        val mongoStockageFactory = {
            // Arrange
            val mongoDB = MongoDBContainer("mongo:8")
            mongoDB.start()
            val connectionUri = mongoDB.connectionString
            RepositoryMongoDb(connectionUri)
            // return repo
        }

        // zone de vérification du contrat par les tests partagés
        AggregatorOfContractTests.allTests.forEach {
            include(it(mongoStockageFactory()))
        }

    })
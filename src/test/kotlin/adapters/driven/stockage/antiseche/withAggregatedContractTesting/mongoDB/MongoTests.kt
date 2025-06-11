package adapters.driven.stockage.antiseche.withAggregatedContractTesting.mongoDB

import adapters.driven.stockage.antiseche.withAggregatedContractTesting.AggregatorOfContractTests
import io.kotest.core.spec.style.FunSpec
import location.adapters.driven.antiseche.mongoDb.RepositoryMongoDb

import org.testcontainers.containers.MongoDBContainer

class MongoTests : FunSpec(
    {

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
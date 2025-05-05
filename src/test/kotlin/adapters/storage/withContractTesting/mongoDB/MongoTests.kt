package adapters.storage.withContractTesting.mongoDB

import adapters.storage.withContractTesting.RepositoryContractTests

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import location.adapters.driven.storage.DTOs.TicketDto
import location.adapters.driven.storage.mongoDb.RepositoryMongoDb
import org.testcontainers.containers.MongoDBContainer

class MongoTests : FunSpec(
    {
        val testTicket = TicketDto(1, 200)

        beforeTest {
            println("Starting a test $it")
        }

        val stockageFactory = {
            // Arrange
            val mongoDB = MongoDBContainer("mongo:8")
            mongoDB.start()
            val connectionUri = mongoDB.connectionString
            RepositoryMongoDb(connectionUri)
            // return repo
        }

        // zone de vérification du contrat par les tests partagés
        RepositoryContractTests.allTests.forEach {
            include(it(stockageFactory()))
        }


    })
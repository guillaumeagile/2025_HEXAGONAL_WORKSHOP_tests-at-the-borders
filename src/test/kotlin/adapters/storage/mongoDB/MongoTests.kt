package adapters.storage.mongoDB

import adapters.storage.StorageSharedTests

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

        test("test local pour préparation") {
            val leStockage = stockageFactory()
            leStockage.save(testTicket)
            leStockage.getAll().getOrNull()?.first() shouldBe testTicket

        }

        // test hérités
        include(StorageSharedTests.storageNoSaveAndCount(stockage = stockageFactory()))

        include(StorageSharedTests.storageSaveAndCount(stockage = stockageFactory()))

        include(StorageSharedTests.storageSaveAndRead(stockage = stockageFactory()))

        include((StorageSharedTests.storageSaveTwoAndRead(stockage = stockageFactory())))

        include(StorageSharedTests.storageCannotSaveTwoOfTheSameId(stockage = stockageFactory()))

    })
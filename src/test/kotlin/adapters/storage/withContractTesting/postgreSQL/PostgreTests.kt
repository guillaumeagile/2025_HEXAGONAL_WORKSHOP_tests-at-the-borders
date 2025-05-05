package adapters.storage.withContractTesting.postgreSQL

import adapters.storage.withContractTesting.RepositoryContractTests

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import location.adapters.driven.storage.DTOs.TicketDto
import location.adapters.driven.storage.postGreSQL.TicketSqlRepository
import org.testcontainers.containers.PostgreSQLContainer

class PostgreTests : FunSpec({

    val testTicket = TicketDto(1, 200)
    val stockageFactory = {
        // Arrange
        val postgres = PostgreSQLContainer("postgres:16")
        postgres.start()
        val repo = TicketSqlRepository(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
        repo.createTableTicket()
        // return repo
        repo
    }

    test("test local pour préparation") {
        val leStockage = stockageFactory()
        leStockage.save(testTicket)
        leStockage.save(testTicket)
        leStockage.getAll().getOrNull()?.first() shouldBe testTicket
    }

    // zone de vérification du contrat par les tests partagés
    RepositoryContractTests.allTests.forEach {
        include(it(stockageFactory()))
    }

})
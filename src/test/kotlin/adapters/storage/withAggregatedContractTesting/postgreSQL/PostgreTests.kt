package adapters.storage.withAggregatedContractTesting.postgreSQL

import adapters.storage.withAggregatedContractTesting.AggregatorOfContractTests

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import location.adapters.driven.storage.DTOs.TicketDto
import location.adapters.driven.storage.postGreSQL.TicketSqlRepository
import org.testcontainers.containers.PostgreSQLContainer

class PostgreTests : FunSpec({

    val sqlStockageFactory = {
        // Arrange
        val postgres = PostgreSQLContainer("postgres:16")
        postgres.start()
        val repo = TicketSqlRepository(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
        repo.createTableTicket()
        // return repo
        repo
    }

    // zone de vérification du contrat par les tests partagés
    AggregatorOfContractTests.allTests.forEach {
        include(it(sqlStockageFactory()))
    }

    /*
    test("test local pour préparation") {
        val testTicket = TicketDto(1, 200)
        val leStockage = sqlStockageFactory()
        leStockage.save(testTicket)
        leStockage.save(testTicket)
        leStockage.getAll().getOrNull()?.first() shouldBe testTicket
    }
*/
})
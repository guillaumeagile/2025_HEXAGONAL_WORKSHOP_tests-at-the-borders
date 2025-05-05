package adapters.driven.oldfashionnedTests.postGreSQL

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import location.adapters.driven.storage.DTOs.TicketDto
import location.adapters.driven.storage.postGreSQL.TicketSqlRepository
import org.testcontainers.containers.PostgreSQLContainer

class AnExampleOfPostGreWithContainerTest : StringSpec({

    "simple CRUD avec test container".config(enabled = false) {
        // Arrange
        val postgres = PostgreSQLContainer("postgres:16")
        postgres.start()
        val repo = TicketSqlRepository(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
        repo.createTableTicket()
        repo.save(TicketDto(id = 1, elapsedMinutes = 30))
        repo.save(TicketDto(id = 2, elapsedMinutes = 18))

        // Act
        val countTickets = repo.count()
        // Assert
        countTickets.isSuccess shouldBe true
        countTickets.getOrThrow() shouldBe 2

        // Tear Down
        postgres.stop()
    }
})
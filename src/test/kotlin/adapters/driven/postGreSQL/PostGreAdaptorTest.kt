package adapters.driven.postGreSQL

import io.kotest.core.spec.style.StringSpec
import org.testcontainers.containers.PostgreSQLContainer
import io.kotest.matchers.*
import location.adapters.driven.storage.postGreSQL.TicketRepository
import location.adapters.driven.storage.DTOs.TicketDto

class PostGreAdaptorTest : StringSpec({

    "simple CRUD avec test container".config(enabled = false) {
        // Arrange
        val postgres = PostgreSQLContainer("postgres:16")
        postgres.start()
        val repo = TicketRepository(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
        repo.createTableTicket()
        repo.saveTicket(TicketDto(id = 1, elapseMinutes = 30))
        repo.saveTicket(TicketDto(id = 2, elapseMinutes = 18))

        // Act
        val countTickets = repo.countTickets()
        // Assert
        countTickets.isSuccess  shouldBe true
        countTickets.getOrThrow() shouldBe 2

        // Tear Down
        postgres.stop()



    }
})
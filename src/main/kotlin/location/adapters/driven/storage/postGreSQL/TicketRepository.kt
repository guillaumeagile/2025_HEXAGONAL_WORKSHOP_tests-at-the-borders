package location.adapters.driven.storage.postGreSQL

import location.ports.ITicketRepository
import location.adapters.driven.storage.DTOs.TicketDto
import java.sql.DriverManager


class TicketRepository(jdbcUrl: String, username: String, password: String) : ITicketRepository {
    private val storageConnection = DriverManager.getConnection(jdbcUrl, username, password)


    fun createTableTicket()  = runCatching {
        val createTableStatement = storageConnection.prepareStatement(
            """ 
                create table if not exists ticket(
                    id decimal(8) primary key,
                    park_time_minutes decimal(4,0)
                )
            """.trimIndent()
        )
        createTableStatement.execute()
    }

    override fun save(ticket: TicketDto)  = runCatching {
        val insertStatement = storageConnection.prepareStatement(
            "insert into ticket(id, park_time_minutes) values (?, ?)"
        )
        insertStatement.setInt(1, ticket.id)
        insertStatement.setInt(2, ticket.elapsedMinutes)
        insertStatement.execute()
    }

    override fun count(): Result< Int> = runCatching {
        val selectStatement = storageConnection.prepareStatement(
            "select count(*) as cardinalityTickets from ticket"
        )
        val result = selectStatement.executeQuery()
        result.next()
        var res =  result.getInt("cardinalityTickets")
        return Result.success( res)
    }

    override fun getAll(): Result<List<TicketDto>> {
        TODO("Not yet implemented")
    }
}
package location.adapters.driven.antiseche.postGreSQL

import location.domain.entities.Ticket
import location.ports.PourStockage
import java.sql.DriverManager

class PostgreAdapteurPourTicket(jdbcUrl: String, username: String, password: String) : PourStockage {

    private val storageConnection = DriverManager.getConnection(jdbcUrl, username, password)

    fun createTableTicket()  = runCatching {
        val createTableStatement = storageConnection.prepareStatement(
            """ 
                create table if not exists ticket(
                 id text primary key,
                  usagerId text
                )
            """.trimIndent()
        )
        createTableStatement.execute()
    }

    override fun StockerTicket(ticketResult: Result<Ticket>)= runCatching {
        val insertStatement = storageConnection.prepareStatement(
            """insert into ticket(id, park_time_minutes) 
                |values (?, ?)
                |ON CONFLICT (id)
                |DO UPDATE SET
                |park_time_minutes = EXCLUDED.park_time_minutes""".trimMargin()
        )
        val ticket = ticketResult.getOrNull()!!
        insertStatement.setString(1, ticket.id)
        insertStatement.setString(2, ticket.usagerId)
        insertStatement.execute()
        ticket
    }
}
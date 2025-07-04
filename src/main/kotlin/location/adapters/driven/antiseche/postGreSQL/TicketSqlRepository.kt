package location.adapters.driven.antiseche.postGreSQL

import location.domain.entities.Ticket
import location.ports.antiseche.PourTickets
import java.sql.DriverManager


class TicketSqlRepository(jdbcUrl: String, username: String, password: String) : PourTickets {
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

    override fun save(ticket: Ticket)  = runCatching {

        TODO("mapper le domain object vers le DTO")
        /*
        val insertStatement = storageConnection.prepareStatement(
            """insert into ticket(id, park_time_minutes) 
                |values (?, ?)
                |ON CONFLICT (id)
                |DO UPDATE SET
                |park_time_minutes = EXCLUDED.park_time_minutes""".trimMargin()
        )
        insertStatement.setInt(1, ticket.id)
        insertStatement.setInt(2, ticket.elapsedMinutes)
        insertStatement.execute()
        */
    }

    override fun count(): Result< Int> = runCatching {
        val selectStatement = storageConnection.prepareStatement(
            "select count(*) as cardinalityTickets from ticket"
        )
        val result = selectStatement.executeQuery()
        result.next()
        val res =  result.getInt("cardinalityTickets")
        return Result.success( res)
    }

    override fun getAll(): Result<List<Ticket>> {
        TODO("mapper le DTO vers le domain object")
        /*
        val selectStatement = storageConnection.prepareStatement(
            "select * from ticket order by id"
        )
        val resultQuery = selectStatement.executeQuery()
        val listOfTickets = ArrayList<TicketDto>()
        while (resultQuery.next()) {
            val aTicketDto = TicketDto(
                id = resultQuery.getInt("id"),
                elapsedMinutes = resultQuery.getInt("park_time_minutes")
            )
            listOfTickets.add(aTicketDto)
        }
        return Result.success(listOfTickets)
        
         */
    }

    override fun reset(): Result<Boolean> {
        val deleteStatement = storageConnection.prepareStatement(
            "delete from ticket"
        )
        return Result.success(
            deleteStatement.execute()
        )
    }
}
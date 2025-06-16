package location.adapters.driven.antiseche.postGreSQL

import kotlinx.datetime.toKotlinLocalDateTime
import location.domain.entities.Ticket
import location.domain.valueObjects.Devises
import location.domain.valueObjects.Monnaie
import location.ports.antiseche.PourTickets
import java.sql.DriverManager

/**
 * Cette classe permet de stocker les tickets dans une base de données PostgreSQL.
 */
class TicketSqlRepository(jdbcUrl: String, username: String, password: String) : PourTickets {
    private val storageConnection = DriverManager.getConnection(jdbcUrl, username, password)


    fun createTableTicket()  = runCatching {
        val createTableStatement = storageConnection.prepareStatement(
            """ 
                CREATE TABLE IF NOT EXISTS ticket(
                    id VARCHAR(255) PRIMARY KEY,
                    usager_id VARCHAR(255) NOT NULL,
                    moment_entree TIMESTAMP NOT NULL,
                    duree_location BIGINT NOT NULL,
                    moment_sortie TIMESTAMP NOT NULL,
                    prix_valeur DECIMAL(10, 2) NOT NULL,
                    prix_devise VARCHAR(10) NOT NULL
                )
            """.trimIndent()
        )
        createTableStatement.execute()
    }

    override fun save(ticket: Ticket): Result<Ticket> = runCatching {
        val insertStatement = storageConnection.prepareStatement(
            """
            INSERT INTO ticket(
                id, 
                usager_id, 
                moment_entree, 
                duree_location, 
                moment_sortie, 
                prix_valeur, 
                prix_devise
            ) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (id) 
            DO UPDATE SET 
                usager_id = EXCLUDED.usager_id,
                moment_entree = EXCLUDED.moment_entree,
                duree_location = EXCLUDED.duree_location,
                moment_sortie = EXCLUDED.moment_sortie,
                prix_valeur = EXCLUDED.prix_valeur,
                prix_devise = EXCLUDED.prix_devise
            """.trimIndent()
        )
        
        insertStatement.setString(1, ticket.id)
        insertStatement.setString(2, ticket.usagerId)
        insertStatement.setTimestamp(3, java.sql.Timestamp.valueOf(ticket.momentEntree.toString()))
        insertStatement.setLong(4, ticket.dureeDeLocation.inWholeMilliseconds)
        insertStatement.setTimestamp(5, java.sql.Timestamp.valueOf(ticket.momentSortie.toString()))
        insertStatement.setDouble(6, ticket.prix.valeur)
        insertStatement.setString(7, ticket.prix.devise.name)
        
        insertStatement.executeUpdate()
        ticket
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

    override fun getAll(): Result<List<Ticket>> = runCatching {
        val selectStatement = storageConnection.prepareStatement(
            """
            SELECT 
                id, 
                usager_id, 
                moment_entree, 
                duree_location, 
                moment_sortie, 
                prix_valeur, 
                prix_devise
            FROM ticket
            """.trimIndent()
        )
        
        val resultSet = selectStatement.executeQuery()
        val tickets = mutableListOf<Ticket>()
        
        while (resultSet.next()) {
            val id = resultSet.getString("id")
            val usagerId = resultSet.getString("usager_id")
            val momentEntree = resultSet.getTimestamp("moment_entree")!!.toLocalDateTime().toKotlinLocalDateTime()
            val dureeDeLocation = kotlin.time.Duration.parse("PT${resultSet.getLong("duree_location")} ms")
            val momentSortie = resultSet.getTimestamp("moment_sortie")!!.toLocalDateTime().toKotlinLocalDateTime()
            val prixValeur = resultSet.getDouble("prix_valeur")
            val prixDevise = Devises.valueOf(resultSet.getString("prix_devise"))
            
            val ticket = Ticket(
                id = id,
                usagerId = usagerId,
                momentEntree = momentEntree ,
                dureeDeLocation = dureeDeLocation,
                momentSortie = momentSortie,
                prix = Monnaie(prixValeur, prixDevise)
            )
            
            tickets.add(ticket)
        }
        
        tickets
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
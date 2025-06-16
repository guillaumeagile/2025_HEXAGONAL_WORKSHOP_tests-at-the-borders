package location.adapters.driven.antiseche.postGreSQL

import location.domain.entities.Ticket
import location.domain.valueObjects.Devises
import location.domain.valueObjects.Monnaie
import location.ports.antiseche.PourTickets
import java.sql.DriverManager

/**
 * Cette classe permet de stocker les tickets dans une base de données PostgreSQL.
 */
class TicketSqlRepository(jdbcUrl: String, username: String, password: String) : PourTickets {
    private val storageConnection = DriverManager.getConnection(jdbcUrl, username, password).apply {
        // Disable auto-commit to manage transactions explicitly
        autoCommit = false
    }

    init {
        storageConnection
    }

    fun createTableTicket() = runCatching {
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
        val result = createTableStatement.executeUpdate()
        storageConnection.commit()
        result
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
        
        // Convert Kotlin LocalDateTime to java.sql.Timestamp properly
        val momentEntreeJava = java.time.LocalDateTime.of(
            ticket.momentEntree.year,
            ticket.momentEntree.monthNumber,
            ticket.momentEntree.dayOfMonth,
            ticket.momentEntree.hour,
            ticket.momentEntree.minute,
            ticket.momentEntree.second,
            ticket.momentEntree.nanosecond
        )
        val momentSortieJava = java.time.LocalDateTime.of(
            ticket.momentSortie.year,
            ticket.momentSortie.monthNumber,
            ticket.momentSortie.dayOfMonth,
            ticket.momentSortie.hour,
            ticket.momentSortie.minute,
            ticket.momentSortie.second,
            ticket.momentSortie.nanosecond
        )
        
        val timestampEntree = java.sql.Timestamp.valueOf(momentEntreeJava)
        val timestampSortie = java.sql.Timestamp.valueOf(momentSortieJava)
        
        insertStatement.setString(1, ticket.id)
        insertStatement.setString(2, ticket.usagerId)
        insertStatement.setTimestamp(3, timestampEntree)
        insertStatement.setLong(4, ticket.dureeDeLocation.inWholeMilliseconds)
        insertStatement.setTimestamp(5, timestampSortie)
        insertStatement.setDouble(6, ticket.prix.valeur)
        insertStatement.setString(7, ticket.prix.devise.name)
        
        val rowsAffected = insertStatement.executeUpdate()
        storageConnection.commit()
        
        ticket
    }

    override fun count(): Result<Int> = runCatching {
        val selectStatement = storageConnection.prepareStatement(
            "SELECT COUNT(*) AS cardinalityTickets FROM ticket"
        )
        val result = selectStatement.executeQuery()
        result.next()
        val count = result.getInt("cardinalityTickets")
        count
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
            ORDER BY id
            """.trimIndent()
        )
        
        val resultSet = selectStatement.executeQuery()
        val tickets = mutableListOf<Ticket>()
        
        while (resultSet.next()) {
            try {
                val id = resultSet.getString("id")
                val usagerId = resultSet.getString("usager_id")
                
                // Convert java.sql.Timestamp to Kotlin LocalDateTime properly
                val momentEntreeTimestamp = resultSet.getTimestamp("moment_entree")
                val momentEntreeJava = momentEntreeTimestamp.toLocalDateTime()
                val momentEntree = kotlinx.datetime.LocalDateTime(
                    year = momentEntreeJava.year,
                    monthNumber = momentEntreeJava.monthValue,
                    dayOfMonth = momentEntreeJava.dayOfMonth,
                    hour = momentEntreeJava.hour,
                    minute = momentEntreeJava.minute,
                    second = momentEntreeJava.second,
                    nanosecond = momentEntreeJava.nano
                )
                
                // Create Duration directly from milliseconds without parsing
                val durationMs = resultSet.getLong("duree_location")
                val dureeDeLocation = kotlin.time.Duration.parseIsoString(  "PT${durationMs/1000}S")
                
                val momentSortieTimestamp = resultSet.getTimestamp("moment_sortie")
                val momentSortieJava = momentSortieTimestamp.toLocalDateTime()
                val momentSortie = kotlinx.datetime.LocalDateTime(
                    year = momentSortieJava.year,
                    monthNumber = momentSortieJava.monthValue,
                    dayOfMonth = momentSortieJava.dayOfMonth,
                    hour = momentSortieJava.hour,
                    minute = momentSortieJava.minute,
                    second = momentSortieJava.second,
                    nanosecond = momentSortieJava.nano
                )
                
                val prixValeur = resultSet.getDouble("prix_valeur")
                val prixDevise = Devises.valueOf(resultSet.getString("prix_devise"))
                
                val ticket = Ticket(
                    id = id,
                    usagerId = usagerId,
                    momentEntree = momentEntree,
                    dureeDeLocation = dureeDeLocation,
                    momentSortie = momentSortie,
                    prix = Monnaie(prixValeur, prixDevise)
                )
                
                tickets.add(ticket)
            } catch (e: Exception) {
                throw e
            }
        }
        
        tickets
    }

    override fun reset(): Result<Boolean> = runCatching {
        val deleteStatement = storageConnection.prepareStatement(
            "TRUNCATE TABLE ticket RESTART IDENTITY"
        )
        val rowsAffected = deleteStatement.executeUpdate()
        storageConnection.commit()
        true
    }
}
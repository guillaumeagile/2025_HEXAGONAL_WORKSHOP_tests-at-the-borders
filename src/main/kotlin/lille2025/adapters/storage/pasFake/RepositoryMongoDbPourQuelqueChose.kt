package lille2025.adapters.storage.pasFake

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.kotlin.client.MongoClient
import lille2025.ports.PourPersisterUnTicket
import location.domain.entities.Ticket
import org.bson.codecs.pojo.annotations.BsonId
import java.time.Instant


class RepositoryMongoDbPourQuelqueChose(connexionUrl: String) : PourPersisterUnTicket {


    data class DTOMongoTicket(
        @BsonId
        val id: String,
        val usagerId: String,  // son email
        val momentEntree: Instant,
        val dureeDeLocationEnMinutes: Long,
        val momentSortie: Instant,
        val prixEnEuros: Double)


    private val mongoClient = MongoClient.Factory.create(connexionUrl)
    private val db = mongoClient.getDatabase("ticketsDevLille")
    private val ticketCollection = db.getCollection<DTOMongoTicket>("ledgerTicket")

    private fun saveOrUpdateTicket(ticket: DTOMongoTicket) {
        val filter = Filters.eq("_id", ticket.id)
        val options = ReplaceOptions().upsert(true)
        ticketCollection.replaceOne(filter, ticket, options)
    }


    override fun enregistrer(ticket: Ticket?): Ticket? {
        TODO("Not yet implemented")
    }

    override fun compterLesTicketsExistants(): Int {
        TODO("Not yet implemented")
    }

//class (connectionUri: String) {

}

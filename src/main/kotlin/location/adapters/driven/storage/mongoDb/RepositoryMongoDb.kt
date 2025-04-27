package location.adapters.driven.storage.mongoDb

import com.mongodb.kotlin.client.MongoClient
import location.ports.ITicketRepository
import org.bson.codecs.pojo.annotations.BsonId


data class DTOMongoTicket(
    @BsonId
    val id: Int,
    val parkTimeMinutes: Int
)


class RepositoryMongoDb(connexionUrl: String)  {
    private val mongoClient = com.mongodb.kotlin.client.MongoClient.Factory.create(connexionUrl)
    private val db = mongoClient.getDatabase("tickets")
    private val ticketCollection = db.getCollection<DTOMongoTicket>("ledgerTicket")

    fun createTicket(ticket: DTOMongoTicket) {
        ticketCollection.insertOne(ticket)
    }

    fun countTickets(): Long {
        return ticketCollection.countDocuments()
    }

    fun getTickets(): List<DTOMongoTicket> {
        val l = mutableListOf<DTOMongoTicket>()
        return ticketCollection.find().toCollection(l)
    }

}
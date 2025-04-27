package location.adapters.driven.storage.mongoDb

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.kotlin.client.MongoClient
import location.adapters.driven.storage.DTOs.TicketDto
import location.ports.ITicketRepository
import org.bson.codecs.pojo.annotations.BsonId


class RepositoryMongoDb(connexionUrl: String) : ITicketRepository {

     data class DTOMongoTicket(
        @BsonId
        val id: Long,
        val parkTimeMinutes: Int
    )
    private val mongoClient = MongoClient.Factory.create(connexionUrl)
    private val db = mongoClient.getDatabase("tickets")
    private val ticketCollection = db.getCollection<DTOMongoTicket>("ledgerTicket")

    private fun saveOrUpdateTicket(ticket: DTOMongoTicket) {
        val filter = Filters.eq("_id", ticket.id)
        val options = ReplaceOptions().upsert(true)
        ticketCollection.replaceOne(filter, ticket, options)
    }

    override fun save(ticket: TicketDto): Result<Boolean> {
        val adaptedTicket = DTOMongoTicket(id = ticket.id.toLong(), parkTimeMinutes = ticket.elapsedMinutes)
        saveOrUpdateTicket(adaptedTicket)
        return Result.success(true)
    }

    override fun count(): Result<Int> = runCatching {
         ticketCollection.countDocuments().toInt()
    }

    override fun getAll(): Result<List<TicketDto>>  = runCatching {
        ticketCollection
            .find()
            .sort(ascending("_id"))
            .map  { t -> TicketDto(id = t.id.toInt(), elapsedMinutes = t.parkTimeMinutes) }.toList()
    }

}
package location.adapters.driven.antiseche.mongoDb

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.kotlin.client.MongoClient
import dev.krud.shapeshift.ShapeShiftBuilder
import io.nacular.measured.units.Time.Companion.minutes
import io.nacular.measured.units.times
import kotlinx.datetime.LocalDateTime
import location.adapters.driven.storage.DTOs.TicketDto
import location.domain.entities.Ticket
import location.ports.antiseche.PourTickets
import org.bson.codecs.pojo.annotations.BsonId
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


class RepositoryMongoDb(connexionUrl: String) : PourTickets {


    data class DTOMongoTicket(
        @BsonId
        val id: String,
        val usagerId: String,  // son email
        val momentEntree: LocalDateTime,
        val dureeDeLocationEnMinutes: Long,
        val momentSortie: LocalDateTime,
        val prixEnEuros: Double)


    private val mongoClient = MongoClient.Factory.create(connexionUrl)
    private val db = mongoClient.getDatabase("tickets")
    private val ticketCollection = db.getCollection<DTOMongoTicket>("ledgerTicket")

    private fun saveOrUpdateTicket(ticket: DTOMongoTicket) {
        val filter = Filters.eq("_id", ticket.id)
        val options = ReplaceOptions().upsert(true)
        ticketCollection.replaceOne(filter, ticket, options)
    }

    override fun save(ticket: Ticket): Result<Boolean> {
        // mapper le domain object vers le DTO
        val adaptedTicket = DTOMongoTicket(
            id = ticket.id,
            usagerId = ticket.usagerId,
            momentEntree = ticket.momentEntree,
            dureeDeLocationEnMinutes =  ticket.dureeDeLocation.inWholeMinutes,
            momentSortie = ticket.momentSortie,
            prixEnEuros = ticket.prix.EnEuros()
        )
            //DTOMongoTicket(id = ticket.id.toLong(), parkTimeMinutes = ticket.elapsedMinutes)
        saveOrUpdateTicket(adaptedTicket)
        return Result.success(true)
    }

    override fun count(): Result<Int> = runCatching {
         ticketCollection.countDocuments().toInt()
    }

    override fun getAll(): Result<List<Ticket>>  = runCatching {
        TODO("mapper le DTO vers le domain object")
      /*  ticketCollection
            .find()
            .sort(ascending("_id"))
            .map  { t -> Ticket(id = t.id.toInt(), elapsedMinutes = t.parkTimeMinutes) }.toList()
            */
    }

    override fun reset(): Result<Boolean> {
        ticketCollection.deleteMany(Filters.empty())
        return Result.success(true)
    }

}
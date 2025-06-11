package location.adapters.driven.antiseche.mongoDb

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.kotlin.client.MongoClient
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import location.domain.entities.Ticket
import location.domain.valueObjects.Monnaie
import location.ports.antiseche.PourTickets
import org.bson.codecs.pojo.annotations.BsonId
import java.time.Instant
import java.time.ZoneOffset
import kotlin.time.Duration.Companion.minutes


class RepositoryMongoDb(connexionUrl: String) : PourTickets {


    data class DTOMongoTicket(
        @BsonId
        val id: String,
        val usagerId: String,  // son email
        val momentEntree: Instant,
        val dureeDeLocationEnMinutes: Long,
        val momentSortie: Instant,
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
            momentEntree = ticket.momentEntree.toJavaLocalDateTime().toInstant(ZoneOffset.UTC),
            dureeDeLocationEnMinutes =  ticket.dureeDeLocation.inWholeMinutes,
            momentSortie = ticket.momentSortie.toJavaLocalDateTime().toInstant(ZoneOffset.UTC),
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
        ticketCollection
            .find()
            .sort(ascending("_id"))
            .map { t -> 
                Ticket(
                    id = t.id,
                    usagerId = t.usagerId,
                    momentEntree = Instant.ofEpochMilli(t.momentEntree.toEpochMilli()).atZone(ZoneOffset.UTC).toLocalDateTime().toKotlinLocalDateTime(),
                    dureeDeLocation = t.dureeDeLocationEnMinutes.minutes,
                    momentSortie = Instant.ofEpochMilli(t.momentSortie.toEpochMilli()).atZone(ZoneOffset.UTC).toLocalDateTime().toKotlinLocalDateTime(),
                    prix =  Monnaie.Euros(  t.prixEnEuros)
                )
            }.toList()
    }

    override fun reset(): Result<Boolean> {
        ticketCollection.deleteMany(Filters.empty())
        return Result.success(true)
    }

}
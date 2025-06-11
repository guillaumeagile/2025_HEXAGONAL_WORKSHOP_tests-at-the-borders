package location.adapters.driven.antiseche.valKey

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import location.domain.entities.Ticket
import location.domain.valueObjects.Monnaie
import location.ports.antiseche.PourTickets
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import kotlin.time.Duration.Companion.minutes

// généré par IA, mais est ce que ca marche ?
class ValKeyAdapter(redisUrl: String = "redis://localhost:6379") : PourTickets {

    // Redis DTO for storing ticket data
    data class RedisTicketDTO(
        val id: String,
        val usagerId: String,
        val momentEntreeMillis: Long,
        val dureeDeLocationMinutes: Int,
        val momentSortieMillis: Long,
        val prixValeur: Double,
        val prixDevise: String
    )

    private val redissonClient: RedissonClient
    private val ticketMapName = "tickets"

    init {
        val config = Config()
        config.useSingleServer().address = redisUrl
        redissonClient = Redisson.create(config)
    }

    // Convert domain Ticket to Redis DTO
    private fun toRedisDTO(ticket: Ticket): RedisTicketDTO {
        return RedisTicketDTO(
            id = ticket.id,
            usagerId = ticket.usagerId,
            momentEntreeMillis = ticket.momentEntree.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
            dureeDeLocationMinutes = ticket.dureeDeLocation.inWholeMinutes.toInt(),
            momentSortieMillis = ticket.momentSortie.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
            prixValeur = ticket.prix.valeur,
            prixDevise = ticket.prix.devise.toString()
        )
    }

    // Convert Redis DTO to domain Ticket
    private fun toDomainTicket(dto: RedisTicketDTO): Ticket {
        return Ticket(
            id = dto.id,
            usagerId = dto.usagerId,
            momentEntree = Instant.fromEpochMilliseconds(dto.momentEntreeMillis).toLocalDateTime(TimeZone.currentSystemDefault()),
            dureeDeLocation = dto.dureeDeLocationMinutes.minutes,
            momentSortie = Instant.fromEpochMilliseconds(dto.momentSortieMillis).toLocalDateTime(TimeZone.currentSystemDefault()),
            prix = Monnaie.Euros(dto.prixValeur)
        )
    }

    override fun save(ticket: Ticket): Result<Boolean> = runCatching {
        val ticketMap = redissonClient.getMap<String, RedisTicketDTO>(ticketMapName)
        ticketMap.put(ticket.id, toRedisDTO(ticket))
        true
    }

    override fun count(): Result<Int> = runCatching {
        val ticketMap = redissonClient.getMap<String, RedisTicketDTO>(ticketMapName)
        ticketMap.size
    }

    override fun getAll(): Result<List<Ticket>> = runCatching {
        val ticketMap = redissonClient.getMap<String, RedisTicketDTO>(ticketMapName)
        ticketMap.values.map { toDomainTicket(it) }
    }

    override fun reset(): Result<Boolean> = runCatching {
        val ticketMap = redissonClient.getMap<String, RedisTicketDTO>(ticketMapName)
        ticketMap.clear()
        true
    }

    // Method to get a specific ticket by ID - useful for the obtenirTicket method in PaiementLocation
    fun getTicketById(id: String): Result<Ticket?> = runCatching {
        val ticketMap = redissonClient.getMap<String, RedisTicketDTO>(ticketMapName)
        val dto = ticketMap[id]
        dto?.let { toDomainTicket(it) }
    }

    // Close the Redisson client when done
    fun close() {
        redissonClient.shutdown()
    }
}
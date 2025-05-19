package adapters

import location.adapters.driven.storage.DTOs.TicketDto
import location.ports.ITicketRepository

class FauxStockage : ITicketRepository {

    val listeOrdonneeDesTickets = hashMapOf<Int, TicketDto>()

    override fun save(ticket: TicketDto) = runCatching {
      //  listDesTickets.add(ticket)
        listeOrdonneeDesTickets.put(ticket.id, ticket)
        true
    }

    override fun count(): Result<Int> = Result.success(listeOrdonneeDesTickets.size + 1)

    override fun getAll(): Result<List<TicketDto>> = Result.success( listeOrdonneeDesTickets.map( { it.value }) )

    override fun reset(): Result<Boolean> {
        listeOrdonneeDesTickets.clear()
        return Result.success(true)
    }

}
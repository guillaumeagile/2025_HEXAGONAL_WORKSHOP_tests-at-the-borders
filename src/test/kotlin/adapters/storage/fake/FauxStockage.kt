package adapters.storage.fake

import location.ports.ITicketRepository

import location.adapters.driven.storage.DTOs.TicketDto

class FauxStockage : ITicketRepository {

    val listeOrdonneeDesTickets = hashMapOf<Int, TicketDto>()

    override fun save(ticket: TicketDto) = runCatching {
      //  listDesTickets.add(ticket)
        listeOrdonneeDesTickets.put(ticket.id, ticket)
        true
    }

    override fun count(): Result<Int> = Result.success(listeOrdonneeDesTickets.size)

    override fun getAll(): Result<List<TicketDto>> = Result.success( listeOrdonneeDesTickets.map( { it.value }) )

}



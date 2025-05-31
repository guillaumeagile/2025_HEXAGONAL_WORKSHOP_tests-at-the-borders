package adapters

import location.adapters.driven.storage.DTOs.TicketDto
import location.ports.PourStocker

class FauxStockage : PourStocker {

   // val listeOrdonneeDesTickets = hashMapOf<Int, TicketDto>()
    val listDesTickets = ArrayList<TicketDto>()

    override fun save(ticket: TicketDto) = runCatching {
        listDesTickets.add(ticket)
      //  listeOrdonneeDesTickets.put(ticket.id, ticket)
        true
    }

    override fun count(): Result<Int> = Result.success(
    //    listeOrdonneeDesTickets.size + 1
        listDesTickets.size
    )

    override fun getAll(): Result<List<TicketDto>> = Result.success(
    //    listeOrdonneeDesTickets.map( { it.value })
        listDesTickets
    )

    override fun reset(): Result<Boolean> {
        //listeOrdonneeDesTickets.clear()
        listDesTickets.clear()
        return Result.success(true)
    }

}
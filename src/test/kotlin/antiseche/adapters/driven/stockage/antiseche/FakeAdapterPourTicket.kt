package antiseche.adapters.driven.stockage.antiseche

import location.domain.entities.Ticket
import location.ports.antiseche.PourTickets

class FakeAdapterPourTicket : PourTickets {

   // val listeOrdonneeDesTickets = hashMapOf<Int, TicketDto>()
    val listDesTickets = ArrayList<Ticket>()

    override fun save(ticket: Ticket) = runCatching {
        listDesTickets.add(ticket)
      //  listeOrdonneeDesTickets.put(ticket.id, ticket)
        true
    }

    override fun count(): Result<Int> = Result.success(
    //    listeOrdonneeDesTickets.size + 1
        listDesTickets.size
    )

    override fun getAll(): Result<List<Ticket>> = Result.success(
    //    listeOrdonneeDesTickets.map( { it.value })
        listDesTickets
    )

    override fun reset(): Result<Boolean> {
        //listeOrdonneeDesTickets.clear()
        listDesTickets.clear()
        return Result.success(true)
    }

}
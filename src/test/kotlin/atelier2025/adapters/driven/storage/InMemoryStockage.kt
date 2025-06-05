package atelier2025.adapters.driven.storage

import location.domain.entities.Ticket
import location.ports.PourStockage

 class InMemoryStockage : PourStockage {

     val length: Int
         get() = tickets.size

     private val tickets = mutableListOf<Ticket>()

    override fun StockerTicket(ticket: Result<Ticket>): Result<Ticket> {
        tickets.add(ticket.getOrNull()!!)
        return Result.success(ticket.getOrNull()!!)
    }

     fun reset() {
         tickets.clear()
     }

 }

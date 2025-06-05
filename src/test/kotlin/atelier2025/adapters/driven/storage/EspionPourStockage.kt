package atelier2025.adapters.driven.storage

import location.domain.entities.Ticket
import location.ports.PourStockage

class EspionPourStockage : PourStockage {

    var appelOk:  Boolean = false
        get() = field

    override fun StockerTicket(ticket: Result<Ticket>) :  Result<Ticket> {

       appelOk = true
       return  Result.success(ticket.getOrNull()!!)

     }
}

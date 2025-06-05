package location.ports

import location.domain.entities.Ticket

interface PourStockage {

    fun StockerTicket(ticket: Result<Ticket>) : Result<Ticket>


}
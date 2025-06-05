package location.domain.useCases

import kotlinx.datetime.LocalDateTime
import location.domain.entities.Ticket
import location.domain.usine.UsineDeTickets
import location.domain.valueObjects.DureeDeLocation
import location.ports.PourAvoirHeure
import location.ports.PourStockage

class PaiementLocation(
    val usineDeTickets: UsineDeTickets,
    val stockageDeTickets: PourStockage,
    val horloges: PourAvoirHeure
) {

    internal fun payerLocationImmediate(duree: DureeDeLocation): Result<Ticket> {

        val heureEntree = this.horloges.quelleHeureEstIl()
        val ticket =  usineDeTickets.creation( heureEntree, duree.enMinutes)

        if ( ticket.isFailure )
           return ticket

        val ticketStocke = stockageDeTickets.StockerTicket(ticket)

        return ticketStocke

       // return ticket.getOrDefault( Ticket.enEchec() )
    }



    fun payerLocationEnAvance(duree: DureeDeLocation, heureEntree: LocalDateTime) : Ticket {
         TODO()
    }

    fun obtenirTicket(id: String) : Ticket{
        TODO("il faut chercher le ticket dans le stockage")
    }
}
package location.domain.useCases

import location.domain.entities.Ticket
import location.domain.usine.UsineDeTickets
import location.domain.valueObjects.DureeDeLocation
import location.ports.PourAvoirHeure
import location.ports.PourStocker

class PaiementLocation(
    val usineDeTickets: UsineDeTickets,
    val fauxStockageDeTickets: PourStocker,
    val horloges: PourAvoirHeure
) {

    fun PayerLocationImmediate( duree: DureeDeLocation) : Ticket {

        val heureEntree = this.horloges.quelleHeureEstIl()
        val ticket =  usineDeTickets.creation( heureEntree, duree.enMinutes)

        if ( ticket.isFailure )
           return Ticket.enEchec()


     // à vous de jouer pour choisir comment on va mettre le ticket dans le repo
     //   fauxStockageDeTickets.X(ticket)

        return ticket.getOrDefault( Ticket.enEchec() )
    }

    fun ObtenirTicket(id: String) : Ticket{
        TODO()
    }
}
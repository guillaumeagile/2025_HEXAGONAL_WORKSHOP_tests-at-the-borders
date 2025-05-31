package location.domain.useCases

import location.domain.entities.Ticket
import location.domain.valueObjects.DureeDeLocation
import location.ports.ITicketRepository

class PaiementLocation(fauxStockageDeTickets: ITicketRepository) {

    fun PayerLocationImmediate( duree: DureeDeLocation) : Ticket {
        TODO("à moi de coder la partie metier pure")



     // à vous de jouer pour choisir comment on va mettre le ticket dans le repo
     //   fauxStockageDeTickets.X(ticket)
    }

    fun ObtenirTicket(id: String) : Ticket{
        TODO()
    }
}
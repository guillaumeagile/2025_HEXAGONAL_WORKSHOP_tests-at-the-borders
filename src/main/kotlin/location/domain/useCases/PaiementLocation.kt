package location.domain.useCases

import kotlinx.datetime.LocalDateTime
import location.domain.entities.Ticket
import location.domain.usine.UsineDeTickets
import location.domain.valueObjects.DureeDeLocation
import location.ports.PourAvoirHeure
import location.ports.antiseche.PourTickets

class PaiementLocation(
    val usineDeTickets: UsineDeTickets,
    val stockageDeTickets: PourTickets,
    val horloges: PourAvoirHeure
) {

    fun payerLocationImmediate(duree: DureeDeLocation) : Ticket {

        val heureEntree = this.horloges.quelleHeureEstIl()
        val ticket =  usineDeTickets.creation( heureEntree, duree.enMinutes)


        if ( ticket.isFailure )
           return Ticket.enEchec()

     // à vous de jouer pour choisir comment on va mettre le ticket dans le repo
     //   stockageDeTickets.X(ticket)

        return ticket.getOrDefault( Ticket.enEchec() )
    }

    fun payerLocationEnAvance(duree: DureeDeLocation, heureEntree: LocalDateTime) : Ticket {
         TODO()
    }

    fun obtenirTicket(id: String) : Ticket{
        TODO("il faut chercher le ticket dans le stockage")
    }
}
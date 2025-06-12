package location.ports

import location.domain.entities.Ticket

interface PourPersisterUnTicket {

    fun faireQuelqueChose() : Boolean

    fun enregistrer(ticket: Ticket?) : Ticket?

    fun compterLesTicketsExistants() : Int
}
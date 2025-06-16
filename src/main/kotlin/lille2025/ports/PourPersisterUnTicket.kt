package lille2025.ports

import location.domain.entities.Ticket

interface PourPersisterUnTicket {


    fun enregistrer(ticket: Ticket?) : Ticket?

    fun compterLesTicketsExistants() : Int
}
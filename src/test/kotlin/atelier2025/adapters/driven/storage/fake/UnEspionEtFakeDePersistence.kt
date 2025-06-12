package atelier2025.adapters.driven.storage.fake

import location.domain.entities.Ticket
import location.ports.PourPersisterUnTicket

class UnEspionEtFakeDePersistence : PourPersisterUnTicket {
    override fun faireQuelqueChose(): Boolean = true
    var compteur: Int = 0

    val listeDesTickets = mutableListOf<Ticket>()

    override fun enregistrer(ticket: Ticket?): Ticket? {
        listeDesTickets.add(ticket!!)
        compteur++
        return ticket
    }

    override fun compterLesTicketsExistants(): Int {
        return listeDesTickets.size
    }

    fun laMethodeAEtéAppelée(): Int = compteur
}
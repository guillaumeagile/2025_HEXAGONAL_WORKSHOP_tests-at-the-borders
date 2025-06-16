package lille2025.adapters.storage.fake

import location.domain.entities.Ticket
import lille2025.ports.PourPersisterUnTicket

class UnEspionEtFakeDePersistence : PourPersisterUnTicket {

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
package location.domain.entities

import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import kotlinx.datetime.LocalDateTime
import location.ports.IJeDonneDesIdentifiants

// QUELLE EST LA RESPONSABILITE DE CETTE CLASSE ?
// fournir des tickets
// on peut lui mettre des contraintes métiers
// par exemple: pas de ticket avant 8h
// ou encore: pas de ticket d'une duree trop courte (30 minutes)
// pas de ticket d'une duree trop longue > 8h (480 minutes)
// ou encore : pas de ticket si le parc est plein

class UsineDeTickets(val idGenerateur: IJeDonneDesIdentifiants) {

    fun Creation(heureEntree: LocalDateTime, dureeMinutes: Int): Ticket {
        TODO()
    }

    //TODO: choisir quelle Primitive Obsession


    fun Creation(heureEntree: LocalDateTime, duree: Measure<Time>): Ticket {
        TODO()
    }

}




//val id = this.idGenerateur.idSuivant()
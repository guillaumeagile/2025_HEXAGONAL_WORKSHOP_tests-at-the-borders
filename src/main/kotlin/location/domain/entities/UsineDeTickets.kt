package location.domain.entities

import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import kotlinx.datetime.LocalDateTime
import location.ports.IJeDonneDesIdentifiants

// TODO: se ré accorder sur ce truc là

class UsineDeTickets(val idGenerateur: IJeDonneDesIdentifiants) {

    fun Creation(heureEntree: LocalDateTime, dureeMinutes: Int): Ticket {
        TODO()
    }

    fun Creation(heureEntree: LocalDateTime, duree: Measure<Time>): Ticket {
        TODO()
    }

}




//val id = this.idGenerateur.idSuivant()
package location.domain.entities

import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.Time.Companion.minutes
import io.nacular.measured.units.times
import kotlinx.datetime.LocalDateTime
import location.domain.valueObjects.Monnaie
import location.ports.IJeDonneDesIdentifiants

// QUELLE EST LA RESPONSABILITE DE CETTE CLASSE ?
// fournir des tickets
// on peut lui mettre des contraintes métiers
// par exemple: pas de ticket avant 8h
// ou encore: pas de ticket d'une duree trop courte (30 minutes)
// pas de ticket d'une duree trop longue > 8h (480 minutes)
// ou encore : pas de ticket si le parc est plein

class UsineDeTickets(val idGenerateur: IJeDonneDesIdentifiants) {

    fun creation(heureEntree: LocalDateTime, dureeMinutes: Int): Result<Ticket>  {

        val tempsDemmande=  dureeMinutes * minutes;
        val temp = Ticket.builder( heureEntree, tempsDemmande,  Monnaie.Euros(0.0))
        val ticket = temp .map {
            it.copy(     id = this.idGenerateur.idSuivant())
        }
          return ticket

    }

    //TODO: choisir quelle Primitive Obsession


    fun creation(heureEntree: LocalDateTime, duree: Measure<Time>): Ticket {
        TODO()
    }

}




//val id = this.idGenerateur.idSuivant()
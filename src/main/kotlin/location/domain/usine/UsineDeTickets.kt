package location.domain.usine

import io.nacular.measured.units.Time
import io.nacular.measured.units.times
import kotlinx.datetime.LocalDateTime
import location.domain.entities.Ticket
import location.domain.valueObjects.Monnaie
import location.ports.PourLesIdentifiants

class UsineDeTickets(
    val idGenerateur: PourLesIdentifiants,
    val regleDePrix: PourCalculerLePrix // au lieu de cette interface
// si on faisait du fonctionnel, on aurait passé juste une lambda (duree: Int) -> Monnaie
) {

    fun creation(heureEntree: LocalDateTime, dureeMinutes: Int): Result<Ticket>  {

        val tempsDemmande=  dureeMinutes * Time.Companion.minutes;
        val temp = Ticket.Companion.builder( heureEntree, tempsDemmande,  Monnaie.Companion.Euros(0.0))
        val ticket = temp .map {
            it.copy(
                id = this.idGenerateur.idSuivant(),
                prix = this.regleDePrix.calculPrix(dureeMinutes)
            )
        }
          return ticket
    }

    // une amélioration serait d'introduire des types de tickets pour décrire les étapes:
    // ticket avec ID (mais sans prix)
    // ticket avec prix



}
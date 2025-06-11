package location.domain.usine

import kotlinx.datetime.LocalDateTime
import location.domain.entities.Ticket
import location.domain.valueObjects.Monnaie
import location.ports.PourLesIdentifiants
import kotlin.time.Duration.Companion.minutes

class UsineDeTickets(
    val idGenerateur: PourLesIdentifiants,
    val regleDePrix: PourCalculerLePrix // au lieu de cette interface
// si on faisait du fonctionnel, on aurait passé juste une lambda (duree: Int) -> Monnaie
) {

    fun creation(heureEntree: LocalDateTime, dureeMinutes: Int): Result<Ticket>  {

        val tempsDemmande=  dureeMinutes.minutes;
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
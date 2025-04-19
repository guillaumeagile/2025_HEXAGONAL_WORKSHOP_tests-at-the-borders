package location.domain.entities

import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.Time.Companion.seconds
import io.nacular.measured.units.times
import kotlinx.datetime.LocalDateTime

data class Ticket(
    val id : String,
    val usagerId : String,  // son email
    val momentEntree: LocalDateTime,
    val dureeDeLocation: Measure<Time>, // à changer par un value object qui porte la règle de temps maximal de locatiin
    val momentSortie: LocalDateTime
) {
    companion object {
        fun bidon(): Ticket {
            return Ticket(
                id = "fake",
                usagerId = "fake@user.com",
                momentEntree = LocalDateTime.parse("2000-01-01T00:00:00"),
                dureeDeLocation = (0 * seconds),
                momentSortie = LocalDateTime.parse("2000-01-01T00:00:00")
            )
        }

        fun builder(momentEntree: LocalDateTime, dureeDeLocation: Measure<Time>): Result<Ticket> =
            Result.success(
                Ticket.bidon()
            )
    }
}

// quelles sont les régles métier du ticket? (à traduire en tests)


// val id: String ,  //extarnilser la création du Id du ticket
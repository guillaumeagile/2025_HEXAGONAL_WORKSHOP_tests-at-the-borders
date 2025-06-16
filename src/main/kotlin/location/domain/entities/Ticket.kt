package location.domain.entities


import kotlinx.datetime.*
import location.domain.valueObjects.Monnaie
import kotlin.time.Duration


data class Ticket(
    val id: String,
    val usagerId: String,  // son email
    val momentEntree: LocalDateTime,
    val dureeDeLocation: Duration, // à changer par un value object qui porte la règle de temps maximal de locatiin
    val momentSortie: LocalDateTime,
    val prix: Monnaie
) {

    companion object {

        fun enEchec(): Ticket =
            Ticket(
                id = "",
                usagerId = "invalide",
                momentEntree = LocalDateTime.parse("2000-01-01T00:00:00"),
                dureeDeLocation = Duration.ZERO,
                momentSortie = LocalDateTime.parse("2000-01-01T00:00:00"),
                prix = Monnaie.Euros(0.0)
            )

        fun builder(momentEntree: LocalDateTime, dureeDeLocation:  Duration, prix: Monnaie): Result<Ticket> =
            Result.success(
                Ticket.enEchec().copy(
                    momentEntree = momentEntree,
                    dureeDeLocation = dureeDeLocation,
                    prix = prix
                )
            )

        fun BuildOne() : Ticket {
            val momentEntree = LocalDateTime.parse("2025-01-01T00:00:00").toInstant(TimeZone.currentSystemDefault())
            val momentSortie = momentEntree.plus(5, DateTimeUnit.MINUTE)
            val duration = momentSortie - momentEntree  // Fixed: Calculate positive duration
            return Ticket(
                id = "1",
                usagerId = "userID",
                momentEntree = momentEntree.toLocalDateTime(TimeZone.currentSystemDefault()),
                dureeDeLocation = duration,
                momentSortie = momentSortie.toLocalDateTime(TimeZone.currentSystemDefault()),
                prix = Monnaie.Euros(0.0)
            )
        }
    }
}

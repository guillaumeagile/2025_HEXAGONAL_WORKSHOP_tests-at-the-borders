package atelier2025.entites

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.nacular.measured.units.Time
import io.nacular.measured.units.times
import kotlinx.datetime.LocalDateTime
import location.domain.entities.Ticket
import location.domain.valueObjects.Monnaie
import kotlin.time.Duration.Companion.minutes

class TicketTests : StringSpec({

    "le ticket bidon ne sert pas à grand chose" .config(enabled = true) {

        val sut =  Ticket.Companion.enEchec()

        sut.dureeDeLocation shouldBe 0 * Time.Companion.minutes
        sut.momentEntree shouldBe LocalDateTime.Companion.parse("2000-01-01T00:00:00")
        sut.momentSortie    shouldBe LocalDateTime.Companion.parse("2000-01-01T00:00:00")
    }

    // TODO a faire passer
    "le ticket ne peut avoir un montant à zero ou négatif" .config(enabled = false) {

        val sut =  Ticket.Companion.builder( momentEntree = LocalDateTime.Companion.parse("2000-01-01T00:00:00"),
            dureeDeLocation = 0.minutes, prix = Monnaie.Companion.Euros(-1.0))

        sut.isFailure shouldBe true
    }





    "la durée ne doit pas être inférieure à 30mn" .config(enabled = false) {

    }
}) {

}
package atelier2025.ateliers

import atelier2025.entites.RegleDePrixPourTests
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotContain
import io.nacular.measured.units.Time
import io.nacular.measured.units.times
import kotlinx.datetime.LocalDateTime
import location.domain.usine.UsineDeTickets
import location.utilities.TestableIdGenerateur
import location.utilities.ulidGenerateur

class UsineTests
    : StringSpec({


    "le ticket doit avoir un generateur qui s'occupe de l'ID" .config(enabled = true) {

        var ticketGenerateur= UsineDeTickets(TestableIdGenerateur(), RegleDePrixPourTests )
        val ticket = ticketGenerateur.creation(
            LocalDateTime(2016, 2, 15, 16, 57, 0, 0),
            42)

        ticket.isSuccess shouldBe true
        ticket.getOrNull()?.id shouldBe "fakeId-1"
        ticket.getOrNull()?.dureeDeLocation shouldBe 42 * Time.Companion.minutes
        ticket.getOrNull()?.momentEntree?.year shouldBe 2016
        ticket.getOrNull()?.momentEntree?.dayOfMonth shouldBe 15
    }



    "le ticket doit avoir un generateur qui s'occupe de l'ID et garanti que un 2e ticket possede un Id different"
        .config(enabled = true) {

            var ticketGenerateur= UsineDeTickets(ulidGenerateur, RegleDePrixPourTests )
            val ticket1 = ticketGenerateur.creation(
                LocalDateTime(2016, 2, 15, 16, 57, 0, 0),
                42)

            val ticket2 = ticketGenerateur.creation(
                LocalDateTime(2016, 2, 15, 16, 57, 0, 0),
                42)

            ticket1.getOrNull()?.id shouldNotContain  "fakeId"
            ticket1.getOrNull()?.id shouldNotBe ticket2.getOrNull()?.id
        }

})
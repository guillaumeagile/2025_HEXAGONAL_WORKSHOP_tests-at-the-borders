package exercice_1.regles_metier

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.nacular.measured.units.Time.Companion.minutes
import io.nacular.measured.units.times
import kotlinx.datetime.LocalDateTime
import location.domain.entities.Ticket
import location.domain.entities.UsineDeTickets
import location.domain.valueObjects.Monnaie
import location.utilities.UlidGenerateur

class `1_TestTicket` : StringSpec({

    "le ticket bidon ne sert pas à grand chose" .config(enabled = true) {

        val sut =  Ticket.genererUnFake()

        sut.dureeDeLocation shouldBe 0 * minutes
        sut.momentEntree shouldBe LocalDateTime.parse("2000-01-01T00:00:00")
        sut.momentSortie    shouldBe LocalDateTime.parse("2000-01-01T00:00:00")
    }

    // TODO a faire passer
    "le ticket ne peut avoir un montant à zero ou négatif" .config(enabled = false) {

        val sut =  Ticket.builder( momentEntree = LocalDateTime.parse("2000-01-01T00:00:00"),
            dureeDeLocation = 0 * minutes, prix = Monnaie.Euros(-1.0))


        sut.isFailure shouldBe true
    }



    "le ticket doit avoir un generateur qui s'occupe de l'ID" .config(enabled = false) {
        // remplacer IdGenerateur par un fake+spy  (ca veut dire un contrat derriere => ULID.Suivant())
        // montrer comment hors du test, c'est un UlidGenerateur qui va prendre la place

        var ticketGenerateur=  UsineDeTickets(UlidGenerateur)
        val ticket = ticketGenerateur.creation(
            LocalDateTime(2016, 2, 15, 16, 57, 0, 0),
            42)

      //  ticket.id shouldNotBe null
      //  ticket.id shouldNotBe "quelque chose de fixe"
        ticket.dureeDeLocation shouldBe 42 * minutes
        ticket.momentEntree.year shouldBe 2016
        ticket.momentEntree.dayOfMonth shouldBe 15
    }








    "le ticket doit avoir un generateur qui s'occupe de l'ID et garanti que un 2e ticket possede un Id different"
        .config(enabled = false) {

        var ticketGenerateur=  UsineDeTickets(UlidGenerateur)
        val ticket1 = ticketGenerateur.creation(
            LocalDateTime(2016, 2, 15, 16, 57, 0, 0),
            42)

        val ticket2 = ticketGenerateur.creation(
            LocalDateTime(2016, 2, 15, 16, 57, 0, 0),
            42)

       // ticket1.id shouldNotBe ticket2.id
    }


    "la durée ne doit pas être inférieure à 30mn" .config(enabled = false) {

    }
}) {

}



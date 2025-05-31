package atelier2025.useCases

import adapters.FauxStockage
import adapters.autres_adapters_fakes.FausseHorloge
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import location.domain.entities.UsineDeTickets
import location.domain.useCases.PaiementLocation
import location.domain.valueObjects.DureeDeLocation
import location.domain.valueObjects.Monnaie
import location.utilities.TestableIdGenerateur

class PaiementLocationTests: BehaviorSpec( {

    // ce test doit rester une page blanche, il ne doit pas supposer de quoique ce soit de technique sur le stockage
    Given("le use case de paiement de location") {

        val fauxStockageDeTickets = FauxStockage()
        val usineDeTickets = UsineDeTickets(TestableIdGenerateur())
        val fausseHorloge = FausseHorloge( LocalDateTime.parse("2025-06-01T00:00:00") )

        val sut = PaiementLocation( fauxStockageDeTickets, usineDeTickets, fausseHorloge)

        //
        When("je loue pour 15 minutes") {

            val actualTicket = sut.PayerLocationImmediate (DureeDeLocation(15))

            Then("le cout est de 0,25€") {

                actualTicket.id shouldBe "fakeId-1"
                actualTicket.momentEntree shouldBe LocalDateTime.parse("2025-06-01T00:00:00")
                actualTicket.prix shouldBe  Monnaie.Euros(0.25)

            }
            Then("le ticket est enregistré") {
                fauxStockageDeTickets.count() shouldBe 1
            }
        }

        // ce test est plus pur au niveau du métier (domaine)
        When("je loue pour 3 minutes") {

            val actualTicket = sut.PayerLocationImmediate (DureeDeLocation(3))

            Then("le cout est de 0,25€") {
                actualTicket.prix shouldBe  Monnaie.Euros(0.25)
            }
            Then("le ticket est obtenable") {
               val actualObtenu =   sut.ObtenirTicket(actualTicket.id)
                actualObtenu shouldBe actualTicket
            }
        }
    }



})
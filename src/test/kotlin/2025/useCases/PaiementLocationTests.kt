package `2025`.useCases

import adapters.FauxStockage
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import location.domain.useCases.PaiementLocation
import location.domain.valueObjects.DureeDeLocation
import location.domain.valueObjects.Monnaie

class PaiementLocationTests: BehaviorSpec( {

    // ce test doit rester une page blanche, il ne doit pas supposer de quoique ce soit de technique sur le stockage
    Given("le use case de paiement de location") {

        val fauxStockageDeTickets = FauxStockage()
        val sut = PaiementLocation( fauxStockageDeTickets)

        //
        When("je loue pour 15 minutes") {

            val actualTicket = sut.PayerLocationImmediate (DureeDeLocation(15))

            Then("le cout est de 0,25€") {
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
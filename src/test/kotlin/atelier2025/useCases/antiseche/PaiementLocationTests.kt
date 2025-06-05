package atelier2025.useCases.antiseche

import adapters.autres_adapters_fakes.FausseHorloge
import atelier2025.adapters.driven.storage.InMemoryStockage
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import location.domain.useCases.PaiementLocation
import location.domain.usine.UsineDeTickets
import location.domain.usine.regles.CalculPrixHauteSaison
import location.domain.valueObjects.DureeDeLocation
import location.ports.PourAvoirHeure
import location.utilities.TestableIdGenerateur


class PaiementLocationTests: BehaviorSpec( {

    // ce test doit rester une page blanche, il ne doit pas supposer de quoique ce soit de technique sur le stockage
    Given("le use case de paiement de location") {
        val fausseHorloge = FausseHorloge(LocalDateTime.Companion.parse("2025-06-01T00:00:00")) as PourAvoirHeure
       // val adapter =   XAdapter() as PourStockage  // à vous de jouer:
        // écrivez le port et l'adapter qui va bien pour notre métier
        val doublurePourStockage = InMemoryStockage()

        val usineDeTickets = UsineDeTickets(TestableIdGenerateur(), CalculPrixHauteSaison())
        // on peut aussi utiliser RegleDePrixPourTests

        val aTester = PaiementLocation(usineDeTickets, doublurePourStockage, fausseHorloge)

        When("je loue pour 15 minutes") {

            doublurePourStockage.reset()
            val actualTicket = aTester.payerLocationImmediate (DureeDeLocation(15))


            Then("le ticket est enregistré") {
                actualTicket.isSuccess shouldBe true
                doublurePourStockage.length shouldBe 1
            }
        }

        When("je fais rien"){
            doublurePourStockage.reset()

            Then("le ticket n'est pas enregistré") {
                doublurePourStockage.length shouldBe 0
            }
        }



        // ce test est plus pur au niveau du métier (domaine)
        When("je loue pour 3 minutes") {

            val actualTicket = aTester.payerLocationImmediate (DureeDeLocation(3))

           /*   // le test de la règle de calcul de prix est complètement couvert par les tests unitaires ailleurs
           Then("le cout est de 0,25€") {
                actualTicket.prix shouldBe  Monnaie.Euros(0.25)
            }*/
            Then("je peux obtenir le ticket à tout moment") {
             //  val actualObtenu =   aTester.obtenirTicket(actualTicket.id)
             //   actualObtenu shouldBe actualTicket
            }
        }
    }



})
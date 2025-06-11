package atelier2025.useCases.antiseche

import adapters.autres_adapters_fakes.FausseHorloge
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import location.domain.useCases.PaiementLocation
import location.domain.usine.UsineDeTickets
import location.domain.usine.regles.CalculPrixHauteSaison
import location.domain.valueObjects.DureeDeLocation
import location.domain.valueObjects.Monnaie
import location.ports.PourAvoirHeure
import location.utilities.TestableIdGenerateur
import kotlin.time.Duration.Companion.minutes

class PaiementLocationTests: BehaviorSpec( {

    // ce test doit rester une page blanche, il ne doit pas supposer de quoique ce soit de technique sur le stockage
    Given("le use case de paiement de location") {
        val fausseHorloge = FausseHorloge(LocalDateTime.Companion.parse("2025-06-01T00:00:00")) as PourAvoirHeure
        val adapter = adapters.driven.stockage.antiseche.XAdapter() as  location.ports.antiseche. PourTickets  // à vous de jouer:
        // écrivez le port et l'adapter qui va bien pour notre métier

        val usineDeTickets = UsineDeTickets(TestableIdGenerateur(), CalculPrixHauteSaison())
        // on peut aussi utiliser RegleDePrixPourTests

        val sut = PaiementLocation(usineDeTickets, adapter, fausseHorloge)

        When("je loue pour 15 minutes") {

            val actualTicket = sut.payerLocationImmediate (DureeDeLocation(15))

            Then("le cout est de 0,25€") {

                actualTicket.id shouldBe "fakeId-1"
                actualTicket.momentEntree shouldBe LocalDateTime.Companion.parse("2025-06-01T00:00:00")
                //A FAIRE PASSER: actualTicket.momentSortie shouldBe LocalDateTime.parse("2025-06-01T00:15:00")
                actualTicket.dureeDeLocation shouldBe 15 .minutes
                actualTicket.prix shouldBe  Monnaie.Companion.Euros(0.25)
            }

            Then("le ticket est enregistré") {
                adapter.count().isSuccess shouldBe true
                adapter.count().getOrNull() shouldBe 1
            }
        }

        // ce test est plus pur au niveau du métier (domaine)
        When("je loue pour 3 minutes") {

            val actualTicket = sut.payerLocationImmediate (DureeDeLocation(3))

           /*   // le test de la règle de calcul de prix est complètement couvert par les tests unitaires ailleurs
           Then("le cout est de 0,25€") {
                actualTicket.prix shouldBe  Monnaie.Euros(0.25)
            }*/
            Then("je peux obtenir le ticket à tout moment") {
               val actualObtenu =   sut.obtenirTicket(actualTicket.id)
                actualObtenu shouldBe actualTicket
            }
        }
    }



})
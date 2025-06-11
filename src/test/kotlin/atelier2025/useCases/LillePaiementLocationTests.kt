package atelier2025.useCases

import adapters.autres_adapters_fakes.FausseHorloge
import atelier2025.adapters.driven.storage.fake.UnTruc
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import location.domain.useCases.LillePaiementLocation
import location.domain.usine.UsineDeTickets
import location.domain.usine.regles.CalculPrixHauteSaison
import location.domain.valueObjects.DureeDeLocation
import location.domain.valueObjects.Monnaie
import location.ports.PourAvoirHeure
import location.ports.PourQuelqueChose
import location.utilities.TestableIdGenerateur
import kotlin.time.Duration.Companion.minutes

class LillePaiementLocationTests: BehaviorSpec( {

    // ce test doit rester une page blanche, il ne doit pas supposer de quoique ce soit de technique sur le stockage
    Given("le use case de paiement de location") {
        val fausseHorloge = FausseHorloge(LocalDateTime.Companion.parse("2025-06-01T00:00:00")) as PourAvoirHeure

        val adapter = UnTruc() as PourQuelqueChose  // à vous de jouer:
        // écrivez le port et l'adapter qui va bien pour notre métier

        val usineDeTickets = UsineDeTickets(TestableIdGenerateur(), CalculPrixHauteSaison())
        // on peut aussi utiliser RegleDePrixPourTests

        val sut = LillePaiementLocation(usineDeTickets, adapter, fausseHorloge)

        When("je loue pour 15 minutes") {

            val actualTicket = sut.payerLocationImmediate (DureeDeLocation(15))

            Then("le cout est de 0,25€") { // est ce que j'ai vrai besoin de re vérifier la regle de prix ?
                actualTicket.id shouldBe "fakeId-1"
                actualTicket.momentEntree shouldBe LocalDateTime.Companion.parse("2025-06-01T00:00:00")
                //A FAIRE PASSER: actualTicket.momentSortie shouldBe LocalDateTime.parse("2025-06-01T00:15:00")
                actualTicket.dureeDeLocation shouldBe 15 .minutes
                actualTicket.prix shouldBe  Monnaie.Companion.Euros(0.25)
            }

            Then("le ticket est enregistré") {
                // je fais comment pour savoir si le ticket est enregistré ?
            }
        }

        // ce test est plus pur au niveau du métier (domaine)
        When("je loue pour 3 minutes") {

            val actualTicket = sut.payerLocationImmediate (DureeDeLocation(3))

            Then("je peux obtenir le ticket à tout moment") {
             // voir ce qu'on peut faire avec le port
            }
        }
    }



})
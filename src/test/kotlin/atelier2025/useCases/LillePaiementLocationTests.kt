package atelier2025.useCases

import antiseche.adapters.autres_adapters_fakes.FausseHorloge
import atelier2025.adapters.driven.storage.fake.UnEspionEtFakeDePersistence
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import location.domain.useCases.LillePaiementLocation
import location.domain.usine.UsineDeTickets
import location.domain.usine.regles.CalculPrixHauteSaison
import location.domain.valueObjects.DureeDeLocation
import location.ports.PourAvoirHeure
import location.utilities.TestableIdGenerateur

class LillePaiementLocationTests: BehaviorSpec( {

    // ce test doit rester une page blanche, il ne doit pas supposer de quoique ce soit de technique sur le stockage
    Given("le use case de paiement de location") {
        val fausseHorloge = FausseHorloge(LocalDateTime.Companion.parse("2025-06-01T00:00:00")) as PourAvoirHeure

        val usineDeTickets = UsineDeTickets(TestableIdGenerateur(), CalculPrixHauteSaison())
        // on peut aussi utiliser RegleDePrixPourTests

        When("je loue pour 15 minutes") {

            val adapter = UnEspionEtFakeDePersistence()
            val sut = LillePaiementLocation(usineDeTickets, adapter, fausseHorloge)

            val actualTicket = sut.payerLocationImmediate (DureeDeLocation(15))


            Then("je veux savoir si l'adaptateur a été appelé") {
                // version metier
               // sut.donneMoiTousLesTicketsExistants()

                // version technique
               adapter.compterLesTicketsExistants() shouldBe 1

                 //version mockist   / vérifier que adapter a été appelé / SPY
                adapter.laMethodeAEtéAppelée() shouldBe 1
            }
        }

        When("je ne fais rien") {
            val adapter = UnEspionEtFakeDePersistence()
            Then("je veux savoir que l'adaptateur n'a été pas appelé") {
                //version mockist   / vérifier que adapter a été appelé / SPY
                adapter.laMethodeAEtéAppelée() shouldBe 0
            }
            Then("je veux savoir que rien n'a été stocké") {
                adapter.compterLesTicketsExistants() shouldBe 0
            }
        }

        // ce test est plus pur au niveau du métier (domaine)
        When("je loue pour 3 minutes") {
            val adapter = UnEspionEtFakeDePersistence()
            val sut = LillePaiementLocation(usineDeTickets, adapter, fausseHorloge)

            val actualTicket = sut.payerLocationImmediate (DureeDeLocation(3))

            Then("je peux obtenir le ticket à tout moment") {
             // voir ce qu'on peut faire avec le port
            }
        }
    }



})



package atelier2025.useCases.antiseche

import adapters.autres_adapters_fakes.FausseHorloge
import atelier2025.adapters.driven.storage.InMemoryStockage
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.datetime.LocalDateTime
import location.domain.entities.Ticket
import location.domain.useCases.PaiementLocation
import location.domain.usine.UsineDeTickets
import location.domain.usine.regles.CalculPrixHauteSaison
import location.domain.valueObjects.DureeDeLocation
import location.ports.PourAvoirHeure
import location.ports.PourStockage
import location.utilities.TestableIdGenerateur


class PaiementLocationTests: BehaviorSpec( {

    // ce test doit rester une page blanche, il ne doit pas supposer de quoique ce soit de technique sur le stockage
    Given("le use case de paiement de location") {
        val fausseHorloge = FausseHorloge(LocalDateTime.Companion.parse("2025-06-01T00:00:00")) as PourAvoirHeure
       // val adapter =   XAdapter() as PourStockage  // à vous de jouer:
        // écrivez le port et l'adapter qui va bien pour notre métier
        val doublurePourStockage = InMemoryStockage()
        //ou bien un mock?
        val mocked = mockk<PourStockage>(relaxed = true)
        every { mocked.StockerTicket(any<Result<Ticket>>()) } returns  Result.success(Ticket.enEchec())
        // les ennuis commencent:
        // Failed matching mocking signature for
        //io.mockk.MockKException: Failed matching mocking signature for
        //SignedCall(retValue=, isRetValueMock=true, retType=class kotlin.Any, self=PourStockage(#1), method=StockerTicket-bjn95JY(Any), args=[java.lang.Object@7b61a6b5], invocationStr=PourStockage(#1).StockerTicket-bjn95JY(java.lang.Object@7b61a6b5))
        //left matchers: [any()]

        val usineDeTickets = UsineDeTickets(TestableIdGenerateur(), CalculPrixHauteSaison())


        val aTester = PaiementLocation(usineDeTickets, mocked, fausseHorloge)

        When("je loue pour 15 minutes") {

            //doublurePourStockage.reset()
            val actualTicket = aTester.payerLocationImmediate (DureeDeLocation(15))


            Then("le ticket est enregistré") {
                actualTicket.isSuccess shouldBe true
                verify(exactly = 1) { mocked.StockerTicket(any<Result<Ticket>>()) }

                // on peut juste verifier les appels, mais ensuite si on doit mock la relecture
                // c'est trop facile de créer des états incohérents
                // on est incapable de faire des assertions fiables et réalistes sur le stockage

                //d'où l'avantage de la doublure (fake) en mémoire par exemple, dont le comportement va vraiment
                // imiter une base de données, dans differents scénarios (mais il convient de la tester aussi)

                // doublurePourStockage.length shouldBe 1
            }
        }

        When("je fais rien"){
           // doublurePourStockage.reset()

            Then("le ticket n'est pas enregistré") {
             //   doublurePourStockage.length shouldBe 0
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
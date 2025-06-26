package sunny2025.domain.reglesAntiSeche

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import location.domain.usine.regles.CalculPrixHauteSaison
import location.domain.valueObjects.Monnaie

class RegleDePrixStandardTests : FeatureSpec({
    val sut = CalculPrixHauteSaison()

    feature("cout de location: 0,25€ pour 15 minutes") {

        scenario("je loue pour 15 minutes, le prix est de 0,25€") {

            val actual = sut.calculPrix(15)
          //  sut.rembourse(Idticket, partiellement)
            val expected = Monnaie.Euros(0.25)
            actual shouldBe expected
        }

        scenario("je loue pour 60 minutes, le prix est de 1€") {

            val actual = sut.calculPrix(60)
            val expected = Monnaie.Euros(1.0)
            actual shouldBe expected
        }
    }

    feature("tarif par tranche: toute tranche de 15 minutes entamée est due") {

        scenario(  " je loue pour 3 minutes, le prix est de 0,25€") {
            val actual = sut.calculPrix(3)
            val expected = Monnaie.Euros(0.25)
            actual shouldBe expected
        }

        scenario( " je loue pour 26 minutes, le prix est de 0,5€") {
            val actual = sut.calculPrix(26)
            val expected = Monnaie.Euros(0.5)
            actual shouldBe expected
        }
    }


})
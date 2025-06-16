package atelier2025.domain

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe

class RegleDePrixAvecFidelitéTests : FeatureSpec({
    val sut = RemiseAvecProgrammeFidelité()

    feature("determiner la remise selon le nombre de voyages effectués") {

        scenario("si aucun voyage effectué, alors pas de remise ") {

            sut.QuelleRemiseAppliquer shouldBe Remise.Aucune
        }

    }
})
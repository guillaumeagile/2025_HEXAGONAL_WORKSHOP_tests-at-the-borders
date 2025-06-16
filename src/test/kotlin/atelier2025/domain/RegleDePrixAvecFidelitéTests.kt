package atelier2025.domain

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import location.abstractions.PourLireLesLocations

class RegleDePrixAvecFidelitéTests : FeatureSpec({

    val sut = RemiseAvecProgrammeFidelité()


    feature("determiner la remise du client Alice selon le nombre de voyages effectués par elle") {

        scenario("si aucun voyage effectué, alors pas de remise ") {

            sut.QuelleRemiseAppliquer shouldBe Remise.Aucune
        }

        scenario("si 4 voyages ont été payés par Alice, le 5e est gratuit")
        {
            val stockage = FakeX() as PourLireLesLocations

        }

    }
})
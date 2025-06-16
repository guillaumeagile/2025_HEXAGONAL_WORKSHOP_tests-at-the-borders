package atelier2025.domain

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import location.abstractions.PourLireLesLocations
import location.domain.entities.Location

class RegleDePrixAvecFidelitéTests : FeatureSpec({




    feature("determiner la remise du client Alice selon le nombre de voyages effectués par elle") {

        scenario("si aucun voyage effectué, alors pas de remise ") {
            val sut = RemiseAvecProgrammeFidelité(stockage = StubLireLocation() as PourLireLesLocations)
            sut.QuelleRemiseAppliquer shouldBe Remise.Aucune
        }

        scenario("si 4 voyages ont été payés par Alice, le 5e est gratuit")
        {
            val stockage = StubLireLocation() as PourLireLesLocations
            val location1 = Location( "1", "Alice", Remise.Aucune)
            val location2 = Location ( "2", "Alice", Remise.Aucune)
            val location3 = Location( "3", "Alice", Remise.Aucune)
            val location4 = Location( "4", "Alice", Remise.Aucune)

            stockage.enregistrer(location1)
            stockage.enregistrer(location2)
            stockage.enregistrer(location3)
            stockage.enregistrer(location4)

            // setMock  pour que  retourne  NombreDeLocations = 4
            val sut = RemiseAvecProgrammeFidelité(stockage)

            sut.QuelleRemiseAppliquer shouldBe Remise.Totale
        }

        scenario("si dans les 4 derniers locations, une a bénéficié de la remise totale, la 5e n'est pas gratuite")
        {
            val stockage = StubLireLocation() as PourLireLesLocations
            val location1 = Location( "1", "Alice", Remise.Totale)
            val location2 = Location ( "2", "Alice", Remise.Aucune)
            val location3 = Location( "3", "Alice", Remise.Aucune)
            val location4 = Location( "4", "Alice", Remise.Aucune)

            stockage.enregistrer(location1)
            stockage.enregistrer(location2)
            stockage.enregistrer(location3)
            stockage.enregistrer(location4)

            // setMock  pour que  retourne  NombreDeLocations = 4
            val sut = RemiseAvecProgrammeFidelité(stockage)

            sut.QuelleRemiseAppliquer shouldBe Remise.Aucune
        }


    }
})
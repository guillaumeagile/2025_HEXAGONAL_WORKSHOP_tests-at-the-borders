package sunny2025.domain.reglesAntiSeche

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import location.abstractions.PourLireLesLocations
import location.domain.entities.Location
import sunny2025.adapters.antiSeche.StubLireLocation
import sunny2025.domain.Remise

class RegleDePrixAvecFidelitéDeAliceEtBobTests : FeatureSpec({

    //TODO: tenter d'écrire ca avec un mock 😳

    feature("Bob et Alice voyagent, determiner la remise de Alice sans interferer avec Bob")
    {


        scenario("si 3 voyages ont été payés par Alice, le 4e n'est pas gratuit")
        {
            val stockage = StubLireLocation() as PourLireLesLocations
            val location1 = Location("1", "Alice", Remise.Aucune)
            val location2 = Location("2", "Alice", Remise.Aucune)
            val location3 = Location("3", "Alice", Remise.Aucune)
            val location4 = Location("4", "Bob", Remise.Aucune)

            stockage.enregistrer(location1)
            stockage.enregistrer(location2)
            stockage.enregistrer(location3)
            stockage.enregistrer(location4)

            // setMock  pour que  retourne  NombreDeLocations = 4
            val sut = RemiseAvecProgrammeFidelité(stockage)

            sut.quelleRemiseAppliquer() shouldBe Remise.Aucune
        }

        scenario("si Bob fait 0 locations et  si 4 locations ont été payés par Alice, le 1er Bob est payant")
        {
            val stockage = StubLireLocation() as PourLireLesLocations
            val location1 = Location("1", "Alice", Remise.Aucune)
            val location2 = Location("2", "Alice", Remise.Aucune)
            val location3 = Location("3", "Alice", Remise.Aucune)
            val location4 = Location("4", "Alice", Remise.Aucune)

            stockage.enregistrer(location1)
            stockage.enregistrer(location2)
            stockage.enregistrer(location3)
            stockage.enregistrer(location4)

            // setMock  pour que  retourne  NombreDeLocations = 4
            val sut = RemiseAvecProgrammeFidelité(stockage)

            sut.quelleRemiseAppliquer("Bob") shouldBe Remise.Aucune
        }
    }

})
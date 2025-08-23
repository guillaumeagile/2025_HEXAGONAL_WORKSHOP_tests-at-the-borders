package sunny2025.domain

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import location.domain.entities.Location
import sunny2025.adapters.FauxPourRecupererLocation


class RegleDePrixAvecFidelitéTests : FeatureSpec({


    feature("determiner la remise du client Alice selon le nombre de voyages effectués par elle") {

        scenario("si aucun voyage effectué, alors pas de remise ") {
            val sut = RemiseAvecProgrammeFidelité(
                locationAdapter = FauxPourRecupererLocation() as PourRecupérerLesLocations)
            sut.quelleRemiseAppliquer() shouldBe Remise.Aucune
        }

        scenario("si 4 locations ont été payées par Alice, la 5e est gratuit")
        {
            val stockage = FauxPourRecupererLocation() as PourRecupérerLesLocations
            val location1 = Location("1", "Alice", Remise.Aucune)
            val location2 = Location("2", "Alice", Remise.Aucune)
            val location3 = Location("3", "Alice", Remise.Aucune)
            val location4 = Location("4", "Alice", Remise.Aucune)

            stockage.alimenter(location1)
            stockage.alimenter(location2)
            stockage.alimenter(location3)
            stockage.alimenter(location4)

            val sut = RemiseAvecProgrammeFidelité(stockage)

            sut.quelleRemiseAppliquer() shouldBe Remise.Totale
        }

        scenario("si dans les 4 derniers locations, une a bénéficié de la remise totale, la 5e n'est pas gratuite")
        {
            val stockage = FauxPourRecupererLocation() as PourRecupérerLesLocations
            val location1 = Location("1", "Alice", Remise.Totale)
            val location2 = Location("2", "Alice", Remise.Aucune)
            val location3 = Location("3", "Alice", Remise.Aucune)
            val location4 = Location("4", "Alice", Remise.Aucune)

            stockage.alimenter(location1)
            stockage.alimenter(location2)
            stockage.alimenter(location3)
            stockage.alimenter(location4)

            // setMock  pour que  retourne  NombreDeLocations = 4
            val sut = RemiseAvecProgrammeFidelité(stockage)

            sut.quelleRemiseAppliquer() shouldBe Remise.Aucune
        }

        scenario("si 2 voyages ont été payés par Alice, le 3e est payant (aucune remise)")
        {
            val stockage = FauxPourRecupererLocation() as PourRecupérerLesLocations
            val location1 = Location("1", "Alice", Remise.Aucune)
            val location2 = Location("2", "Alice", Remise.Aucune)

            stockage.alimenter(location1)
            stockage.alimenter(location2)

            // setMock  pour que  retourne  NombreDeLocations = 4
            val sut = RemiseAvecProgrammeFidelité(stockage)

            sut.quelleRemiseAppliquer("Alice") shouldBe Remise.Aucune
        }
    }

    feature("Bob et Alice voyagent, determiner la remise de Alice sans interferer avec Bob")
    {


        scenario("si 3 voyages ont été payés par Alice, le 4e n'est pas gratuit")
        {
            val stockage = FauxPourRecupererLocation() as PourRecupérerLesLocations
            val location1 = Location("1", "Alice", Remise.Aucune)
            val location2 = Location("2", "Alice", Remise.Aucune)
            val location3 = Location("3", "Alice", Remise.Aucune)
            val location4 = Location("4", "Bob", Remise.Aucune)

            stockage.alimenter(location1)
            stockage.alimenter(location2)
            stockage.alimenter(location3)
            stockage.alimenter(location4)

            // setMock  pour que  retourne  NombreDeLocations = 4
            val sut = RemiseAvecProgrammeFidelité(stockage)

            sut.quelleRemiseAppliquer() shouldBe Remise.Aucune
        }

        scenario("si Bob fait 0 locations et  si 4 locations ont été payés par Alice, le 1er Bob est payant")
        {
            val stockage = FauxPourRecupererLocation() as PourRecupérerLesLocations
            val location1 = Location("1", "Alice", Remise.Aucune)
            val location2 = Location("2", "Alice", Remise.Aucune)
            val location3 = Location("3", "Alice", Remise.Aucune)
            val location4 = Location("4", "Alice", Remise.Aucune)

            stockage.alimenter(location1)
            stockage.alimenter(location2)
            stockage.alimenter(location3)
            stockage.alimenter(location4)

            // setMock  pour que  retourne  NombreDeLocations = 4
            val sut = RemiseAvecProgrammeFidelité(stockage)

            sut.quelleRemiseAppliquer("Bob") shouldBe Remise.Aucune
        }
    }
})
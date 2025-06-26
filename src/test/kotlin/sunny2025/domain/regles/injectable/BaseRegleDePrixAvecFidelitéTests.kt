package sunny2025.domain.regles.injectable

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import location.abstractions.PourLireLesLocations
import location.domain.entities.Location
import sunny2025.adapters.StubLireLocation
import sunny2025.domain.regles.Remise
import sunny2025.domain.regles.RemiseAvecProgrammeFidelité

open class BaseRegleDePrixAvecFidelitéTests(val stockage: PourLireLesLocations) : FeatureSpec({

    //TODO: tenter d'écrire ca avec un mock 😳

    feature("determiner la remise du client Alice selon le nombre de voyages effectués par elle") {

        scenario("si aucun voyage effectué, alors pas de remise ") {
            val sut = RemiseAvecProgrammeFidelité(stockage = stockage)
            sut.quelleRemiseAppliquer() shouldBe Remise.Aucune
        }

        scenario("si 4 voyages ont été payés par Alice, le 5e est gratuit")
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

            sut.quelleRemiseAppliquer() shouldBe Remise.Totale
        }

        scenario("si dans les 4 derniers locations, une a bénéficié de la remise totale, la 5e n'est pas gratuite")
        {

            val location1 = Location("1", "Alice", Remise.Totale)
            val location2 = Location("2", "Alice", Remise.Aucune)
            val location3 = Location("3", "Alice", Remise.Aucune)
            val location4 = Location("4", "Alice", Remise.Aucune)

            stockage.enregistrer(location1)
            stockage.enregistrer(location2)
            stockage.enregistrer(location3)
            stockage.enregistrer(location4)

            // setMock  pour que  retourne  NombreDeLocations = 4
            val sut = RemiseAvecProgrammeFidelité(stockage)

            sut.quelleRemiseAppliquer() shouldBe Remise.Aucune
        }

        scenario("si 2 voyages ont été payés par Alice, le 3e est payant (aucune remise)")
        {

            val location1 = Location("1", "Alice", Remise.Aucune)
            val location2 = Location("2", "Alice", Remise.Aucune)

            stockage.enregistrer(location1)
            stockage.enregistrer(location2)

            // setMock  pour que  retourne  NombreDeLocations = 4
            val sut = RemiseAvecProgrammeFidelité(stockage)

            sut.quelleRemiseAppliquer("Alice") shouldBe Remise.Aucune
        }
    }
})
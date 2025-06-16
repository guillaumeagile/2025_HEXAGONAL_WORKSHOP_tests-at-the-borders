package atelier2025.domain

import location.abstractions.PourLireLesLocations
import location.domain.entities.Location

class StubLireLocation : PourLireLesLocations {

    val locations = mutableListOf<Location>()


    override fun enregistrer(location: Location) {
        locations.add(location)
    }

    override fun NombreDeLocations(): Int {
      return locations.size
    }

    override fun donneMoiLes4DernieresLocations(): List<Location> {
        return locations .take(4)
    }
}

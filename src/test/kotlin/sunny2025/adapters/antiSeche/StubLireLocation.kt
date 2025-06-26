package sunny2025.adapters.antiSeche

import location.abstractions.PourLireLesLocations
import location.domain.entities.Location

class StubLireLocation : PourLireLesLocations {

    val locations = mutableListOf<Location>()


    override fun enregistrer(location: Location) {
        locations.add(location)
    }

    override fun nombreDeLocations(client: String): Int {
      return locations.filter { it.client == client }  .size
    }

    override fun donneMoiLes4DernieresLocations( client: String): List<Location> {
        return locations  .filter { it.client == client } .take(4)
    }

    override fun reset() {
        locations.clear() // Reset the list of locations
    }
}
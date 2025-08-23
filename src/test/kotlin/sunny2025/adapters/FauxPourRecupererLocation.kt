package sunny2025.adapters

import location.domain.entities.Location
import sunny2025.domain.PourRecupérerLesLocations

class FauxPourRecupererLocation : PourRecupérerLesLocations {

    val memoryLocations = mutableListOf<Location>()

    override fun récupererLes4PlusRécentes(): List<Location> {
       return memoryLocations
    }

    override fun récupererLeNombre(): Int {
        TODO("Not yet implemented")
    }

    override fun alimenter(location: Location) {
        memoryLocations.add(location)
    }
}
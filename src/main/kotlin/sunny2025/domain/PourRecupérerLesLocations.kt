package sunny2025.domain

import location.domain.entities.Location

interface PourRecupérerLesLocations  {

    fun récupererLes4PlusRécentes() : List<Location>

    fun récupererLeNombre() : Int

    fun alimenter(location: Location)
}

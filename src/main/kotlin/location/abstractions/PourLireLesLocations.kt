package location.abstractions

import location.domain.entities.Location

interface PourLireLesLocations
{
    fun enregistrer(location: Location)
}
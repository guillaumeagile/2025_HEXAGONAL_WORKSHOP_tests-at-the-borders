package atelier2025.domain

import location.abstractions.PourLireLesLocations
import location.domain.entities.Location

class StubLireLocation : PourLireLesLocations {
    override fun enregistrer(location: Location) {

    }

    override fun NombreDeLocations(): Int {
        TODO("Not yet implemented")
    }
}

package location.abstractions

import location.domain.entities.Location

interface PourLireLesLocations
{
    fun enregistrer(location: Location)

    fun NombreDeLocations() : Int

    fun donneMoiLes4DernieresLocations() : List<Location>

   // fun DonneMoiUneQueryable() : IQueryable
}
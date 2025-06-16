package location.abstractions

import location.domain.entities.Location

interface PourLireLesLocations
{
    fun enregistrer(location: Location)

    fun NombreDeLocations(client: String = "Alice") : Int

    fun donneMoiLes4DernieresLocations( client: String = "Alice") : List<Location>

   // fun DonneMoiUneQueryable() : IQueryable
}
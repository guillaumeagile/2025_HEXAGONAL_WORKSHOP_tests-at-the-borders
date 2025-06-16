package atelier2025.domain

import location.abstractions.PourDeterminerUneRemiseCommerciale
import location.abstractions.PourLireLesLocations

class RemiseAvecProgrammeFidelité(val stockage: PourLireLesLocations) : PourDeterminerUneRemiseCommerciale {


    fun getQuelleRemiseAppliquer(client : String = "Alice"): Remise =
        when {
            stockage.NombreDeLocations( client) == 0 -> Remise.Aucune
            stockage.donneMoiLes4DernieresLocations( client).any() { it.remise == Remise.Totale } -> Remise.Aucune
            else -> Remise.Totale
        }

}
package atelier2025.domain

import location.abstractions.PourDeterminerUneRemiseCommerciale
import location.abstractions.PourLireLesLocations

class RemiseAvecProgrammeFidelité(val stockage: PourLireLesLocations) : PourDeterminerUneRemiseCommerciale {


    fun getQuelleRemiseAppliquer(client : String = "Alice"): Remise =
        when {
            stockage.NombreDeLocations() == 0 -> Remise.Aucune
            stockage.donneMoiLes4DernieresLocations().any() { it.remise == Remise.Totale } -> Remise.Aucune
            else -> Remise.Totale
        }

}
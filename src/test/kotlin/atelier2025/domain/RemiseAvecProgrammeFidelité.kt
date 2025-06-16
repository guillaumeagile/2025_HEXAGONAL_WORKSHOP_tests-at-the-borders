package atelier2025.domain

import location.abstractions.PourDeterminerUneRemiseCommerciale
import location.abstractions.PourLireLesLocations

class RemiseAvecProgrammeFidelité(val stockage: PourLireLesLocations) : PourDeterminerUneRemiseCommerciale {


    val QuelleRemiseAppliquer: Remise
        get() =
            when {
                stockage.NombreDeLocations() == 0 -> Remise.Aucune
                stockage.donneMoiLes4DernieresLocations().any() { it.remise == Remise.Totale } -> Remise.Aucune
                else -> Remise.Totale
            }

}
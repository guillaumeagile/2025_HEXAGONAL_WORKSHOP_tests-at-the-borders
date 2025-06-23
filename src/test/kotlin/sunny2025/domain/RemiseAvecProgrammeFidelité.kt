package sunny2025.domain

import location.abstractions.PourDeterminerUneRemiseCommerciale
import location.abstractions.PourLireLesLocations

class RemiseAvecProgrammeFidelité(val stockage: PourLireLesLocations) : PourDeterminerUneRemiseCommerciale {


    fun getQuelleRemiseAppliquer(client: String = "Alice"): Remise =
        when {
            stockage.nombreDeLocations(client) == 0 -> Remise.Aucune
            stockage.donneMoiLes4DernieresLocations(client).any() { it.remise == Remise.Totale } -> Remise.Aucune
            else -> Remise.Totale
        }

    companion object {
        const val SEUIL_REMISE_TOTAL = 4
    }

    fun getQuelleRemiseAppliquer2(client: String = "Alice"): Remise {
        if (stockage.nombreDeLocations(client) == SEUIL_REMISE_TOTAL &&
            stockage.donneMoiLes4DernieresLocations(client).all({ it.remise == Remise.Aucune })
        )
            return Remise.Totale
        return Remise.Aucune
    }
}
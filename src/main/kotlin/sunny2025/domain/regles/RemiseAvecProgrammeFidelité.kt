package sunny2025.domain.regles

import location.abstractions.PourDeterminerUneRemiseCommerciale
import location.abstractions.PourLireLesLocations
import sunny2025.domain.Remise

class RemiseAvecProgrammeFidelité(val stockage: PourLireLesLocations) : PourDeterminerUneRemiseCommerciale {

    companion object {
        const val SEUIL_REMISE_TOTAL = 4
    }

    override fun quelleRemiseAppliquer(client: String ): Remise {
        if (stockage.nombreDeLocations(client) == SEUIL_REMISE_TOTAL &&
            stockage.donneMoiLes4DernieresLocations(client).all({ it.remise == Remise.Aucune }))
            return Remise.Totale
        return Remise.Aucune
    }
}
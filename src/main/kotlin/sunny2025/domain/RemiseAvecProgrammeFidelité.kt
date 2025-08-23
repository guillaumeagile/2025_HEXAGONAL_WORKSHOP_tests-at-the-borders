package sunny2025.domain

import location.abstractions.PourDeterminerUneRemiseCommerciale

class RemiseAvecProgrammeFidelité(val locationAdapter: PourRecupérerLesLocations) : PourDeterminerUneRemiseCommerciale {

    companion object {
        const val SEUIL_REMISE_TOTAL = 4
    }

    override fun quelleRemiseAppliquer(client: String ): Remise {
        val listeDesLocations =  locationAdapter.récupererLes4PlusRécentes()

        if (listeDesLocations.size == SEUIL_REMISE_TOTAL &&
            listeDesLocations.all({ it.remise == Remise.Aucune }))
            return Remise.Totale
        
        return Remise.Aucune
    }
}
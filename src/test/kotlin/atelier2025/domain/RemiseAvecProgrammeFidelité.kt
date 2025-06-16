package atelier2025.domain

import location.abstractions.PourDeterminerUneRemiseCommerciale
import location.abstractions.PourLireLesLocations

class RemiseAvecProgrammeFidelité(stockage: PourLireLesLocations) : PourDeterminerUneRemiseCommerciale {


    val QuelleRemiseAppliquer: Remise = Remise.Aucune
}
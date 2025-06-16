package atelier2025.domain

import location.abstractions.PourDeterminerUneRemiseCommerciale

class RemiseAvecProgrammeFidelité( ) : PourDeterminerUneRemiseCommerciale {


    val QuelleRemiseAppliquer: Remise = Remise.Aucune
}
package location.abstractions

import sunny2025.domain.regles.Remise

interface PourDeterminerUneRemiseCommerciale {

    fun quelleRemiseAppliquer(client: String = "Alice"): Remise

}
package location.abstractions

import sunny2025.domain.Remise

interface PourDeterminerUneRemiseCommerciale {

    fun quelleRemiseAppliquer(client: String = "Alice"): Remise

}
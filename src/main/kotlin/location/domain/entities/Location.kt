package location.domain.entities

import sunny2025.domain.Remise


data class Location(val id : String , val client : String,  val remise: Remise) {
    companion object {
        fun BuildOne(client: String): Location {
            return Location("1", client, Remise.Aucune)}
    }
}
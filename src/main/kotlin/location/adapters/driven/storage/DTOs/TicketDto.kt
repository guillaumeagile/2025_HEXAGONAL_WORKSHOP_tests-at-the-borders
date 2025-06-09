package location.adapters.driven.storage.DTOs

import io.nacular.measured.units.Measure
import kotlinx.datetime.LocalDateTime

//à supprimer
data class TicketDto(val id: Int, val elapsedMinutes: Int)

/*
  val id: String,
    val usagerId: String,  // son email
    val momentEntree: LocalDateTime,
    val dureeDeLocation: Measure<Time>, // à changer par un value object qui porte la règle de temps maximal de locatiin
    val momentSortie: LocalDateTime,
    val prix: Monnaie
 */

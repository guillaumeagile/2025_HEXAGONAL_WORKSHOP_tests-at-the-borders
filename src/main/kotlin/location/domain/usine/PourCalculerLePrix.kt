package location.domain.usine

import location.domain.valueObjects.Monnaie

fun interface PourCalculerLePrix {
    fun calculPrix (duree: Int) : Monnaie
}

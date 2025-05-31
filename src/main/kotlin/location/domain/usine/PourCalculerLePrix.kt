package location.domain.usine

import location.domain.valueObjects.Monnaie

fun interface PourCalculerLePrix {
    fun calculPrix (duree: Int) : Monnaie
}

// au lieu de cette interface, on pourrait aussi utiliser une fonction lambda, tout simplement
// dont la signature serait la suivante:  (duree: Int) -> Monnaie

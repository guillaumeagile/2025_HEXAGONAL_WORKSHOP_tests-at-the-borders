package location.domain.usine.regles

import location.domain.usine.PourCalculerLePrix
import location.domain.valueObjects.Monnaie

class CalculPrixHauteSaison : PourCalculerLePrix {

    override fun calculPrix(duree: Int): Monnaie {

      return  Monnaie.Euros(  duree.toDouble() / 60.0 )
    }
}
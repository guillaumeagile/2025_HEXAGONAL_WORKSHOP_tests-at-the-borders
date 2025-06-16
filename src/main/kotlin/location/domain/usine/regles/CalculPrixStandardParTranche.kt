package location.domain.usine.regles

import location.domain.valueObjects.Monnaie
import kotlin.math.ceil

class CalculPrixHauteSaison : location.domain.usine.PourCalculerLePrix {

    override fun calculPrix(duree: Int): Monnaie {

        val trancheDeQuinzaine = ceil(duree.toDouble() / 15.0)

        return Monnaie.Euros(trancheDeQuinzaine * 0.25)
    }
}